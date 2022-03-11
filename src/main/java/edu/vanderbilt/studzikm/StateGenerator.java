package edu.vanderbilt.studzikm;

import java.util.stream.Stream;

public interface StateGenerator {

	Stream<?> generateStates(World initialState, Country self, int depth);

}
