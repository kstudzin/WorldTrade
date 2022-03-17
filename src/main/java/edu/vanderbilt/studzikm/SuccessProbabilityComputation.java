package edu.vanderbilt.studzikm;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SuccessProbabilityComputation {

	Logger log = LogManager.getLogger(SuccessProbabilityComputation.class);

	private double k;
	private double x0;

	public SuccessProbabilityComputation(double k, double x0) {
		this.k = k;
		this.x0 = x0;
	}

	public Double compute(Double reward) {
		return 1.0 / ( 1 + Math.exp(-k * (reward - x0)));
	}
}
