package edu.vanderbilt.studzikm.planning;

import edu.vanderbilt.studzikm.ActionResult;

public interface Planner {
    Double score(ActionResult<?> result);
}
