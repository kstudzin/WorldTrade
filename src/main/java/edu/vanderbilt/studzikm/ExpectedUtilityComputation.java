package edu.vanderbilt.studzikm;

import edu.vanderbilt.studzikm.planning.Planner;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Set;

/**
 * Strategy for computing expected utility
 */
public class ExpectedUtilityComputation {

	Logger log = LogManager.getLogger(ExpectedUtilityComputation.class);

	private double c;
	private SuccessProbabilityComputation successProbabilityComputation;

	/**
	 * Creates exepected utility computation
	 * @param c failure pentalty
	 * @param successProbabilityComputation strategy for computing success probability
	 */
	public ExpectedUtilityComputation(double c,
									  SuccessProbabilityComputation successProbabilityComputation) {
		this.c = c;
		this.successProbabilityComputation = successProbabilityComputation;
	}

	/**
	 * Computes the expected utility a country after performing an action
	 * @param result the action performed
	 * @param world the world after the action has been performed
	 * @param involvedParties the countries involved in the trade so far
	 * @param planner planner to determine how desirable the action is within the schedule
	 * @return a numeric expected utility
	 */
	public double compute(ActionResult<?> result,
						  World world,							// TODO Why not use world in result?
						  Set<String> involvedParties,
						  Planner planner) {

		double scheduleProbability = involvedParties.stream()
				.map(world::getCountry)
				.mapToDouble(c -> c.computeReward(result))
				.map(successProbabilityComputation::compute)
				.reduce(1, (x, y) -> x * y);
		log.trace("Success probability: " + scheduleProbability);
		return ((scheduleProbability * result.getReward()) + ((1 - scheduleProbability) * c))
				* planner.score(result);
	}

}
