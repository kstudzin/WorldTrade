package edu.vanderbilt.studzikm;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class Transform {

	private Map<String, Integer> input = new HashMap<>();
	private Map<String, Integer> output = new HashMap<>();
	
	public Transform(Map<String, Integer> input, Map<String, Integer> output) {
		this.input = input;
		this.output = output;
	}

	public boolean transform(Country country) {
		 if (validateInputs(country) != input.size()) {
			 return false;
		 }
		 
		 for (Entry<String, Integer> resource : input.entrySet()) {
			 country.updateResouce(resource.getKey(), resource.getValue() * -1);
		 }
		 
		 for (Entry<String, Integer> resource : output.entrySet()) {
			 country.updateResouce(resource.getKey(), resource.getValue());
		 }

		 return true;
	}

	// TODO: Add parameter to validate multiple transforms
	private int validateInputs(Country country) {
		return (int) country.getResources()
				.entrySet().stream()
				.filter(e -> input.containsKey(e.getKey().getName()) && 
						e.getValue() >= input.get(e.getKey().getName()))
				.count();
	}
}
