package de.hdmstuttgart.zeitfresser.model;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class DefaultTaskManagerTest {

  private DefaultTaskManager taskManager;

  @Before
  public void before() {
    taskManager = DefaultTaskManager.createInstance();
  }

  @Test
  public void testCreateTaskList() {
    assertNotNull(taskManager.getTaskList());
    assertTrue(taskManager.getTaskList().size() > 0);
  }
}
