package rl.impl;
import rl.markov.Observation;

public class ScalarObservation implements Observation {

	private double s;
	
	public ScalarObservation(double s) {
		this.s = s;
	}
	
	public double getScalar() {
		return s;
	}
	
}
