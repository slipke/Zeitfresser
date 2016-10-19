package de.hdm_stuttgart.zeitfresser.model;

import java.util.LinkedList;
import java.util.List;

/**
 * A TaskManager holds and administrates a list of tasks.
 */

public abstract class TaskManager {

    private List<Task> taskList;

    protected TaskManager() {
        this.taskList = createTaskList();
    }

    protected List<Task> getTaskList() {
        if (taskList == null) {
            taskList = new LinkedList<>();
        }
        return taskList;
    }

    protected abstract List<Task> createTaskList();

    public void addTaskWithName(String taskName) {
        getTaskList().add(Task.withName(taskName));
    }

    public List<String> getExistentTaskNamesAsList() {
        List<String> taskNames = new LinkedList<>();
        for (Task task : getTaskList()) {
            taskNames.add(task.getName());
        }
        return taskNames;
    }

    public void startTask(String taskName) {
        findTaskByName(taskName).start();
    }

    public void stopTask(String taskName) {
        findTaskByName(taskName).stop();
    }


    public boolean isTaskActive(String taskName) {
        return findTaskByName(taskName).isActive();
    }

    private Task findTaskByName(String taskName) {
        for (Task task : taskList) {
            if (task.getName().equals(taskName)) {
                return task;
            }
        }
        throw new IllegalArgumentException(String.format("Unable to find task with name %s.", taskName));
    }

    public long getOverallDurationForTask(String taskName) {
        return findTaskByName(taskName).getOverallDuration();
    }
}
