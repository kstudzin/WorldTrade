package edu.vanderbilt.studzikm.planning.prophc;

import edu.vanderbilt.studzikm.Action;
import edu.vanderbilt.studzikm.ActionResult;
import edu.vanderbilt.studzikm.Country;
import edu.vanderbilt.studzikm.planning.Planner;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Collections;

public class ProphcPlanner implements Planner {

    private static Logger log = LogManager.getLogger(ProphcPlanner.class);

    private static final Task<Country> finalTask =
            new Task<>(c -> false);
    private static final SubTask<Action> finalSubTask =
            new SubTask<>(a -> true);

    private PartialOrderPlanner<Country, Action> planner;

    public ProphcPlanner(PartialOrderPlanner<Country, Action> planner) {
        this.planner = planner;
        this.planner.registerFinal(finalTask,
                Collections.singletonList(finalSubTask));
    }

    @Override
    public Double score(ActionResult<?> result) {
        log.trace(String.format("Required electronics: %d, " +
                        "Required housing: %d," +
                        "Current action: %s",
                result.getSelf().getTargetAmount("R22"),
                result.getSelf().getTargetAmount("R23"),
                result.getAction().getName()));

        planner.updateState(result.getSelf());
        planner.addAction(result.getAction());
        return planner.computeScore();
    }

    @Override
    public Planner copy() {
        return this;
    }
}
