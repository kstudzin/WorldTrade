package edu.vanderbilt.studzikm.planning.prophc;

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
