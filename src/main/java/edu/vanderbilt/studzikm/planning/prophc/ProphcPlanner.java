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

    public ProphcPlanner(PartialOrderPlanner<Country, ActionResult<?>> planner) {
        this.planner = planner;
        this.planner.registerFinal(finalTask, finalSubTask);
    }

    public ProphcPlanner(ProphcPlanner prophcPlanner) {
        this.planner = new PartialOrderPlanner<>(prophcPlanner.planner);
    }

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

    @Override
    public Planner copy() {
        return new ProphcPlanner(this);
    }
}
