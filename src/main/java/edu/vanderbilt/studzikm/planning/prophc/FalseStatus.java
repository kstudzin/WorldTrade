package edu.vanderbilt.studzikm.planning.prophc;

import edu.vanderbilt.studzikm.planning.prophc.lib.ScoringStrategy;
import edu.vanderbilt.studzikm.planning.prophc.lib.SubTaskStatus;

public class FalseStatus implements SubTaskStatus {

    private static final FalseStatus singleton = new FalseStatus();

    private FalseStatus() {}

    public static FalseStatus getInstance() {
        return singleton;
    }

    @Override
    public double score(Integer position, ScoringStrategy scoringStrategy) {
        return scoringStrategy.compute(1) / 4;
    }

    @Override
    public String toString() {
        return "false";
    }
}
