package edu.vanderbilt.studzikm;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class Country {

	private String name;
	private QualityComputation qualityComputation;
	private Map<Resource, Integer> resources = new HashMap<>();
	private RewardComputation rewardComputation;

	public Country(String name, QualityComputation utilityComp) {
		this.name = name;
		this.qualityComputation = utilityComp;
	}

	public Country(Country copy) {
		this.name = copy.name;
		this.qualityComputation = copy.qualityComputation;
		this.resources.putAll(copy.resources);
		this.rewardComputation = copy.rewardComputation;
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

	public Integer getResource(String resource) {
		return resources.entrySet()
				.stream()
				.filter(curr -> curr.getKey().getName().equals(resource))
				.map(curr -> curr.getValue())
				.findFirst()
				.orElse(0);
	}

	public void addResource(Resource resource, Integer amount) {
		resources.put(resource, amount);
	}

	public Integer updateResource(Resource resource, Integer delta) {
		return resources.compute(resource, (key, val) -> val == null ? delta : val + delta);
	}

	public void updateResource(String resourceName, Integer delta) {
		Resource resource = resources.keySet()
		.stream()
		.filter(k -> k.getName() == resourceName)
		.findFirst()
		.orElse(null);
		
		updateResource(resource, delta);
	}

	public double computeQuality() {
		return qualityComputation.compute(this);
	}

	public double computeReward(ActionResult<?> result) {
		return rewardComputation.computeReward(result, () -> this);
	}

	public void setRewardComputation(RewardComputation computation) {
		this.rewardComputation = computation;
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
