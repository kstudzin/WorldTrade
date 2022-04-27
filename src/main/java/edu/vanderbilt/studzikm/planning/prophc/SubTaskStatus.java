package edu.vanderbilt.studzikm.planning.prophc;

public interface SubTaskStatus {

    double score(Integer position, ScoringStrategy scoringStrategy);

}
