package edu.vanderbilt.studzikm;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class Transform implements Action {

	private String name;
	private Map<Resource, Integer> input = new HashMap<>();
	private Map<Resource, Integer> output = new HashMap<>();
	
	public Transform(Map<Resource, Integer> input, Map<Resource, Integer> output, String name) {
		this.name = name;
		this.input = input;
		this.output = output;
	}

	public ResourceDelta transform(Country country) {
		 if (validateInputs(country) != input.size()) {
			 return null;
		 }

		 ResourceDelta delta = new ResourceDelta();
		 for (Entry<Resource, Integer> resource : input.entrySet()) {
			 country.updateResource(resource.getKey(), resource.getValue() * -1);
			 delta.addInput(resource.getKey(), resource.getValue());
		 }

		 for (Entry<Resource, Integer> resource : output.entrySet()) {
			 country.updateResource(resource.getKey(), resource.getValue());
			 delta.addOutput(resource.getKey(), resource.getValue());
		 }

		 return delta;
	}

	@Override
	public String toString() {
		return "Transform [name=" + name + "]";
	}

	// TODO: Add parameter to validate multiple transforms
	private int validateInputs(Country country) {
		return (int) input
				.entrySet().stream()
				.filter(e -> country.getResource(e.getKey()) >= e.getValue())
				.count();
	}
}
