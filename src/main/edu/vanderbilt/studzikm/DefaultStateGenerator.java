package edu.vanderbilt.studzikm;

import java.util.HashMap;
import java.util.Map;

public class DefaultStateGenerator implements StateGenerator {

	private DefaultTransforms transforms;

	public DefaultStateGenerator(Map<String, Resource> resources) {
		this.transforms = new DefaultTransforms(resources);
	}

	public Map<World, Transform> generateStates(World initialState, Country self) {


		Map<World, Transform> result = new HashMap<>();
		for (Transform transform : transforms.ALL_TRANSFORMS) {
			Country countryCopy = new Country(self);
			transform.transform(countryCopy);
			World newState = new World(initialState);
			newState.addCountry(countryCopy);
			result.put(initialState, transform);
		}

		return result;
	}

}
