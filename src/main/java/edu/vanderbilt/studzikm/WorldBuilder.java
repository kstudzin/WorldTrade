package edu.vanderbilt.studzikm;

import java.util.Map;
import java.util.function.Supplier;

public class WorldBuilder {

	private World world = new World();
	private Map<String, Resource> resources;

	public WorldBuilder(Map<String, Resource> resources) {
		this.resources = resources;
	}

	public WorldBuilder addCountry(String name, Supplier<QualityComputation> qualityComputationSupplier) {
		world.addCountry(new Country(name, qualityComputationSupplier.get()));
		return this;
	}

	public WorldBuilder addResource(String countryName, String resourceName, Integer ammount) {
		world.getCountry(countryName).addResource(resources.get(resourceName), ammount);
		return this;
	}

	public World build() {
		return world;
	}

}
