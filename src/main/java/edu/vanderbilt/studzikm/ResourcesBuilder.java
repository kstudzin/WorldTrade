package edu.vanderbilt.studzikm;

import java.util.HashMap;
import java.util.Map;

public class ResourcesBuilder {

	private Map<String, Resource> resources = new HashMap<>();

	public ResourcesBuilder addResource(String name, Double weight) {
		resources.put(name, new Resource(name, weight));
		return this;
	}

	public Map<String, Resource> build() {
		return resources;
	}
}
