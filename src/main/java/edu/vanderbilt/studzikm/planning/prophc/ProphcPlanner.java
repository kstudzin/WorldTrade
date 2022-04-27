package edu.vanderbilt.studzikm.planning.prophc;

import edu.vanderbilt.studzikm.Action;
import edu.vanderbilt.studzikm.ActionResult;
import edu.vanderbilt.studzikm.Country;
import edu.vanderbilt.studzikm.planning.Planner;

import java.util.Collections;

public class ProphcPlanner implements Planner {

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
        planner.updateState(result.getSelf());
        planner.addAction(result.getAction());
        return planner.computeScore();
    }

    @Override
    public Planner copy() {
        return this;
    }
}
