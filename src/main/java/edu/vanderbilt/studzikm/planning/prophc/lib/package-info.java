/**
 * Relaxed Constraint Partial Order Planner with Histories
 *
 * This is an implementation of a partial order planner that assigns scores
 * to a sequence of actions based on if the sequence elements work together
 * to accomplish a task.  A partial order planner specifies tasks and subtasks
 * and finds a way to execute subtasks so that tasks are completed. While the
 * tasks may have an ordering, e.g., complete Task A before Task B, the subtasks
 * are not ordered. This implementation has relaxed constraints because it allows
 * a subtasks to execute that belong to a different task than the current one.
 * However, a subtask that is not a part of the current task will score lower than
 * a subtask of current task.
 *
 * While not FOL, uses lambdas heavily
 */
package edu.vanderbilt.studzikm.planning.prophc.lib;