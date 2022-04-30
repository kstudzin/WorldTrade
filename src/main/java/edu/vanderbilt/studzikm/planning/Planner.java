package edu.vanderbilt.studzikm.planning;

import edu.vanderbilt.studzikm.ActionResult;

/**
 * Interface for planners to score action results
 */
public interface Planner {

    /**
     * Score of this action result with respect to an overall plan of actions
     * @param result the action result to score as the next element in the schedule
     * @return the numeric score
     */
    Double score(ActionResult<?> result);

    /**
     * Copies this planner
     *
     * Used by the search tree
     *
     * @return another instance of the same planner
     */
    Planner copy();
}
