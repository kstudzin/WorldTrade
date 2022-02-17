package edu.vanderbilt.studzikm;

import java.util.Collection;

public interface StateGenerator {

	Collection<?> generateStates(World initialState, Country self, int depth);

}
