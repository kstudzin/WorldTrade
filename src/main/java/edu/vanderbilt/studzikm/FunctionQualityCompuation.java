package edu.vanderbilt.studzikm;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;

public class FunctionQualityCompuation implements QualityComputation {

	private BiFunction<Double, Double, Double> function;
	private Map<String, Double> targetProportion = new HashMap<>();

	public FunctionQualityCompuation() {
		this((target, actual) ->
				(actual / target) * Math.exp(-((Math.pow(actual, 2) - Math.pow(target, 2)) / (2 * Math.pow(target, 2))))
		);
	}

	public FunctionQualityCompuation(BiFunction<Double, Double, Double> function) {
		this.function = function;
		targetProportion.put("R1", 1.0);
		targetProportion.put("R2", 240.25);
		targetProportion.put("R3", 5.0/4.0);
		targetProportion.put("R21", 40.75);
		targetProportion.put("R22", 20.0);
		targetProportion.put("R23", .25);
		targetProportion.put("R21'", 0.001);
		targetProportion.put("R22'", 0.001);
		targetProportion.put("R23'", 0.001);
	}

	@Override
	public double compute(Country country) {
		Double people = country.getResource("R1") * 1.0;

		return country.getResources()
				.entrySet()
				.stream()
				.mapToDouble(e -> function.apply(targetProportion.get(e.getKey().getName()), 
						e.getValue() / people) * 
						e.getKey().getWeight())
				.sum();
	}

}
