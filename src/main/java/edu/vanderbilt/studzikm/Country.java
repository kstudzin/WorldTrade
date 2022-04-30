package edu.vanderbilt.studzikm;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Represents a country in the world state
 */
public class Country {

	private String name;
	private QualityComputation qualityComputation;
	private Map<Resource, Integer> resources = new HashMap<>();
	private RewardComputation rewardComputation;
	private TargetResourceAmountComputation targetComputation;
	private Map<String, Integer> targetAmounts = new HashMap<>();

	/**
	 * Creates a country
	 * @param name name of the country
	 * @param qualityComputation computes the quality of the country based on resources
	 * @param resourceAmountComputation computes the target amount of each resource to meet countries needs
	 */
	public Country(String name,
				   QualityComputation qualityComputation,
				   TargetResourceAmountComputation resourceAmountComputation) {
		this.name = name;
		this.qualityComputation = qualityComputation;
		this.targetComputation = resourceAmountComputation;
	}

	/**
	 * Copy constructor. Used by search tree
	 * @param copy the original
	 */
	public Country(Country copy) {
		this.name = copy.name;
		this.qualityComputation = copy.qualityComputation;
		this.resources.putAll(copy.resources);
		this.rewardComputation = copy.rewardComputation;
		this.targetComputation =  copy.targetComputation;
	}

	/**
	 * Gets the name of the country
	 * @return the name of the country
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the name of the country
	 * @param name the name of the country
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Gets the country's resources
	 * @return a map of the country's resources to the amount of each resource
	 */
	public Map<Resource, Integer> getResources() {
		return Collections.unmodifiableMap(resources);
	}

	/**
	 * Gets the amount of a given resource
	 * @param resource the resource amount to get
	 * @return the amount of the resource the country owns
	 */
	public Integer getResource(Resource resource) {
		return resources.getOrDefault(resource, 0);
	}

	/**
	 * Gests the amount of a given resource
	 * @param resource the name of the resource to get the amount of
	 * @return the amount of the resource the country owns
	 */
	public Integer getResource(String resource) {
		return resources.entrySet()
				.stream()
				.filter(curr -> curr.getKey().getName().equals(resource))
				.map(curr -> curr.getValue())
				.findFirst()
				.orElse(0);
	}

	/**
	 * Adds a new resource to the country's resources
	 * @param resource the resource to add
	 * @param amount the amount of the resource to add
	 */
	public void addResource(Resource resource, Integer amount) {
		resources.put(resource, amount);
	}

	/**
	 * Updates the amount of a resource the country owns
	 *
	 * @param resource the resource to update
	 * @param delta a number, positive or negative, indicating the amound of the resource to add or subtract, respectively
	 * @return the updated amount of the resource
	 */
	public Integer updateResource(Resource resource, Integer delta) {
		return resources.compute(resource, (key, val) -> val == null ? delta : val + delta);
	}

	/**
	 * Updates teh amount of a resource the country owns
	 * @param resourceName the name of the resource to update
	 * @param delta a number, positive or negative, indicating the amound of the resource to add or subtract, respectively
	 */
	public void updateResource(String resourceName, Integer delta) {
		Resource resource = resources.keySet()
		.stream()
		.filter(k -> k.getName() == resourceName)
		.findFirst()
		.orElse(null);
		
		updateResource(resource, delta);
	}

	/**
	 * Computes the quality of this country state based on it's available resources
	 * @return a quality score
	 */
	public double computeQuality() {
		// TODO lazy load this value
		return qualityComputation.compute(this);
	}

	/**
	 * Compute the reward of an action on this country
	 * @param result the action that may provide a reward
	 * @return the reward of the given action
	 */
	public double computeReward(ActionResult<?> result) {
		// TODO lazy load this value
		return rewardComputation.computeReward(result, () -> this);
	}

	/**
	 * Set the strategy to compute the reward
	 * @param computation the computation
	 */
	public void setRewardComputation(RewardComputation computation) {
		this.rewardComputation = computation;
	}

	/**
	 * Gets the amount of a resource required by a countries population
	 * @param resource the name of the resource
	 * @return the amount required for optimal quality
	 */
	public Integer getTargetAmount(String resource) {
		// Lazy load target amounts
		if (targetAmounts.isEmpty()) {
			targetAmounts = targetComputation.compute(this);
		}

		return targetAmounts.get(resource);
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
