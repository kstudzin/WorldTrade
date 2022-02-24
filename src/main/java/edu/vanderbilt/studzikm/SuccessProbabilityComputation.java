package edu.vanderbilt.studzikm;

public class SuccessProbabilityComputation {

	private double k;
	private double x0;

	public SuccessProbabilityComputation(double k, double x0) {
		this.k = k;
		this.x0 = x0;
	}

	public Double compute(TransferResult result) {
		return 1.0 / ( 1 + Math.exp(k * (result.getOtherReward() - x0)));
	}

	public Double compute(TransformResult result) {
		return 1.0;
	}
}
