package rl.markov;

public interface ObservableState<S, T> extends State {

	public T observe(S observer);
	public double probOfObservation(S observer, T observation);
	
}
