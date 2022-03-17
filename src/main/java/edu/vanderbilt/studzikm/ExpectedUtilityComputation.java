package edu.vanderbilt.studzikm;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Set;

public class ExpectedUtilityComputation {

	Logger log = LogManager.getLogger(ExpectedUtilityComputation.class);

	private double c;
	private SuccessProbabilityComputation successProbabilityComputation;

	public ExpectedUtilityComputation(double c,
			SuccessProbabilityComputation successProbabilityComputation) {
		this.c = c;
		this.successProbabilityComputation = successProbabilityComputation;
	}

	public double compute(ActionResult<?> result, World world, Set<String> involvedParties) {
		// Success probability is the product of success probability of each participating country
		// This only involves one other country so we don't actually need to compute any product.
		// Since we are proposing the schedule exactly because it is best for our country, we will
		// naturally accept it. Therefore we don't need to compute a probability we will accept our 
		// own schedule.
		// 
		// We may need to multiply the products in the future if we allow transfers between more than 
		// two countries.
		double scheduleProbability = involvedParties.stream()
				.map(world::getCountry)
				.mapToDouble(c -> c.computeReward(result))
				.map(successProbabilityComputation::compute)
				.reduce(1, (x, y) -> x * y);
		log.trace("Success probability: " + scheduleProbability);
		return (scheduleProbability * result.getReward()) + ((1 - scheduleProbability) * c);
	}

}
