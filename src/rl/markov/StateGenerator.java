package rl.markov;

public abstract class StateGenerator<T extends State> {

	public abstract T getState();
	
}
