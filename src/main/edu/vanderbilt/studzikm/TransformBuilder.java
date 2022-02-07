package edu.vanderbilt.studzikm;

import java.util.HashMap;
import java.util.Map;

public class TransformBuilder {

	private Map<String, Integer> input = new HashMap<>();
	private Map<String, Integer> output = new HashMap<>();

	public TransformBuilder addInput(String resourceName, Integer requiredAmount) {
		input.put(resourceName, requiredAmount);
		return this;
	}
	
	public TransformBuilder addOutput(String resourcename, Integer requiredAmount) {
		output.put(resourcename, requiredAmount);
		return this;
	}
	
	public Transform build() {
		return new Transform(input, output);
	}
}
