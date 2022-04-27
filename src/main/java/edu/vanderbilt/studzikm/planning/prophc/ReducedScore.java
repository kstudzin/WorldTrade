package edu.vanderbilt.studzikm.planning.prophc;

public class ReducedScore implements SubTaskStatus {

    private static final ReducedScore singleton = new ReducedScore();

    private ReducedScore() {}

    public static ReducedScore getInstance() {
        return singleton;
    }

    @Override
    public double score(Integer position, ScoringStrategy scoringStrategy) {
        return scoringStrategy.compute(position) / 2;
    }

    @Override
    public String toString() {
        return "reduced";
    }
}
