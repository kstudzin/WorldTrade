package edu.vanderbilt.studzikm.planning.prophc;

import org.apache.commons.collections4.queue.CircularFifoQueue;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.function.Function;

public class FunctionScoringStrategy implements ScoringStrategy {

    private Logger log = LogManager.getLogger(FunctionScoringStrategy.class);

    private static final double c = 0.83188;
    private Function<Integer, Double> func = i -> c / Math.pow(i, 3);

    @Override
    public double compute(CircularFifoQueue<Boolean> history) {
        Double sum = 0.0;
        for (int i = 0; i < history.size(); i++) {
            if (history.get(i)) {
                sum += func.apply(history.size() - i);
            }
        }

        if (sum == 0.0) {
            sum = 0.2;
        }
        log.trace(String.format("History: %s (Score: %f)", history, sum));
        return sum;
    }

}
