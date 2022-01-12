import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;

import rl.impl.ExplicitTransitionFunction;
import rl.impl.UniformStateGenerator;
import rl.markov.MarkovChain;
import rl.markov.State;
import rl.markov.StateGenerator;

public class Main {

	public static void main(String[] args) throws InterruptedException, IOException {
		
		int[][] map = MapLoader.loadMap("resources/map.png");
		int width = map.length;
		int height = map[0].length;
		
		List<Position2DState> states = new ArrayList<>();
		Position2DState[][] statesArray = new Position2DState[width][height];
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				Position2DState s = new Position2DState(x,  y, map[x][y]);
				states.add(s);
				statesArray[x][y] = s;
			}
		}
		
		for (Position2DState p : states) {
			if (p.getX() > 0) {
				if (map[(int)p.getX() - 1][(int)p.getY()] == 0) {
					p.setNextToWall();
				}
			}
			if (p.getY() > 0) {
				if (map[(int)p.getX()][(int)p.getY() - 1] == 0) {
					p.setNextToWall();
				}
			}
			if (p.getX() < width - 1) {
				if (map[(int)p.getX() + 1][(int)p.getY()] == 0) {
					p.setNextToWall();
				}
			}
			if (p.getY() < height - 1) {
				if (map[(int)p.getX()][(int)p.getY() + 1] == 0) {
					p.setNextToWall();
				}
			}
		}
		
		ExplicitTransitionFunction<Position2DState> trans = new ExplicitTransitionFunction<>();
		for (Position2DState s : states) {
			trans.addState(s);
		}
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				trans.addTransition(statesArray[x][y], statesArray[x][y], 1);
				if (map[x][y] == 0) continue;
				if (x > 0 && Math.random() < 0.99) {
					trans.addTransition(statesArray[x - 1][y], statesArray[x][y], 1);
				}
				if (y > 0 && Math.random() < 0.99) {
					trans.addTransition(statesArray[x][y - 1], statesArray[x][y], 1);
				}
				if (x < width - 1 && Math.random() < 0.99) {
					trans.addTransition(statesArray[x + 1][y], statesArray[x][y], 1);
				}
				if (y < height - 1 && Math.random() < 0.99) {
					trans.addTransition(statesArray[x][y + 1], statesArray[x][y], 1);
				}
			}
		}
		
		StateGenerator<Position2DState> stateGen = new UniformStateGenerator<Position2DState>(states);
		Position2DState target = stateGen.getState();

		MarkovChain<Position2DState> gridMovement = new MarkovChain<Position2DState>(target, trans);
		ParticleFilter filter = new ParticleFilter(gridMovement, new UniformStateGenerator(states), 10000);
		
		GridWorldVisualizer vis = new GridWorldVisualizer(width, height);
		
		JFrame window = new JFrame();
		window.setSize(1000, 1000);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.add(vis);
		window.setVisible(true);
		
		new Thread() {
			public void run() {
				while (true) {
					window.repaint();
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}.start();
		
		for (State s : states) {
			Position2DState p = (Position2DState) s;
			vis.setColor((int)p.getX(), (int)p.getY(), p.getColor());
		}
		
		
		while (true) {
			target = gridMovement.sample(target);
			Position2DState bestGuess = (Position2DState) filter.bestGuess();
//			for (int j = 0; j < 1; j++) {
//				if (agent.getX() < bestGuess.getX()) {
//					agent = new Position((int)agent.getX() + 1, (int)agent.getY());
//				}
//				if (agent.getY() < bestGuess.getY()) {
//					agent = new Position((int)agent.getX(), (int)agent.getY() + 1);
//				}
//				if (agent.getX() > bestGuess.getX()) {
//					agent = new Position((int)agent.getX() - 1, (int)agent.getY());
//				}
//				if (agent.getY() > bestGuess.getY()) {
//					agent = new Position((int)agent.getX(), (int)agent.getY() - 1);
//				}
//			}
			//agent = new Position((int)(Math.random() * width), (int)(Math.random() * height));
			filter.iterate(null, target.observe(null));
			
			vis.clearWeights();
			for (State s : filter.getStates()) {
				Position2DState state = (Position2DState) s;
				vis.addWeight((int)state.getX(), (int)state.getY(), 1);
			}
			vis.clearMarkers();
			vis.setMarker((int)target.getX(), (int)target.getY());
			
			Thread.sleep(100);
			
		}
		
	}
	
}
