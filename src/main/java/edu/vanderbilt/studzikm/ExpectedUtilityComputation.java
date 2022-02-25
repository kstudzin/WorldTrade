package edu.vanderbilt.studzikm;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ExpectedUtilityComputation {

	Logger log = LogManager.getLogger(ExpectedUtilityComputation.class);

	private double c;
	private SuccessProbabilityComputation successProbabilityComputation;

	public ExpectedUtilityComputation(double c,
			SuccessProbabilityComputation successProbabilityComputation) {
		this.c = c;
		this.successProbabilityComputation = successProbabilityComputation;
	}

	public double compute(TransferResult result) {
		// Success probability is the product of success probability of each participating country
		// This only involves one other country so we don't actually need to compute any product.
		// Since we are proposing the schedule exactly because it is best for our country, we will
		// naturally accept it. Therefore we don't need to compute a probability we will accept our 
		// own schedule.
		// 
		// We may need to multiply the products in the future if we allow transfers between more than 
		// two countries.
		double successProbability = successProbabilityComputation.compute(result);
		log.trace("Success probability: " + successProbability);
		return (successProbability * result.getReward()) + ((1 - successProbability) * c);
	}

	public double compute(TransformResult result) {
		// Because successProbability is always 1 when the Action only involves our country, the
		// expected utility computation reduces to simply the discounted reward.
		return result.getReward();
	}
}
