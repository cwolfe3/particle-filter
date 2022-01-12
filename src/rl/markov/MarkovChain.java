package rl.markov;

public class MarkovChain<T extends State> {

	private T current;
	TransitionFunction<T> trans;
	
	public MarkovChain(T startState, TransitionFunction<T> trans) {
		this.current = startState;
		this.trans = trans;
	}
	
	public void iterate() {
		current = trans.transition(current);
	}
	
	public T getState() {
		return current;
	}
	
	public T sample(T s) {
		return trans.transition(s);
	}
	
}
