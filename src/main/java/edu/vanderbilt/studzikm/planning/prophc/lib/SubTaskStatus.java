package edu.vanderbilt.studzikm.planning.prophc.lib;

import edu.vanderbilt.studzikm.planning.prophc.lib.ScoringStrategy;

public interface SubTaskStatus {

    double score(Integer position, ScoringStrategy scoringStrategy);

}
