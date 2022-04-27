package edu.vanderbilt.studzikm;

public interface Action {

	enum Type {
		TRANSFER, TRANSFORM;
	}

	String getName();

	Type getType();

	// Enforce that implementations of this interface define
	// these methods because they are used in prioritizing
	// the frontier consistently
	int hashCode();
	boolean equals(Object obj);
}
