package edu.vanderbilt.studzikm.planning.prophc.lib;

import edu.vanderbilt.studzikm.planning.prophc.FalseStatus;
import org.apache.commons.collections4.OrderedMap;

import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;

/**
 * Represents the subtasks. Contains predicates that determine whether
 * a given action is desirable via a status
 * @param <T> type representing an action
 */
public class SubTask<T> {

    private Set<Map.Entry<Predicate<T>, SubTaskStatus>> getStatus;

    /**
     * Creates a new subtask with a map of predicates to the status
     * indicating if a given action is desirable. This map should be
     * in order such that the correct status is the first predicate
     * to be true maps to the correct status
     * @param getStatus
     */
    public SubTask(OrderedMap<Predicate<T>, SubTaskStatus> getStatus) {
        this.getStatus = getStatus.entrySet();
    }

    /**
     * Determines the subtask status for an action
     *
     * @param action the action being considered as part of a task
     * @return
     */
    public SubTaskStatus getStatus(T action) {
        for (Map.Entry<Predicate<T>, SubTaskStatus> entry : getStatus) {
            if (entry.getKey().test(action)) {
                return entry.getValue();
            }
        }

        return FalseStatus.getInstance();
    }
}
