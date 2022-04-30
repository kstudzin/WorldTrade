package edu.vanderbilt.studzikm;

import java.util.stream.Stream;

/**
 * Interface for a generator of possible next states
 */
public interface StateGenerator {

	/**
	 * Generates next states given the world state and a country performing actions
	 * @param initialState the starting world state
	 * @param self the country performing actions
	 * @param depth the depth that these actions will be with in the schedle
	 * @return a stream of action results
	 */
	Stream<?> generateStates(World initialState, Country self, int depth);

}
