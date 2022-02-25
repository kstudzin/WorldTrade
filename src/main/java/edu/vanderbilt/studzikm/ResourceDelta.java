package edu.vanderbilt.studzikm;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ResourceDelta {

	private Map<String, Integer> inputs = new HashMap<>();
	private Map<String, Integer> outputs = new HashMap<>();

	public ResourceDelta addInput(Resource resource, Integer amount) {
		inputs.put(resource.getName(), amount);
		return this;
	}

	public ResourceDelta addOutput(Resource resource, Integer amount) {
		outputs.put(resource.getName(), amount);
		return this;
	}

	public Map<String, Integer> getInputs() {
		return Collections.unmodifiableMap(inputs);
	}

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
