package edu.vanderbilt.studzikm.planning.prophc;

public class TrueStatus implements SubTaskStatus {
    private static final TrueStatus singleton = new TrueStatus();

    private TrueStatus() {}

    public static TrueStatus getInstance() {
        return singleton;
    }

    @Override
    public double score(Integer position, ScoringStrategy scoringStrategy) {
        return scoringStrategy.compute(position);
    }

    @Override
    public String toString() {
        return "true";
    }
}
