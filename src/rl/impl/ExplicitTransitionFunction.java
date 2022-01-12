package rl.impl;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rl.markov.State;
import rl.markov.TransitionFunction;

public class ExplicitTransitionFunction<T extends State> implements TransitionFunction<T> {

	private Map<T, Transition> transitions;
	
	public ExplicitTransitionFunction() {
		transitions = new HashMap<>();
	}
	
	public void addState(T s) {
		transitions.put(s, new Transition());
	}
	
	public void addTransition(T s1, T s2, double prob) {
		transitions.get(s1).addTransition(s2,  prob);
	}
	
	private class Transition {
		private List<T> states;
		private List<Double> probs;
		
		public Transition() {
			states = new ArrayList<>();
			probs = new ArrayList<>();
		}
		
		public void addTransition(T s, double prob) {
			states.add(s);
			probs.add(prob);
		}
		
		public T transition(State s) {
			double total = 0;
			for (Double d : probs) {
				total += d;
			}
			
			double choice = Math.random() * total;
			double current = 0;
			
			for (int i = 0; i < states.size(); i++) {
				current += probs.get(i);
				if (choice <= current) {
					return states.get(i);
				}
			}
			return states.get(0);
		}
		
		public double prob(State s1, State s2) {
			for (int i = 0; i < states.size(); i++) {
				if (states.get(i).equals(s2)) {
					return probs.get(i);
				}
			}
			return 0;
		}
		
	}

	@Override
	public T transition(State s) {
		return transitions.get(s).transition(s);
	}

	@Override
	public double prob(State s1, State s2) {
		return transitions.get(s1).prob(s1, s2);
	}
	
}
