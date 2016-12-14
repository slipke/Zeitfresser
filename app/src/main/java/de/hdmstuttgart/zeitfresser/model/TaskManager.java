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
   * Stop a task. An {@link IllegalArgumentException} is thrown if {@code null}
   * is passed as argument.
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
