package de.hdmstuttgart.zeitfresser.model;

import com.github.mikephil.charting.data.Entry;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public abstract class TaskManager {

  private List<Task> taskList = new LinkedList<>();

  TaskManager() {
    this.taskList = createTaskList();
  }

  protected abstract List<Task> createTaskList();

  /**
   * Return the current task list.
   *
   * @return the current {@link List} of tasks.
   */
  public List<Task> getTaskList() {
    return taskList;
  }

  /**
   * Start a task.
   *
   * @param task The task to be started.
   */
  public void startTask(Task task) {
    if (task == null) {
      throw new IllegalArgumentException("Argument \"task\" must not be null");
    }

    task.start();
  }

  /**
   * Stop a task. An {@link IllegalArgumentException} is thrown if {@code null} is passed as
   * argument.
   *
   * @param task The {@link Task} to be stopped.
   */
  public void stopTask(Task task) {
    if (task == null) {
      throw new IllegalArgumentException("Argument \"taskName\" must not be null or empty!)");
    }

    task.stop();
  }

  /**
   * Check if a certain {@link Task} is active.
   *
   * @param task The {@link Task} to be checked.
   * @return true if active, false otherwise.
   */
  public boolean isTaskActive(Task task) {
    if (task == null) {
      throw new IllegalArgumentException("Input argument \"task\" was null!");
    }

    return task.isActive();
  }

  /**
   * Compute total amount of time recorded for a certain {@code task}.
   *
   * @param task The task for which total time shall be computed.
   * @return The total amount of time over all records attached to {@code task}.
   */
  public float getOverallDurationForTask(Task task) {
    if (task == null) {
      throw new IllegalArgumentException("Input argument \"task\" was null!");
    }

    return task.getOverallDuration();
  }

  public List<Task> getFilteredTasks(Date from, Date to) {
    List<Task> newTaskList = new LinkedList<>();
    List<Task> taskListFrom = new LinkedList<>();
    List<Task> taskListTo = new LinkedList<>();

    if (from == null && to == null) {
      return filterZeroDurationTasks(taskList);
    }

    if (from != null) {
      taskListFrom = getTasksWithRecordsLaterThan(from, taskList);
    }

    if (to != null) {
      taskListTo = getTasksWithRecordsEarlierThan(to, taskList);
    }

    // Now compare both lists and return only objects which are in both lists
    if (to == null) {
      // No to set, return fromList
      newTaskList = taskListFrom;
    } else if (from == null) {
      // No from set, return toList
      newTaskList = taskListTo;
    } else {
      // Compare both lists
      newTaskList = getOnlyObjectsPresentInBothLists(taskListFrom, taskListTo);
    }

    return filterZeroDurationTasks(newTaskList);
  }

  private List<Task> getTasksWithRecordsLaterThan(Date date, List<Task> fullTaskList) {
    List<Task> newTaskList = new LinkedList<>();
    for (Task task : fullTaskList) {
      if (task.hasRecordsAfter(date)) {
        newTaskList.add(task);
      }
    }
    return newTaskList;
  }

  private List<Task> getTasksWithRecordsEarlierThan(Date date, List<Task> fullTaskList) {
    List<Task> newTaskList = new LinkedList<>();
    for (Task task : fullTaskList) {
      if (task.hasRecordsBefore(date)) {
        newTaskList.add(task);
      }
    }
    return newTaskList;
  }

  private List<Task> getOnlyObjectsPresentInBothLists(List<Task> fromList, List<Task> toList) {
    List<Task> newList = new LinkedList<>();

    // Iterate over fromList and add objects which are present in toList
    for (Task task : fromList) {
      if (toList.contains(task)) {
        newList.add(task);
      }
    }

    // Iterate over toList and add objects which are present in fromList and not already in newList
    for (Task task : toList) {
      if (fromList.contains(task) && !newList.contains(task)) {
        newList.add(task);
      }
    }

    return newList;
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
