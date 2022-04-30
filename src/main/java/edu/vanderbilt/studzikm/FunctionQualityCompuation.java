package edu.vanderbilt.studzikm;

import java.util.Map;
import java.util.function.BiFunction;

/**
 * Implementation of a quality computation that takes a function as input
 */
public class FunctionQualityCompuation implements QualityComputation {

	private BiFunction<Double, Double, Double> function;

	/**
	 * Creates a function quality computation with the default function, which is a normalized Rayleigh distribution.
	 */
	public FunctionQualityCompuation() {
		this((target, actual) ->
				(actual / target) * Math.exp(-((Math.pow(actual, 2) - Math.pow(target, 2)) / (2 * Math.pow(target, 2))))
		);
	}

	/**
	 * Creates a function quality computation with the given function
	 * @param function the function to compute the quality. Must accept a target amount
	 *                    of a resource and the actual amount of a resource
	 */
	public FunctionQualityCompuation(BiFunction<Double, Double, Double> function) {
		this.function = function;
	}

	/**
	 * Computes the quality of the given country
	 * @param country the country to compute the quality of
	 * @return a numeric quality score
	 */
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
