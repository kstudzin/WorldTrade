package edu.vanderbilt.studzikm;

public interface Action {

	// Enforce that implementations of this interface define
	// these methods because they are used in prioritizing
	// the frontier consistently
	int hashCode();
	boolean equals(Object obj);
}
