package edu.vanderbilt.studzikm;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;

public class FunctionQualityCompuation implements QualityComputation {

	private BiFunction<Double, Double, Double> function;

	public FunctionQualityCompuation() {
		this((target, actual) ->
				(actual / Math.max(target, 0.000001)) * Math.exp(-((Math.pow(actual, 2) - Math.pow(target, 2)) / (2 * Math.pow(target, 2))))
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
				.mapToDouble(e -> function.apply(country.getTargetAmount(e.getKey().getName()) * 1.0,
						e.getValue() * 1.0) *
						e.getKey().getWeight())
				.sum();
	}

}
