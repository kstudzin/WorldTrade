package edu.vanderbilt.studzikm.planning.prophc;

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
    public double compute(CircularFifoQueue<Boolean> history) {
        Double sum = 0.0;
        Integer max = sumOfSquares.apply(history.size());
        for (int i = 0; i < history.size(); i++) {
            if (history.get(i)) {
                sum += func.apply(i);
            }
        }

        Double score = Math.max(0.05, sum / max);
        log.trace(String.format("History: %s (Score: %f)", history, score));
        return score;
    }

}
