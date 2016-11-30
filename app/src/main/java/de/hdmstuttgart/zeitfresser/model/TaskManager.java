package de.hdmstuttgart.zeitfresser.model;

import com.github.mikephil.charting.data.Entry;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

/**
 * A TaskManager holds and administrates a list of tasks.
 */

public abstract class TaskManager {

  private List<Task> taskList;

  protected TaskManager() {
    this.taskList = createTaskList();
  }

  public List<Task> getTaskList() {
    if (taskList == null) {
      taskList = new LinkedList<>();
    }
    return taskList;
  }

  protected abstract List<Task> createTaskList();

  public void addTaskWithName(String taskName) {
    if (taskName != null && !taskName.isEmpty()) {
      getTaskList().add(Task.withName(taskName));
    } else {
      throw new IllegalArgumentException("Argument \"taskName\" must not be null or empty!)");
    }
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

  public void startTask(Task task) {
    if (task != null) {
      task.start();
    }
  }

  public void stopTask(Task task) {
    if (task != null) {
      task.stop();
    } else {
      throw new IllegalArgumentException("Input argument \"task\" was null!");
    }
  }

  public boolean isTaskActive(Task task) {
    if (task != null) {
      return task.isActive();
    } else {
      throw new IllegalArgumentException("Input argument \"task\" was null!");
    }
  }

  public float getTotalDurationForTask(Task task) {
    if (task != null) {
      return task.getOverallDuration();
    } else {
      throw new IllegalArgumentException("Input argument \"task\" was null!");
    }
  }

  /**
   * Returns all tasks as an entry list.
   *
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
