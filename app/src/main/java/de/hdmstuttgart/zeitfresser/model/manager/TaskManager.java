package de.hdmstuttgart.zeitfresser.model.manager;

import com.github.mikephil.charting.data.Entry;
import de.hdmstuttgart.zeitfresser.model.Task;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public abstract class TaskManager {

  /**
   * Return the current task list.
   *
   * @return the current {@link List} of tasks.
   */
  public abstract List<Task> getTaskList();

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

  /**
   * Retrieve all Tasks which have records in the {@link Date} range between
   * <code>from</code> and <code>to</code>. If one of the arguments is null, this filter criterion
   * is simply ignored. If both arguments are null, there's no filtering according to start or
   * end date at all.
   *
   * @param from Defines the start of the period of interest (if not null).
   * @param to   Defines the end of the period of interest (if not null).
   * @return A list of tasks matching the given date range.
   */
  public List<Task> getFilteredTasks(Date from, Date to) {
    List<Task> tasks = getTaskList();

    if (from == null && to == null) {
      return filterZeroDurationTasks(tasks);
    }

    if (from != null) {
      tasks = getTasksWithRecordsLaterThan(from, tasks);
    }

    if (to != null) {
      tasks = getTasksWithRecordsEarlierThan(to, tasks);
    }

    return filterZeroDurationTasks(tasks);
  }

  private List<Task> getTasksWithRecordsLaterThan(Date date, List<Task> tasks) {
    if (date == null) {
      throw new IllegalArgumentException("Argument 'date' must not be null!");
    }

    if (tasks == null) {
      throw new IllegalArgumentException("Argument 'tasks' must not be null!");
    }

    List<Task> newTaskList = new LinkedList<>();
    for (Task task : tasks) {
      if (task.hasRecordsAfter(date)) {
        newTaskList.add(task);
      }
    }
    return newTaskList;
  }

  private List<Task> getTasksWithRecordsEarlierThan(Date date, List<Task> tasks) {
    if (date == null) {
      throw new IllegalArgumentException("Argument 'date' must not be null!");
    }

    if (tasks == null) {
      throw new IllegalArgumentException("Argument 'tasks' must not be null!");
    }

    List<Task> newTaskList = new LinkedList<>();
    for (Task task : tasks) {
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
    if (tasks == null) {
      throw new IllegalArgumentException("Argument 'tasks' must not be null");
    } else {

      List<Task> filteredList = new LinkedList<>();

      for (Task task : tasks) {
        if (task.getOverallDuration() > 0.0f) {
          filteredList.add(task);
        }
      }

      return filteredList;
    }


  }

  /**
   * Convert a list of tasks to a list of entries since this is the form they need to have to get
   * displayed at the frontend.
   *
   * @param tasks The list of tasks to be converted.
   * @return A corresponding list of entries.
   */
  public List<Entry> taskListToEntryList(List<Task> tasks) {
    List<Entry> entries = new LinkedList<>();

    for (Task task : tasks) {
      entries.add(new Entry(task.getOverallDuration(), (int) task.getId()));
    }

    return entries;
  }

  /**
   * Convert a list of tasks to another list containing their corresponding labels.
   *
   * @param tasks The list of tasks whose labels are extracted.
   * @return A list of labels.
   */
  public List<String> taskListToLabelList(List<Task> tasks) {
    List<String> labels = new LinkedList<>();

    for (Task task : tasks) {
      labels.add(task.getName());
    }

    return labels;
  }

}
