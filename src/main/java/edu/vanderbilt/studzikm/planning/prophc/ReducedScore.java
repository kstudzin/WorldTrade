package edu.vanderbilt.studzikm.planning.prophc;

import edu.vanderbilt.studzikm.planning.prophc.lib.ScoringStrategy;
import edu.vanderbilt.studzikm.planning.prophc.lib.SubTaskStatus;

/**
 * Represents a single history value for an action that is neutral. The
 * action should be neither incentivized or disincentivized.
 */
public class ReducedScore implements SubTaskStatus {

    private static final ReducedScore singleton = new ReducedScore();

    private ReducedScore() {}

    /**
     * Factory method for getting an instance of this status
     *
     * @return an instance of the false status
     */
    public static ReducedScore getInstance() {
        return singleton;
    }

    /**
     * The score for a reduced score status is a fraction of score
     * based on position
     *
     * @param position the actions position within the history
     * @param scoringStrategy the computation for scoring the action
     * @return scores for neutral actions
     */
    @Override
    public double score(Integer position, ScoringStrategy scoringStrategy) {
        return scoringStrategy.compute(position) / 2;
    }

    @Override
    public String toString() {
        return "reduced";
    }
}
