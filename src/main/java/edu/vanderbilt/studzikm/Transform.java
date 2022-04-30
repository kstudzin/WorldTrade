package edu.vanderbilt.studzikm;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;

/**
 * Represents a transform action that a country can perform.
 *
 * A transform is when a country transforms some of its resources
 * into other resources according to a transfer template.
 *
 * In general, there is one output that is a goal output and other
 * outputs are waste products or resources that are not consumed
 *
 * This object allows the same transform to be performed multiple times
 * at once. You can specify how many times to apply the transform in terms
 * of a percent of the total amount that could be produced. For example,
 * suppose you have enough inputs to create 4 of resource1. If you set the
 * proportion to 50%, the transform will create 2 resource1's.
 */
public class Transform implements Action {

	private String name;
	private double proportion;
	private Map<Resource, Integer> input = new HashMap<>();
	private Map<Resource, Integer> output = new HashMap<>();

	/**
	 * Creates a transform action.
	 *
	 * @param input the input resources and amounts of the transform
	 * @param output the output resources and amounts of the transform
	 * @param name the name of the transform, i.e., the name of the primary resource output
	 * @param proportion the percent of the total possible production that could be produced
	 */
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

	/**
	 * Performs the transform on the given country
	 * @param country the country to perform the transform on
	 * @return amount of change of all input and output resources
	 */
	public ResourceDelta transform(Country country) {
		int numberOperations = validateInputs(country);
		if (numberOperations == 0) {
			 return null;
		 }

		return transform(country, numberOperations);
	}

	/**
	 * Performs the transform on the given country
	 * @param country the country to perform the transform on
	 * @param resourceMultiplier the number of operations to perform
	 * @return amount of change of all input and output resources
	 */
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

	/**
	 * Gets the name of this resource
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Gets the proportion of this resource
	 * @return
	 */
	public Double getProportion() {
		return proportion;
	}

	/**
	 * Gets the inputs and the amount required
	 * @return a map of input resource to amount of input resource required
	 */
	public Map<Resource, Integer> getInputs() {
		return Collections.unmodifiableMap(input);
	}

	/**
	 * Gets the outputs and the amount produced
	 * @return a map of the output resources to amount of outputs produced
	 */
	public Map<Resource, Integer> getOutputs() {
		return Collections.unmodifiableMap(output);
	}

	/**
	 * Gets the type of action
	 * @return TRANSFORM
	 */
	public Type getType() {
		return Type.TRANSFORM;
	}

	@Override
	public String toString() {
		return "Transform [name=" + name + "]";
	}

	/**
	 * Determines the number transforms that can be executed
	 * @param country the country performing the transforms
	 * @return the number of transfoers
	 */
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
