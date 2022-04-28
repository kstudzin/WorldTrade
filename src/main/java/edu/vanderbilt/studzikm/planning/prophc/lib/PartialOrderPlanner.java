package edu.vanderbilt.studzikm.planning.prophc.lib;

import org.apache.commons.collections4.OrderedMap;
import org.apache.commons.collections4.map.LinkedMap;
import org.apache.commons.collections4.queue.CircularFifoQueue;


/**
 * The partial order planner is the class main class used for planning
 *
 * @param <T> the type representing the state of world
 * @param <R> the type representing actions that can transform the state of the world
 */
public class PartialOrderPlanner<T, R> {

    // The order of the map represents the order in which tasks should be completed
    private OrderedMap<Task<T>, SubTask<R>> subTaskMp = new LinkedMap<>();
    private CircularFifoQueue<SubTaskStatus> history;
    private Task<T> currentTask;
    private ScoringStrategy scoringStrategy;
    private int historyLength;

    /**
     * Creates a partial order planner
     *
     * @param historyLength number of history records to include in planning scores
     * @param scoringStrategy functions used to calculate scores
     */
    public PartialOrderPlanner(int historyLength,
                               ScoringStrategy scoringStrategy) {
        this.historyLength = historyLength;
        this.history = new CircularFifoQueue<>(historyLength);
        this.scoringStrategy = scoringStrategy;
    }

    /**
     * Copies a partial order planner.
     *
     * This is necessary because of the tree structure of searching for possible plans.
     *
     * @param original the planner to copy
     */
    public PartialOrderPlanner(PartialOrderPlanner original) {
        this.historyLength = original.historyLength;
        this.history = new CircularFifoQueue<>(historyLength);
        this.history.addAll(original.history);
        this.scoringStrategy = original.scoringStrategy;
        this.subTaskMp = new LinkedMap<>();
        this.subTaskMp.putAll(original.subTaskMp);
        this.currentTask = original.currentTask;
    }

    /**
     * Registers a task and a subtask. The planner attempts to complete tasks in the same order
     * they are registers; therefore, it is important to register the tasks in the correct order.
     * A task should only be registered once.
     *
     * @param task the task to register
     * @param subTasks the subtasks of the task
     */
    public void register(Task<T> task, SubTask<R> subTasks) {
        subTaskMp.put(task, subTasks);
        if (currentTask == null) {
            currentTask = subTaskMp.firstKey();
        }
    }

    /**
     * Used so the planner can continue to evaluate plans after all goals have been
     * achieved. In this case, it is very likely that the {@code finalTask} will always
     * return false and the {@code finalSubTask} will always return true
     * @param finalTask
     * @param finalSubTask
     */
    public void registerFinal(Task<T> finalTask, SubTask<R> finalSubTask) {
        register(finalTask, finalSubTask);
    }

    /**
     * Updates the state of the world. This means checking if the current task is complete
     * and finding the next task to work on.
     *
     * @param state object representing the state of the world
     */
    public void updateState(T state) {
        if (currentTask.isComplete(state)) {
            currentTask = subTaskMp.nextKey(currentTask);
        }
    }

    /**
     * Adds an action to the history of actions performed.
     *
     * @param action the action the planner is considering executing
     */
    public void addAction(R action) {
        history.add(
                subTaskMp.get(currentTask)
                        .getStatus(action));
    }

    /**
     * Calculates the score of the current history
     * @return a score
     */
    public Double computeScore() {
        return scoringStrategy.compute(history);
    }
}
