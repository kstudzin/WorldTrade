package edu.vanderbilt.studzikm;

import java.util.Collection;

public interface StateGenerator {

	Collection<ActionResult> generateStates(World initialState, Country self);

}
