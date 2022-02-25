package edu.vanderbilt.studzikm;

import java.util.Collection;
import java.util.stream.Stream;

public interface TransformFactory {

	Collection<Transform> getTransforms();

	Stream<Transform> stream();
}
