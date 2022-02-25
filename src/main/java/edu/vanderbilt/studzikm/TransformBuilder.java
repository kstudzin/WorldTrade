package edu.vanderbilt.studzikm;

import java.util.HashMap;
import java.util.Map;

public class TransformBuilder {

	private String name;
	private double proportion;
	private Map<Resource, Integer> input = new HashMap<>();
	private Map<Resource, Integer> output = new HashMap<>();

	public TransformBuilder addInput(Resource resource, Integer requiredAmount) {
		input.put(resource, requiredAmount);
		return this;
	}

	public TransformBuilder addOutput(Resource resource, Integer requiredAmount) {
		output.put(resource, requiredAmount);
		return this;
	}

	public TransformBuilder name(String name) {
		this.name = name;
		return this;
	}

	public TransformBuilder proportion(double proportion) {
		this.proportion = proportion;
		return this;
	}

	public Transform build() {
		return new Transform(input, output, name, proportion);
	}
}
