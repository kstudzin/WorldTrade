package edu.vanderbilt.studzikm.planning.prophc.lib;

import org.apache.commons.collections4.OrderedMap;
import org.apache.commons.collections4.map.LinkedMap;
import org.apache.commons.collections4.queue.CircularFifoQueue;


public class PartialOrderPlanner<T, R> {

    private OrderedMap<Task<T>, SubTask<R>> subTaskMp = new LinkedMap<>();
    private CircularFifoQueue<SubTaskStatus> history;
    private Task<T> currentTask;
    private ScoringStrategy scoringStrategy;
    private int historyLength;

    public PartialOrderPlanner(int historyLength,
                               ScoringStrategy scoringStrategy) {
        this.historyLength = historyLength;
        this.history = new CircularFifoQueue<>(historyLength);
        this.scoringStrategy = scoringStrategy;
    }

    public PartialOrderPlanner(PartialOrderPlanner original) {
        this.historyLength = original.historyLength;
        this.history = new CircularFifoQueue<>(historyLength);
        this.history.addAll(original.history);
        this.scoringStrategy = original.scoringStrategy;
        this.subTaskMp = new LinkedMap<>();
        this.subTaskMp.putAll(original.subTaskMp);
        this.currentTask = original.currentTask;
    }

    public void register(Task<T> task, SubTask<R> subTasks) {
        subTaskMp.put(task, subTasks);
        if (currentTask == null) {
            currentTask = subTaskMp.firstKey();
        }
    }

    public void registerFinal(Task<T> finalTask, SubTask<R> finalSubTask) {
        register(finalTask, finalSubTask);
    }

    public void updateState(T state) {
        if (currentTask.isComplete(state)) {
            currentTask = subTaskMp.nextKey(currentTask);
        }
    }

    public void addAction(R action) {
        history.add(
                subTaskMp.get(currentTask)
                        .getStatus(action));
    }

    public Double computeScore() {
        return scoringStrategy.compute(history);
    }
}
