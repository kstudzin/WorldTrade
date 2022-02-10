package edu.vanderbilt.studzikm;

import java.util.Collection;

public interface StateGenerator {

	<T extends Action> Collection<ActionResult<? extends Action>> generateStates(World initialState, Country self);

}
