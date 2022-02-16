package edu.vanderbilt.studzikm;

import java.util.HashMap;
import java.util.Map;

import com.google.common.base.Preconditions;

public class DefaultUtilityComputation implements UtilityComputation {

	private Map<Resource, Integer> proportionalityConsts;

	public DefaultUtilityComputation() {
		this.proportionalityConsts = new HashMap<>();
	}

	public DefaultUtilityComputation(Map<Resource, Integer> proportionalityConsts) {
		Preconditions.checkNotNull(proportionalityConsts, "To use default constants, use the default constructor");
		this.proportionalityConsts = proportionalityConsts;
	}

	@Override
	public double compute(World world, Country country) {
		return country.getResources()
				.entrySet()
				.stream()
				.mapToDouble(e -> proportionalityConsts.getOrDefault(e.getKey(), 1) * 
						e.getKey().getWeight() * 
						e.getValue())
				.sum();
	}

}
