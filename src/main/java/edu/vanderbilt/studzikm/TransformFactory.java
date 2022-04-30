package edu.vanderbilt.studzikm;

import java.util.Collection;
import java.util.stream.Stream;

/**
 * Factory interface for supplying transforms
 */
public interface TransformFactory {

	/**
	 * Gets the transforms
	 *
	 * @return a collection of transforms
	 */
	Collection<Transform> getTransforms();

	/**
	 * Streams the transforms
	 * @return a stream of the transforms
	 */
	Stream<Transform> stream();
}
