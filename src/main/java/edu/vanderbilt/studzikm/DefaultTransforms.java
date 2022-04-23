package edu.vanderbilt.studzikm;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DefaultTransforms implements TransformFactory {

	public static final String HOUSING = "R23";
	public static final String ALLOYS = "R21";
	public static final String ELECTRONICS = "R22";

	private static final Double[] defaultProportions = {0.33, 0.66};

	private final Collection<Transform> transforms;

	public DefaultTransforms(Map<String, Resource> resources) {
		this(resources, defaultProportions);
	}

	public DefaultTransforms(Map<String, Resource> resources, Double[] proportions) {
		transforms = Stream.of(proportions)
		.flatMap(p -> createTransforms(resources, p))
		.collect(Collectors.toSet());
	}

	private Stream<Transform> createTransforms(Map<String, Resource> resources, Double proportion) {
		return Stream.of(

				new TransformBuilder()
				.name(HOUSING)
				.proportion(proportion)
				.addInput(resources.get("R1"), 5)
				.addInput(resources.get("R2"), 1)
				.addInput(resources.get("R3"), 5)
				.addInput(resources.get("R21"), 3)
				.addOutput(resources.get("R23"), 1)
				.addOutput(resources.get("R23'"), 1)
				.addOutput(resources.get("R1"), 5)
				.build(),

				new TransformBuilder()
				.name(ALLOYS)
				.proportion(proportion)
				.addInput(resources.get("R1"), 1)
				.addInput(resources.get("R2"), 2)
				.addOutput(resources.get("R1"), 1)
				.addOutput(resources.get("R21"), 1)
				.addOutput(resources.get("R21'"), 1)
				.build(),

				new TransformBuilder()
				.name(ELECTRONICS)
				.proportion(proportion)
				.addInput(resources.get("R1"), 1)
				.addInput(resources.get("R2"), 3)
				.addInput(resources.get("R21"), 2)
				.addOutput(resources.get("R1"), 1)
				.addOutput(resources.get("R22"), 2)
				.addOutput(resources.get("R22'"), 1)
				.build()

				);
	}

	public Collection<Transform> getTransforms() {
		return Collections.unmodifiableCollection(transforms);
	}

	public Stream<Transform> stream() {
		return transforms.stream();
	}
}
