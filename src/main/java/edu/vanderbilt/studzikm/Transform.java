package edu.vanderbilt.studzikm;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;

public class Transform implements Action {

	private String name;
	private double proportion;
	private Map<Resource, Integer> input = new HashMap<>();
	private Map<Resource, Integer> output = new HashMap<>();
	
	public Transform(
			Map<Resource, Integer> input, 
			Map<Resource, Integer> output, 
			String name,
			double proportion) {
		this.name = name;
		this.proportion = proportion;
		this.input = input;
		this.output = output;
	}

	public ResourceDelta transform(Country country) {
		int numberOperations = validateInputs(country);
		if (numberOperations == 0) {
			 return null;
		 }

		return transform(country, numberOperations);
	}

	public ResourceDelta transform(Country country, int resourceMultiplier) {

		 ResourceDelta delta = new ResourceDelta();
		 for (Entry<Resource, Integer> resource : input.entrySet()) {
			Integer newValue = country.updateResource(resource.getKey(), resource.getValue() * -resourceMultiplier);
			delta.addInput(resource.getKey(), newValue);
		 }

		 for (Entry<Resource, Integer> resource : output.entrySet()) {
			Integer newValue = country.updateResource(resource.getKey(), resource.getValue() * resourceMultiplier);
			delta.addOutput(resource.getKey(), newValue);
		 }

		 return delta;
	}

	public String getName() {
		return name;
	}

	public Double getProportion() {
		return proportion;
	}

	public Map<Resource, Integer> getInputs() {
		return Collections.unmodifiableMap(input);
	}

	public Map<Resource, Integer> getOutputs() {
		return Collections.unmodifiableMap(output);
	}

	public Type getType() {
		return Type.TRANSFORM;
	}

	@Override
	public String toString() {
		return "Transform [name=" + name + "]";
	}

	private int validateInputs(Country country) {
		return input
				.entrySet()
				.stream()
				.mapToInt(e -> (int)((country.getResource(e.getKey()) / e.getValue()) * proportion))
				.min()
				.orElse(0);
	}

	@Override
	public int hashCode() {
		return Objects.hash(input, name, output, proportion);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Transform other = (Transform) obj;
		return Objects.equals(input, other.input) && Objects.equals(name, other.name)
				&& Objects.equals(output, other.output)
				&& Double.doubleToLongBits(proportion) == Double.doubleToLongBits(other.proportion);
	}
}
