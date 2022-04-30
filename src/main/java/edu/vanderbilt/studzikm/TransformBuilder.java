package edu.vanderbilt.studzikm;

import java.util.HashMap;
import java.util.Map;

/**
 * Builder to create transforms
 */
public class TransformBuilder {

	private String name;
	private double proportion;
	private Map<Resource, Integer> input = new HashMap<>();
	private Map<Resource, Integer> output = new HashMap<>();

	/**
	 * Adds an input resource and amount to this transform
	 * @param resource a resource that is required as input
	 * @param requiredAmount the amount of resource required as input
	 * @return this builder
	 */
	public TransformBuilder addInput(Resource resource, Integer requiredAmount) {
		input.put(resource, requiredAmount);
		return this;
	}

	/**
	 * Adds an output resource and the produced amount to this transform
	 * @param resource a resource that is produced as output
	 * @param requiredAmount the amount of resource that is produced
	 * @return this builder
	 */
	public TransformBuilder addOutput(Resource resource, Integer requiredAmount) {
		output.put(resource, requiredAmount);
		return this;
	}

	/**
	 * Sets the name of the transform which should be the name of the
	 * primary output resource
	 * @param name the name of the resource
	 * @return this builder
	 */
	public TransformBuilder name(String name) {
		this.name = name;
		return this;
	}

	/**
	 * Set the allowable transform proportions
	 *
	 * May be called multiple times to set many proportions
	 * @param proportion proportion of total transforms that could be performed
	 * @return this builder
	 */
	public TransformBuilder proportion(double proportion) {
		this.proportion = proportion;
		return this;
	}

	/**
	 * Builds the transform
	 * @return the transform
	 */
	public Transform build() {
		return new Transform(input, output, name, proportion);
	}
}
