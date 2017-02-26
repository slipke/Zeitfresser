package de.hdmstuttgart.zeitfresser.model.manager;

import static junit.framework.Assert.assertTrue;

import de.hdmstuttgart.zeitfresser.model.Task;
import de.hdmstuttgart.zeitfresser.model.manager.DefaultTaskManager;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

/**
 * A test class for {@link DefaultTaskManager}.
 *
 * @author simon.lipke
 */

public class DefaultTaskManagerTest {

  private DefaultTaskManager taskManager;

  @Before
  public void setUp() {
    this.taskManager = DefaultTaskManager.createInstance();
  }

  /**
   * Since this is our Task-Manager before the Database, the returned list is static with a fixed
   * length of 3.
   */
  @Test
  public void testGetTaskList() {
    List<Task> taskList = taskManager.getTaskList();
    assertTrue(taskList.size() == 3);
  }
}
