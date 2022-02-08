package edu.vanderbilt.studzikm;

import java.util.Map;

public interface StateGenerator {

	Map<World, Action> generateStates(World initialState, Country self);

}
