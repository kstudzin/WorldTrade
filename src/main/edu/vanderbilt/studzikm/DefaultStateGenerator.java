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
			transform.transform(self);
			initialState.addCountry(self);
			result.put(initialState, transform);
		}
		
		return result;
	}

}
