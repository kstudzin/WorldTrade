package edu.vanderbilt.studzikm;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class Transform {

	private Map<Resource, Integer> input = new HashMap<>();
	private Map<Resource, Integer> output = new HashMap<>();
	
	public Transform(Map<Resource, Integer> input, Map<Resource, Integer> output) {
		this.input = input;
		this.output = output;
	}

	public boolean transform(Country country) {
		 if (validateInputs(country) != input.size()) {
			 return false;
		 }
		 
		 for (Entry<Resource, Integer> resource : input.entrySet()) {
			 country.updateResouce(resource.getKey(), resource.getValue() * -1);
		 }
		 
		 for (Entry<Resource, Integer> resource : output.entrySet()) {
			 country.updateResouce(resource.getKey(), resource.getValue());
		 }

		 return true;
	}

	// TODO: Add parameter to validate multiple transforms
	private int validateInputs(Country country) {
		return (int) input
				.entrySet().stream()
				.filter(e -> country.getResource(e.getKey()) >= e.getValue())
				.count();
	}
}
