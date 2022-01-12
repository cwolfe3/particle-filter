package rl.impl;
import rl.markov.State;
import rl.markov.StateGenerator;

public class ConstantStateGenerator<T extends State> extends StateGenerator<T> {
	
	private T s;
	
	public ConstantStateGenerator(T s) {
		this.s = s;
	}

	@Override
	public T getState() {
		return s;
	}

}
