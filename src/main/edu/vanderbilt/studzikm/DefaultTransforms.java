package edu.vanderbilt.studzikm;

import java.util.Map;

public class DefaultTransforms {

	public final Transform HOUSING_TRANSFORM;
	public final Transform ALLOYS_TRANSFORM;
	public final Transform ELECTRONICS_TRANSFORM;
	
	DefaultTransforms(Map<String, Resource> resources) {
		HOUSING_TRANSFORM = new TransformBuilder()
				.addInput(resources.get("R1"), 5)
				.addInput(resources.get("R2"), 1)
				.addInput(resources.get("R3"), 5)
				.addInput(resources.get("R21"), 3)
				.addOutput(resources.get("R23"), 1)
				.addOutput(resources.get("R23'"), 1)
				.addOutput(resources.get("R1"), 5)
				.build();
		
		ALLOYS_TRANSFORM = new TransformBuilder()
				.addInput(resources.get("R1"), 1)
				.addInput(resources.get("R2"), 2)
				.addOutput(resources.get("R1"), 1)
				.addOutput(resources.get("R21"), 1)
				.addOutput(resources.get("R21'"), 1)
				.build();
		
		ELECTRONICS_TRANSFORM = new TransformBuilder()
				.addInput(resources.get("R1"), 1)
				.addInput(resources.get("R2"), 3)
				.addInput(resources.get("R21"), 2)
				.addOutput(resources.get("R1"), 1)
				.addOutput(resources.get("R22"), 2)
				.addOutput(resources.get("R22'"), 1)
				.build();
	}
}
