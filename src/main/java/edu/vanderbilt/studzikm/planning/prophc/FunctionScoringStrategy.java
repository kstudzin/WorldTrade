package edu.vanderbilt.studzikm.planning.prophc;

import edu.vanderbilt.studzikm.planning.prophc.lib.ScoringStrategy;
import edu.vanderbilt.studzikm.planning.prophc.lib.SubTaskStatus;
import org.apache.commons.collections4.queue.CircularFifoQueue;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.function.Function;

public class FunctionScoringStrategy implements ScoringStrategy {

    private Logger log = LogManager.getLogger(FunctionScoringStrategy.class);

    private Function<Integer, Double> func = i -> Math.pow(i, 2);
    private Function<Integer, Integer> sumOfSquares = i ->
            (i * (i + 1) * ((2 * i) + 1)) / 6;

    @Override
    public double compute(CircularFifoQueue<SubTaskStatus> history) {
        Double sum = 0.0;
        Integer max = sumOfSquares.apply(history.size());
        for (int i = 0; i < history.size(); i++) {
                sum += history.get(i).score(i + 1, this);
        }

        // Don't return 0. It may lead to utilities of 0 that will score
        // higher than negative scores.
        Double score = Math.max(1/(max * 8), sum / max);
        log.trace(String.format("History: %s (Score: %f)", history, score));
        return score;
    }

    public double compute(Integer position) {
        return func.apply(position);
    }
}
