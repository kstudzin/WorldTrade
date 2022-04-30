package edu.vanderbilt.studzikm;

/**
 * Interface for different types of actions
 */
public interface Action {

	/**
	 * Available types of actions
	 */
	enum Type {
		TRANSFER, TRANSFORM, NULL;
	}

	/**
	 * Gets the name of the primary output resource of this action
	 *
	 * @return resource name
	 */
	String getName();

	/**
	 * Gets the type of this action
	 *
	 * @return
	 */
	Type getType();

	// Enforce that implementations of this interface define
	// these methods because they are used in prioritizing
	// the frontier consistently
	int hashCode();

	boolean equals(Object obj);
}
