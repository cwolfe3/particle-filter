import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JPanel;

public class GridWorldVisualizer extends JPanel {
	
	private static int CELL_SIZE = 8;

	private int width;
	private int height;
	
	private double[][] weights;
	private int[][] stateColors;
	private boolean[][] markers;
	
	private Color[] colors;
	
	public GridWorldVisualizer(int width, int height) {
		weights = new double[width][height];
		markers = new boolean[width][height];
		stateColors = new int[width][height];
		
		colors = new Color[7];
		colors[0] = Color.WHITE;
		colors[1] = Color.GRAY;
		colors[2] = Color.YELLOW;
		colors[3] = Color.ORANGE;
		colors[4] = Color.MAGENTA;
		colors[5] = Color.BLUE;
		colors[6] = Color.GREEN;

	}
	
	public void setWeight(int i, int j, double weight) {
		weights[i][j] = weight;
	}
	
	public void addWeight(int i, int j, double weight) {
		weights[i][j] += weight;
	}
	
	public void clearWeights() {
		for (int i = 0; i < weights.length; i++) {
			for (int j = 0; j < weights[0].length; j++) {
				weights[i][j] = 0;
			}
		}
	}
	
	public void setColor(int i, int j, int color) {
		stateColors[i][j] = color;
	}
	
	private double totalWeight() {
		double total = 0;
		for (int i = 0; i < weights.length; i++) {
			for (int j = 0; j < weights[0].length; j++) {
				total += weights[i][j];
			}
		}
		return total;
	}
	
	public void setMarker(int i, int j) {
		markers[i][j] = true;
	}
	
	public void clearMarkers() {
		for (int i = 0; i < weights.length; i++) {
			for (int j = 0; j < weights[0].length; j++) {
				markers[i][j] = false;
			}
		}
	}
	
	private double maxWeight() {
		double max = 0;
		for (int i = 0; i < weights.length; i++) {
			for (int j = 0; j < weights[0].length; j++) {
				max = Math.max(max, weights[i][j]);
			}
		}
		return max;
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		double maxWeight = maxWeight();
		for (int i = 0; i < weights.length; i++) {
			for (int j = 0; j < weights[0].length; j++) {
				double cornerX = i * CELL_SIZE;
				double cornerY = j * CELL_SIZE;
				double weight = Math.min(1, weights[i][j] / maxWeight);
				double radius = CELL_SIZE / 2;
				
				if (stateColors[i][j] == 0) {
					g.setColor(Color.BLACK);
				} else {
					g.setColor(colors[stateColors[i][j] - 1]);
				}
				g.fillRect((int)cornerX, (int)cornerY, CELL_SIZE, CELL_SIZE);
				
				g.setColor(Color.BLACK);

				if (!markers[i][j]) {
					g.setColor(new Color(0, 0, 0, (float) weight));
				} else {
					g.setColor(Color.RED);
				}
				g.fillOval((int)cornerX, (int)cornerY, (int)(2 * radius), (int)(2 * radius));
			}
		}
	}
	
}
