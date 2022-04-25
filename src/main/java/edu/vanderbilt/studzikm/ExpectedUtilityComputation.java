package edu.vanderbilt.studzikm;

import edu.vanderbilt.studzikm.planning.Planner;
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

	public double compute(ActionResult<?> result,
						  World world,
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
