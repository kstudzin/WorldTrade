package edu.vanderbilt.studzikm.planning.prophc;

import edu.vanderbilt.studzikm.Action;
import edu.vanderbilt.studzikm.ActionResult;
import edu.vanderbilt.studzikm.Country;
import edu.vanderbilt.studzikm.planning.Planner;

public class ProphcPlanner implements Planner {

    private PartialOrderPlanner<Country, Action> planner;
    public ProphcPlanner(PartialOrderPlanner<Country, Action> planner) {
        this.planner = planner;
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
