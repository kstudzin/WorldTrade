package edu.vanderbilt.studzikm.planning.prophc;

import edu.vanderbilt.studzikm.planning.prophc.lib.ScoringStrategy;
import edu.vanderbilt.studzikm.planning.prophc.lib.SubTaskStatus;

/**
 * Represents a single history value for an action that is not desirable.
 */
public class FalseStatus implements SubTaskStatus {

    private static final FalseStatus singleton = new FalseStatus();

    private FalseStatus() {}

    /**
     * Factory method for getting an instance of this status
     *
     * @return an instance of the false status
     */
    public static FalseStatus getInstance() {
        return singleton;
    }

    /**
     * The score for a false status is a fraction of smallest possible
     * score for any status.
     *
     * Why not just return zero? Some good schedules start with negative
     * expected utilities. A score of zero for an undesirable action
     * leads to a 0 expected utility which scores higher than better
     * action histories.
     *
     * @param position the actions position within the history
     * @param scoringStrategy the computation for scoring the action
     * @return score for an undesirable action
     */
    @Override
    public double score(Integer position, ScoringStrategy scoringStrategy) {
        return scoringStrategy.compute(1) / 4;
    }

    @Override
    public String toString() {
        return "false";
    }
}
