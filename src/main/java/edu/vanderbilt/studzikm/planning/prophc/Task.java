package edu.vanderbilt.studzikm.planning.prophc;

import java.util.function.Predicate;

public class Task<T> {

    private Predicate<T> completionTest;

    public Task(Predicate<T> completionTest) {
        this.completionTest = completionTest;
    }

    public boolean isComplete(T state) {
        return completionTest.test(state);
    }
}
