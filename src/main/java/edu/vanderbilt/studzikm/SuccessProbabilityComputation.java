package edu.vanderbilt.studzikm;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Computes the probability another country will accept the
 * country's proposed schedule. High rewards have a higher
 * probability of success. Specifically the function is a
 * sigmoid and the input is the reward to a country.
 */
public class SuccessProbabilityComputation {

	Logger log = LogManager.getLogger(SuccessProbabilityComputation.class);

	private double k;
	private double x0;

	/**
	 * Creates a success probability computation
	 * @param k the logistic growth rate
	 * @param x0 the value at the sigmoid's midpoint
	 */
	public SuccessProbabilityComputation(double k, double x0) {
		this.k = k;
		this.x0 = x0;
	}

	/**
	 * Computes the probability of success
	 * @param reward the reward of the action for which we are computing the probability of success
	 * @return the probability of success between 0 and 1
	 */
	public Double compute(Double reward) {
		return 1.0 / ( 1 + Math.exp(-k * (reward - x0)));
	}
}
