package de.hdmstuttgart.zeitfresser.model;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by patrick on 19.10.16.
 */

public class DefaultTaskManager extends TaskManager {

  public static DefaultTaskManager createInstance() {
    return new DefaultTaskManager();
  }

  private DefaultTaskManager() {

  }

  @Override
  protected List<Task> createTaskList() {
    List<Task> tasks = new LinkedList<>();
    tasks.add(Task.withNameAndId("Dummy Task #1", 1));
    tasks.add(Task.withNameAndId("Dummy Task #2", 2));
    tasks.add(Task.withNameAndId("Dummy Task #3", 3));
    return tasks;
  }
}
