package edu.vanderbilt.studzikm;

import java.util.HashMap;
import java.util.Map;

/**
 * Builder to create a map of resource names to resource objects
 */
public class ResourcesBuilder {

	private Map<String, Resource> resources = new HashMap<>();

	/**
	 * Adds a resource to the map
	 * @param name name of the resource
	 * @param weight the importance of the resource relative to other resources
	 * @return this object
	 */
	public ResourcesBuilder addResource(String name, Double weight) {
		resources.put(name, new Resource(name, weight));
		return this;
	}

	/**
	 * Builds the resource map
	 * @return the map
	 */
	public Map<String, Resource> build() {
		return resources;
	}
}
