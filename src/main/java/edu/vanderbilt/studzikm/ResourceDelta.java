package edu.vanderbilt.studzikm;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

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
}
