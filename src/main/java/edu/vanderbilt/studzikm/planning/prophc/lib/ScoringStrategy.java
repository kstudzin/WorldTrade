package edu.vanderbilt.studzikm.planning.prophc.lib;

import org.apache.commons.collections4.queue.CircularFifoQueue;

/**
 * Provides computation for calculating score of a history
 * of actions. The scores this method calculates should be
 * an indication of how good the plan is.
 */
public interface ScoringStrategy {

    /**
     * Provides a strategy for how to combine scores for individual
     * actions into a comprehensive score for the history
     *
     * @param history the history of actions in the current plan
     * @return a score indicating the quality of the plan
     */
    double compute(CircularFifoQueue<SubTaskStatus> history);

    /**
     * Provides a strategy for how to score a value at a particular
     * position in the history.
     *
     * @param position position in the history
     * @return a score
     */
    double compute(Integer position);

}
