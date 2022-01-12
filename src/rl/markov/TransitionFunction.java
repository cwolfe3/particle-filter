package rl.markov;

public interface TransitionFunction<T extends State> {
	
	public T transition(T s);
	public double prob(T s1, T s2);
	
}
