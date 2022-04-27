package edu.vanderbilt.studzikm.planning.prophc;

import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;

public class SubTask<T> {

    private Set<Map.Entry<Predicate<T>, SubTaskStatus>> getStatus;

    public SubTask(Map<Predicate<T>, SubTaskStatus> getStatus) {
        this.getStatus = getStatus.entrySet();
    }

    public SubTaskStatus getStatus(T action) {
        for (Map.Entry<Predicate<T>, SubTaskStatus> entry : getStatus) {
            if (entry.getKey().test(action)) {
                return entry.getValue();
            }
        }

        return FalseStatus.getInstance();
    }
}
