package edu.vanderbilt.studzikm;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class Country {

	private String name;
	private UtilityComputation utilComp;
	private Map<Resource, Integer> resources = new HashMap<>();

	public Country(String name, UtilityComputation utilityComp) {
		this.name = name;
		this.utilComp = utilityComp;
	}

	public Country(Country copy) {
		this.name = copy.name;
		this.utilComp = copy.utilComp;
		this.resources.putAll(copy.resources);
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
		return resources.getOrDefault(resource, 0);
	}

	public void addResource(Resource resource, Integer amount) {
		resources.put(resource, amount);
	}

	public void updateResource(Resource resource, Integer delta) {
		resources.compute(resource, (key, val) -> val == null ? delta : val + delta);
	}

	public void updateResource(String resourceName, Integer delta) {
		Resource resource = resources.keySet()
		.stream()
		.filter(k -> k.getName() == resourceName)
		.findFirst()
		.orElse(null);
		
		updateResource(resource, delta);
	}

	public double computeUtility(World world) {
		return utilComp.compute(world, this);
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