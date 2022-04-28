package edu.vanderbilt.studzikm.planning.prophc;

import edu.vanderbilt.studzikm.ActionResult;
import edu.vanderbilt.studzikm.Country;
import edu.vanderbilt.studzikm.planning.Planner;
import edu.vanderbilt.studzikm.planning.prophc.lib.PartialOrderPlanner;
import edu.vanderbilt.studzikm.planning.prophc.lib.SubTask;
import edu.vanderbilt.studzikm.planning.prophc.lib.SubTaskStatus;
import edu.vanderbilt.studzikm.planning.prophc.lib.Task;
import org.apache.commons.collections4.OrderedMap;
import org.apache.commons.collections4.map.ListOrderedMap;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.function.Predicate;

/**
 * Implementation of the Planner interface. Functions to connect the World
 * Trade application to the prophc library.
 */
public class ProphcPlanner implements Planner {

    private static Logger log = LogManager.getLogger(ProphcPlanner.class);

    private static final Predicate<ActionResult<?>> finalSubTaskPredicate = a -> true;
    private static final OrderedMap<Predicate<ActionResult<?>>, SubTaskStatus> finalSubTaskMap =
        new ListOrderedMap<>();
    private static final Task<Country> finalTask =
            new Task<>(c -> false);
    private static final SubTask<ActionResult<?>> finalSubTask =
            new SubTask<>(finalSubTaskMap);

    static {
        finalSubTaskMap.put(finalSubTaskPredicate, TrueStatus.getInstance());
    }

    private PartialOrderPlanner<Country, ActionResult<?>> planner;

    /**
     * Creates a PROHC Planner using a Partial Order Planner
     *
     * @param planner the partial order planner to use
     */
    public ProphcPlanner(PartialOrderPlanner<Country, ActionResult<?>> planner) {
        this.planner = planner;
        this.planner.registerFinal(finalTask, finalSubTask);
    }

    /**
     * Copy constructor
     *
     * @param prophcPlanner original planner
     */
    public ProphcPlanner(ProphcPlanner prophcPlanner) {
        this.planner = new PartialOrderPlanner<>(prophcPlanner.planner);
    }

    /**
     * Score the action based on the current state
     *
     * @param result action to score
     * @return score the action
     */
    @Override
    public Double score(ActionResult<?> result) {
        log.trace(String.format("Required electronics: %d, " +
                        "Required housing: %d, " +
                        "Current action: %s %s %s",
                result.getSelf().getTargetAmount("R22"),
                result.getSelf().getTargetAmount("R23"),
                result.getType(),
                result.getName(),
                result.getRole()));

        planner.updateState(result.getSelf());
        planner.addAction(result);
        return planner.computeScore();
    }

    /**
     * Copies the current object for use in search tree
     *
     * @return an identical copy of this planner
     */
    @Override
    public Planner copy() {
        return new ProphcPlanner(this);
    }
}
