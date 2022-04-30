package edu.vanderbilt.studzikm;

import java.util.HashMap;
import java.util.Map;

import com.google.common.base.Preconditions;

/**
 * Computes the quality of a country based on the resources it has
 */
public class LinearQualityComputation implements QualityComputation {

	private Map<Resource, Integer> proportionalityConsts;

	/**
	 * Creates a quality computation that uses a linear function. Uses default proportionality constant of 1
	 */
	public LinearQualityComputation() {
		this.proportionalityConsts = new HashMap<>();
	}

	/**
	 * Creates a quality computation with a linear function and custom proportionality constants
	 * @param proportionalityConsts map of resources to proportionality constants
	 */
	public LinearQualityComputation(Map<Resource, Integer> proportionalityConsts) {
		Preconditions.checkNotNull(proportionalityConsts, "To use default constants, use the default constructor");
		this.proportionalityConsts = proportionalityConsts;
	}

	/**
	 * Computes the quality for the given country using a linear function
	 * @param country the country to compute quality for
	 * @return the quality score
	 */
	@Override
	public double compute(Country country) {
		return country.getResources()
				.entrySet()

				// Get a stream of resources to amounts
				.stream()

				// Compute score for each resource
				.mapToDouble(e -> proportionalityConsts.getOrDefault(e.getKey(), 1) * 
						e.getKey().getWeight() * 
						e.getValue())

				// Sum per resource scores
				.sum();
	}

}
