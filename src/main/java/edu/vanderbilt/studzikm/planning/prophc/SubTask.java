package edu.vanderbilt.studzikm.planning.prophc;

import java.util.function.Predicate;

public class SubTask<T> {

    private Predicate<T> isSubTask;

    public SubTask(Predicate<T> isSubTask) {
        this.isSubTask = isSubTask;
    }

    public boolean isSubTask(T action) {
        return isSubTask.test(action);
    }
}
