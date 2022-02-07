package edu.vanderbilt.studzikm;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class Country {

	private String name;
	private Map<Resource, Integer> resources = new HashMap<>();
	
	public Country(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Map<Resource, Integer> getResources() {
		return Collections.unmodifiableMap(resources);
	}

	public Integer getResource(Resource resource) {
		return resources.get(resource);
	}

	public void addResource(Resource resource, Integer amount) {
		resources.put(resource, amount);
	}

	public void updateResouce(Resource resource, Integer delta) {
		resources.computeIfPresent(resource, (key, val) -> val + delta);
	}
	
	public void updateResouce(String resourceName, Integer delta) {
		Resource resource = resources.keySet()
		.stream()
		.filter(k -> k.getName() == resourceName)
		.findFirst()
		.orElse(null);
		
		updateResouce(resource, delta);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((resources == null) ? 0 : resources.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Country other = (Country) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (resources == null) {
			if (other.resources != null)
				return false;
		} else if (!resources.equals(other.resources))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Country [name=" + name + ", resources=" + resources + "]";
	}

}
