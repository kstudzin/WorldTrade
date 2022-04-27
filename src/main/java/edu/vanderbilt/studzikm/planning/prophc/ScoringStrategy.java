package edu.vanderbilt.studzikm.planning.prophc;

import org.apache.commons.collections4.queue.CircularFifoQueue;

public interface ScoringStrategy {

    double compute(CircularFifoQueue<SubTaskStatus> history);

    double compute(Integer position);

}
