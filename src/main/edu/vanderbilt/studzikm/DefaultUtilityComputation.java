package edu.vanderbilt.studzikm;


public class DefaultUtilityComputation implements UtilityComputation {

	@Override
	public double compute(World world, Country country) {
		return country.getResources()
				.entrySet()
				.stream()
				.mapToDouble(e -> e.getKey().getWeight() * e.getValue())
				.sum();
	}

}
