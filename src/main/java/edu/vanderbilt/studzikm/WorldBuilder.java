package edu.vanderbilt.studzikm;

import com.microsoft.z3.Context;

import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class WorldBuilder {

	private World world = new World();
	private Map<String, Resource> resources;
	private DefaultTransformFactory transformFactory;
	private double gamma;

	public WorldBuilder(Map<String, Resource> resources) {
		this.resources = resources;
		this.transformFactory = new DefaultTransformFactory(resources);
	}

	public WorldBuilder addCountry(String name, Supplier<QualityComputation> qualityComputationSupplier) {
		world.addCountry(new Country(name,
				qualityComputationSupplier.get(),
				new TargetResourceAmountComputation(transformFactory, new Context())));
		return this;
	}

	public WorldBuilder addResource(String countryName, String resourceName, Integer amount) {
		world.getCountry(countryName).addResource(resources.get(resourceName), amount);
		return this;
	}

	public WorldBuilder setGamma(double gamma) {
		this.gamma = gamma;
		return this;
	}

	public World build() {
		Map<String, Double> initialQualities = world.stream()
				.collect(Collectors.toMap(Country::getName, Country::computeQuality));
		RewardComputation rewardComputation = new DiscountedRewardComputation(gamma, initialQualities);
		world.forEach(c -> c.setRewardComputation(rewardComputation));
		return world;
	}

}
