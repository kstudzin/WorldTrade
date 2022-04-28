package edu.vanderbilt.studzikm.planning.prophc.lib;


/**
 * Represents how desirable an action is within the context of
 * the planner
 */
public interface SubTaskStatus {

    /**
     * Scores the action given the action's position in the history and
     * a scoring strategy
     *
     * @param position the actions position within the history
     * @param scoringStrategy the computation for scoring the action
     * @return a score for an action at the given position
     */
    double score(Integer position, ScoringStrategy scoringStrategy);

}
