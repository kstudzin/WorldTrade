package edu.vanderbilt.studzikm.planning.prophc;

import edu.vanderbilt.studzikm.planning.prophc.lib.ScoringStrategy;
import edu.vanderbilt.studzikm.planning.prophc.lib.SubTaskStatus;

/**
 * Represents a single history value for an action that is desirable.
 */
public class TrueStatus implements SubTaskStatus {
    private static final TrueStatus singleton = new TrueStatus();

    private TrueStatus() {}

    /**
     * Factory method for getting an instance of this status
     *
     * @return an instance of the true status
     */
    public static TrueStatus getInstance() {
        return singleton;
    }

    /**
     * The score for a true status is based on the position of the
     * status in the history
     *
     * @param position the actions position within the history
     * @param scoringStrategy the computation for scoring the action
     * @return score for a desirable action
     */
    @Override
    public double score(Integer position, ScoringStrategy scoringStrategy) {
        return scoringStrategy.compute(position);
    }

    @Override
    public String toString() {
        return "true";
    }
}
