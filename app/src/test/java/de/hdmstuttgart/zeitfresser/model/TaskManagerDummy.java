package de.hdmstuttgart.zeitfresser.model;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by simonlipke on 17.02.17.
 */

public class TaskManagerDummy extends TaskManager {

  @Override
  public List<Task> getTaskList() {
    List<Task> tasks = new LinkedList<>();
    tasks.add(Task.withName("Dummy Task #1"));
    tasks.add(Task.withName("Dummy Task #2"));
    tasks.add(Task.withName("Dummy Task #3"));
    return tasks;
  }
}
