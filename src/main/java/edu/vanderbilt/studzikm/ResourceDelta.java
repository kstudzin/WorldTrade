package edu.vanderbilt.studzikm;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Represents the change in resources that a country possess caused by performing an action
 */
public class ResourceDelta {

	private Map<String, Integer> inputs = new HashMap<>();
	private Map<String, Integer> outputs = new HashMap<>();

	/**
	 * Adds a resource to the list of inputs.
	 * @param resource name of the resource
	 * @param amount the amount the country's resource decreases by
	 * @return this delta object
	 */
	public ResourceDelta addInput(Resource resource, Integer amount) {
		inputs.put(resource.getName(), amount);
		return this;
	}

	/**
	 * Adds a resource to the list of outputs
	 * @param resource name of the resource
	 * @param amount the amount the country's reource increases by
	 * @return
	 */
	public ResourceDelta addOutput(Resource resource, Integer amount) {
		outputs.put(resource.getName(), amount);
		return this;
	}

	/**
	 * Gets the inputs
	 * @return a map of resource names to input amount requirements
	 */
	public Map<String, Integer> getInputs() {
		return Collections.unmodifiableMap(inputs);
	}

	/**
	 * Gets the outputs
	 * @return a map of the resource names to the output amount
	 */
	public Map<String, Integer> getOutputs() {
		return Collections.unmodifiableMap(outputs);
	}

	@Override
	public int hashCode() {
		return Objects.hash(inputs, outputs);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ResourceDelta other = (ResourceDelta) obj;
		return Objects.equals(inputs, other.inputs) && Objects.equals(outputs, other.outputs);
	}
}
