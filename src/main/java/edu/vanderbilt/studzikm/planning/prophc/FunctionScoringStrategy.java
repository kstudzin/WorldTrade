package edu.vanderbilt.studzikm.planning.prophc;

import edu.vanderbilt.studzikm.planning.prophc.lib.ScoringStrategy;
import edu.vanderbilt.studzikm.planning.prophc.lib.SubTaskStatus;
import org.apache.commons.collections4.queue.CircularFifoQueue;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.function.Function;

/**
 * Strategy for scoring histories of actions.
 *
 * Scores are powers of 2 so that the newest action is worth exponentially
 * more than older actions. This weights recent actions more heavily. The
 * sum of the scores is divided by the max sum of scores to get a normalized
 * score between 0 and 1.
 *
 */
public class FunctionScoringStrategy implements ScoringStrategy {

    private Logger log = LogManager.getLogger(FunctionScoringStrategy.class);

    private Function<Integer, Double> func = i -> Math.pow(i, 2);
    private Function<Integer, Integer> sumOfSquares = i ->
            (i * (i + 1) * ((2 * i) + 1)) / 6;

    /**
     * Computes the score of the given history
     *
     * @param history the history of actions in the current plan
     * @return normalized sum of scores for individual actions
     */
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

    /**
     * Apply the scoring function to a position value
     *
     * @param position position in the history
     * @return the value of the scoring function for given input
     */
    @Override
    public double compute(Integer position) {
        return func.apply(position);
    }
}
