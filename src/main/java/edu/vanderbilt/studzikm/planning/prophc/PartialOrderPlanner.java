package edu.vanderbilt.studzikm.planning.prophc;

import org.apache.commons.collections4.OrderedMap;
import org.apache.commons.collections4.map.LinkedMap;
import org.apache.commons.collections4.queue.CircularFifoQueue;

import java.util.List;

public class PartialOrderPlanner<T, R> {

    private OrderedMap<Task<T>, List<SubTask<R>>> subTaskMp = new LinkedMap<>();
    private CircularFifoQueue<Boolean> history;
    private Task<T> currentTask;
    private ScoringStrategy scoringStrategy;

    public PartialOrderPlanner(int historyLength,
                               ScoringStrategy scoringStrategy) {
        this.history = new CircularFifoQueue<>(historyLength);
        this.scoringStrategy = scoringStrategy;
    }

    public void register(Task<T> task, List<SubTask<R>> subTasks) {
        subTaskMp.put(task, subTasks);
        if (currentTask == null) {
            currentTask = subTaskMp.firstKey();
        }
    }

    public void registerFinal(Task<T> finalTask, List<SubTask<R>> finalSubTask) {
        register(finalTask, finalSubTask);
    }

    public void updateState(T state) {
        if (currentTask.isComplete(state)) {
            currentTask = subTaskMp.nextKey(currentTask);
        }
    }

    public void addAction(R action) {
        for (SubTask<R> subtask : subTaskMp.get(currentTask)) {
            if (subtask.isSubTask(action)) {
                history.add(true);
                return;
            }
        }

        history.add(false);
    }

    public Double computeScore() {
        return scoringStrategy.compute(history);
    }
}
