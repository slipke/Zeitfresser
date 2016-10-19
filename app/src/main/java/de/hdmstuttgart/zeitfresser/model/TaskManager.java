package de.hdmstuttgart.zeitfresser.model;

import com.github.mikephil.charting.data.Entry;

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

  /**
   * Returns a list of all task names.
   *
   * @return List
   */
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
    throw new IllegalArgumentException(String.format("Unable o find task with name %s.", taskName));
  }

  public float getOverallDurationForTask(String taskName) {
    return findTaskByName(taskName).getOverallDuration();
  }

  /**
   * Returns all tasks as an entry list.
   * @return List
   */
  public List<Entry> tasksAsEntryList() {
    List<Entry> entries = new LinkedList<>();
    for (Task task : taskList) {
      entries.add(new Entry(task.getOverallDuration(), (int) task.getId()));
    }
    return entries;
  }
}
