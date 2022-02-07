package edu.vanderbilt.studzikm;

import java.util.HashMap;
import java.util.Map;

public class TransformBuilder {

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
	
	public Transform build() {
		return new Transform(input, output);
	}
}
