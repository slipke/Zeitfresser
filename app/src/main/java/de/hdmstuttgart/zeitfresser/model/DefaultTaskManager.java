package de.hdmstuttgart.zeitfresser.model;

import java.util.LinkedList;
import java.util.List;

public class DefaultTaskManager extends TaskManager {

  public static DefaultTaskManager createInstance() {
    return new DefaultTaskManager();
  }

  @Override
  public List<Task> getTaskList() {
    List<Task> tasks = new LinkedList<>();
    tasks.add(Task.withName("Dummy Task #1"));
    tasks.add(Task.withName("Dummy Task #2"));
    tasks.add(Task.withName("Dummy Task #3"));
    return tasks;
  }
}
