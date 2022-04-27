package edu.vanderbilt.studzikm.planning.prophc;

import org.apache.commons.collections4.queue.CircularFifoQueue;

import java.util.function.Function;

public class FunctionScoringStrategy implements ScoringStrategy {

    private static final double c = 0.60793;
    private Function<Integer, Double> func = i -> c / Math.pow(i, 2);

    @Override
    public double compute(CircularFifoQueue<Boolean> history) {
        Double sum = 0.0;
        for (int i = 0; i < history.size(); i++) {
            if (history.get(i)) {
                sum += func.apply(i);
            }
        }
        return sum;
    }

}
