import rl.impl.ScalarObservation;
import rl.markov.ObservableState;

public class Position2DState implements ObservableState<Position, ScalarObservation> {

	private static double sigma = 3;
	private static int numColors = 8;
	private static double accuracy = 0.9;
	
	private int x;
	private int y;
	private int color;
	private boolean nextToWall;
	
	public Position2DState(int x, int y) {
		this(x, y, 0);
	}
	
	public Position2DState(int x, int y, int color) {
		this.x = x;
		this.y = y;
		this.color = color;
	}
	
	public boolean equals(Object o) {
		if (o instanceof Position2DState) {
			Position2DState s = (Position2DState) o;
			return s.x == x && s.y == y;
		}
		return false;
	}
	
	public int hashCode() {
		return (int)(0.5 * (x + y) * (x + y + 1) + y);
	}

	@Override
	public ScalarObservation observe(Position obvPos) {
//		double delX = obvPos.getX() - x;
//		double delY = obvPos.getY() - y;
//		double dist = Math.sqrt(delX * delX + delY * delY);
//		
//		double r1 = Math.random();
//		double r2 = Math.random();
//		double noise = Math.sqrt(-2 * Math.log(r1)) * Math.cos(2 * Math.PI * r2);
//		return new ScalarObservation(dist + noise);
		if (Math.random() < accuracy) {
			return new ScalarObservation(color * (nextToWall ? -1 : 1));
		} else {
			int res;
			do {
				res = ((int)(Math.random() * numColors) + 1);
			} while (res == color);
			return new ScalarObservation(res * (nextToWall ? -1 : 1));
		}
	}

	@Override
	public double probOfObservation(Position obvPos, ScalarObservation observation) {
//		double delX = obvPos.getX() - x;
//		double delY = obvPos.getY() - y;
//		double dist = Math.sqrt(delX * delX + delY * delY);
//		double res = (1 / (sigma * Math.sqrt(2 * Math.PI))) * Math.exp(-(Math.pow(observation.getScalar() - dist, 2) / (2 * sigma * sigma)));
//		return res;
		if (observation.getScalar() * color * (nextToWall ? -1 : 1) < 0) {
			return 0;
		} else if (Math.abs(observation.getScalar()) == color) {
			return accuracy;
		} else {
			return (1 - accuracy) / (numColors - 1);
		}
	}
	
	public double getX() {
		return x;
	}
	
	public double getY() {
		return y;
	}
	
	public int getColor() {
		return color;
	}
	
	public String toString() {
		return "State (" + x + ", " + y + ")";
	}
	
	public void setNextToWall() {
		nextToWall = true;
	}
	
}
