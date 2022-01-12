package rl.impl;
import java.util.List;

import rl.markov.State;
import rl.markov.StateGenerator;

public class UniformStateGenerator<T extends State> extends StateGenerator<T> {

	private List<T> states;
	
	public UniformStateGenerator(List<T> states) {
		this.states = states;
	}

	@Override
	public T getState() {
		return states.get((int)(Math.random() * states.size()));
	}
	
}
