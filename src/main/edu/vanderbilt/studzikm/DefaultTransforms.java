package edu.vanderbilt.studzikm;

public class DefaultTransforms {

	public static final Transform HOUSING_TRANSFORM;
	public static final Transform ALLOYS_TRANSFORM;
	public static final Transform ELECTRONICS_TRANSFORM;
	
	static {
		HOUSING_TRANSFORM = new TransformBuilder()
				.addInput("population", 5)
				.addInput("metallic_elements", 1)
				.addInput("timber", 5)
				.addInput("metallic_alloys", 3)
				.addOutput("housing", 1)
				.addOutput("housing_waste", 1)
				.addOutput("population", 5)
				.build();
		
		ALLOYS_TRANSFORM = new TransformBuilder()
				.addInput("population", 1)
				.addInput("metallic_elements", 2)
				.addOutput("population", 1)
				.addOutput("metallic_alloys", 1)
				.addOutput("metallic_alloys_waste", 1)
				.build();
		
		ELECTRONICS_TRANSFORM = new TransformBuilder()
				.addInput("population", 1)
				.addInput("metallic_elements", 3)
				.addInput("metallic_alloys", 2)
				.addOutput("population", 1)
				.addOutput("electronics", 2)
				.addOutput("electronics_waste", 1)
				.build();
	}
}
