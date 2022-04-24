package edu.vanderbilt.studzikm;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;

public class FunctionQualityCompuation implements QualityComputation {

	private BiFunction<Double, Double, Double> function;

	public FunctionQualityCompuation() {
		this((target, actual) ->
				(actual / target) * Math.exp(-((Math.pow(actual, 2) - Math.pow(target, 2)) / (2 * Math.pow(target, 2))))
		);
	}

	public FunctionQualityCompuation(BiFunction<Double, Double, Double> function) {
		this.function = function;
	}

	@Override
	public double compute(Country country) {

		return country.getResources()
				.entrySet()
				.stream()
				.mapToDouble(e -> compute(country, e) * e.getKey().getWeight())
				.sum();
	}

	private double compute(Country country, Map.Entry<Resource, Integer> resourceAmount) {
		String resourceName = resourceAmount.getKey().getName();
		Integer targetAmount = country.getTargetAmount(resourceName);
		if (targetAmount == 0) {
			return 1.0;
		}
		return function.apply(targetAmount * 1.0, resourceAmount.getValue() * 1.0);
	}
}
