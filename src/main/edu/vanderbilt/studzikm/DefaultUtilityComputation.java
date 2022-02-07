package edu.vanderbilt.studzikm;

import java.util.Map.Entry;

public class DefaultUtilityComputation implements UtilityComputation {

	@Override
	public double compute(World world, Country country) {
		double result = 0;

		for (Entry<Resource, Integer> resource : country.getResources().entrySet()) {
			result += resource.getKey().getWeight() * resource.getValue();
		}

		return result ;
	}

}
