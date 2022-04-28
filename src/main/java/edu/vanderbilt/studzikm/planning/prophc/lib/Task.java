package edu.vanderbilt.studzikm.planning.prophc.lib;

import java.util.function.Predicate;

/**
 * Represents a high-level task whose subtasks are being planned by the planner
 * @param <T> the type representing the state of the world
 */
public class Task<T> {

    private Predicate<T> completionTest;

    /**
     * Creates a task given a predicate that determines when a task is
     * complete
     *
     * @param completionTest predicate that returns true when a task is
     *                       complete given a world state
     */
    public Task(Predicate<T> completionTest) {
        this.completionTest = completionTest;
    }

    /**
     * Executes the predicate to determine if the task is complete
     *
     * @param state the world state in which the task may or may not be complete
     * @return true if the task is complete in the given state
     */
    public boolean isComplete(T state) {
        return completionTest.test(state);
    }
}
