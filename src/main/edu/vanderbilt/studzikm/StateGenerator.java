package edu.vanderbilt.studzikm;

import java.util.Map;

public interface StateGenerator {

	Map<World, Transform> generateStates(World initialState, Country self);

}
