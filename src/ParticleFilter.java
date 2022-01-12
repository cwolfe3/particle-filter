import java.util.ArrayList;
import java.util.List;

import rl.markov.MarkovChain;
import rl.markov.ObservableState;
import rl.markov.Observation;
import rl.markov.Observer;
import rl.markov.State;
import rl.markov.StateGenerator;

public class ParticleFilter {
	
	private MarkovChain chain;
	private StateGenerator<ObservableState<Observation, Observer>> gen;
	private int numParticles;
	private List<Particle> particles = new ArrayList<Particle>();
	
	public ParticleFilter(MarkovChain chain, StateGenerator<ObservableState<Observation, Observer>> gen, int numParticles) {
		this.chain = chain;
		this.gen = gen;
		this.numParticles = numParticles;
		
		init();
	}

	private void init() {
		double prob = 1.0 / numParticles;
		for (int i = 0; i < numParticles; i++) {
			particles.add(new Particle(gen.getState(), prob));
		}
	}
	
	private void transition() {
		for (Particle particle : particles) {
			particle.s = (ObservableState<Observation, Observer>) chain.sample(particle.s);
		}
	}
	
	private void reweight(Observer observer, Observation observation) {
		double total = 0;
		for (Particle particle : particles) {
			particle.weight = particle.s.probOfObservation(observer, observation);
			total += particle.weight;
		}
		
		for (Particle particle : particles) {
			particle.weight /= total;
		}
	}
	
	private void resample() {
		List<Particle> newParticles = new ArrayList<Particle>();
		
		double[] cumSum = new double[numParticles];
		for (int i = 0; i < numParticles; i++) {
			cumSum[i] = particles.get(i).weight;
			if (i > 0) {
				cumSum[i] += cumSum[i - 1];
			}
		}
		for (int i = 0; i < numParticles; i++) {
			double selection = Math.random();
			for (int j = 0; j < numParticles; j++) {
				if (selection <= cumSum[j] || j == numParticles - 1) {
					newParticles.add(new Particle(particles.get(j).s, 1.0 / numParticles));
					break;
				}
			}
		}
		particles = newParticles;
	}
	
	public void iterate(Observer observer, Observation observation) {
		transition();
		reweight(observer, observation);
		resample();
	}
	
	public List<State> getStates() {
		List<State> states = new ArrayList<>();
		for (Particle p : particles) {
			states.add(p.s);
		}
		return states;
	}
	
	public State bestGuess() {
		double max = maxWeight();
		for (Particle p : particles) {
			if (p.weight == max) {
				return p.s;
			}
		}
		return null;
	}
	
	private double maxWeight() {
		double max = 0;
		for (Particle p : particles) {
			max = Math.max(max, p.weight);
		}
		return max;
	}
	
	private class Particle {
		public ObservableState s;
		public double weight;
		
		public Particle(ObservableState s, double weight) {
			this.s = s;
			this.weight = weight;
		}
	}
	
}
