package de.hdmstuttgart.zeitfresser.model;

import com.github.mikephil.charting.data.Entry;

import java.util.ArrayList;
import java.util.Date;
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

  /**
   * Return the current task list.
   *
   * @return the current {@link List} of tasks.
   */
  public List<Task> getTaskList() {
    if (taskList == null) {
      taskList = new LinkedList<>();
    }
    return taskList;
  }

  protected abstract List<Task> createTaskList();

  /**
   * Add a new task with name {@code taskName} to the list of tasks.
   *
   * @param taskName the name of the new {@link Task}.
   */
  public void addTaskWithName(String taskName) {
    if (taskName != null && !taskName.isEmpty()) {
      getTaskList().add(Task.withName(taskName));
    } else {
      throw new IllegalArgumentException("Argument \"taskName\" must not be null or empty!)");
    }
  }

  /**
   * Start a task.
   *
   * @param task The task to be started.
   */
  public void startTask(Task task) {
    if (task != null) {
      task.start();
    }
  }

  /**
   * Stop a task. An {@link IllegalArgumentException} is thrown if {@code null} is passed as
   * argument.
   *
   * @param task The {@link Task} to be stopped.
   */
  public void stopTask(Task task) {
    if (task != null) {
      task.stop();
    } else {
      throw new IllegalArgumentException("Input argument \"task\" was null!");
    }
  }

  /**
   * Check if a certain {@link Task} is active.
   *
   * @param task The {@link Task} to be checked.
   * @return true if active, false otherwise.
   */
  public boolean isTaskActive(Task task) {
    if (task != null) {
      return task.isActive();
    } else {
      throw new IllegalArgumentException("Input argument \"task\" was null!");
    }
  }

  /**
   * Compute total amount of time recorded for a certain {@code task}.
   *
   * @param task The task for which total time shall be computed.
   * @return The total amount of time over all records attached to {@code task}.
   */
  public float getOverallDurationForTask(Task task) {
    if (task != null) {
      return task.getOverallDuration();
    } else {
      throw new IllegalArgumentException("Input argument \"task\" was null!");
    }
  }

  public List<Task> getFilteredTasks(Date from, Date to) {
    List<Task> filteredTaskList = new LinkedList<>();

    if (from == null && to == null) {
      return filterZeroDurationTasks(taskList);
    }

    for (Task task : taskList) {
      // 1.) If from and to is set, check for records above from & below date
      if (from != null && to != null && task.hasRecordsBetween(from, to)) {
        filteredTaskList.add(task);
      }

      // 2.) If only from is set, check only for records above from
      if (from != null && to == null && task.hasRecordsAbove(from)) {
        filteredTaskList.add(task);
      }

      // 3.) If only to is set, check only for records below to
      if (from == null && to != null && task.hasRecordsBelow(to)) {
        filteredTaskList.add(task);
      }
    }

    return filterZeroDurationTasks(filteredTaskList);
  }

  private List<Task> filterZeroDurationTasks(List<Task> tasks) {
    List<Task> filteredList = new LinkedList<>();

    for (Task task : tasks) {
      if (task.getOverallDuration() > 0.0f) {
        filteredList.add(task);
      }
    }

    return filteredList;
  }

  public List<Entry> taskListToEntryList(List<Task> tasks) {
    List<Entry> entries = new LinkedList<>();

    for (Task task : tasks) {
      entries.add(new Entry(task.getOverallDuration(), (int) task.getId()));
    }

    return entries;
  }

  public List<String> taskListToLabelList(List<Task> tasks) {
    List<String> labels = new LinkedList<>();

    for (Task task : tasks) {
      labels.add(task.getName());
    }

    return labels;
  }

}
