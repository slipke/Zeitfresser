package de.hdmstuttgart.zeitfresser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import de.hdmstuttgart.zeitfresser.model.DefaultTaskManager;
import de.hdmstuttgart.zeitfresser.model.Record;
import de.hdmstuttgart.zeitfresser.model.Task;

import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Date;
import java.util.List;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class DefaultTaskManagerTest {

  private static DefaultTaskManager taskManager;

  @BeforeClass
  public static void beforeClass() {
    taskManager = DefaultTaskManager.createInstance();
  }

  @Test
  public void getTaskListTest() {
    assertNotNull(taskManager.getTaskList());
    assertTrue(taskManager.getTaskList().size() > 0);
  }

  @Test
  public void addTaskWithNameTest() {
    String name = "Test";

    taskManager.addTaskWithName(name);

    Task tmpTask = null;
    for (Task t : taskManager.getTaskList()) {
      if (t.getName().equals(name)) {
        tmpTask = t;
        break;
      }
    }

    assertNotNull(tmpTask);
  }

  @Test
  public void getExistentTaskNamesAsListTest() {
    List<Task> taskList = taskManager.getTaskList();
    List<String> taskNames = taskManager.getExistentTaskNamesAsList();

    assertTrue(taskManager.getExistentTaskNamesAsList().size() > 0);
    assertEquals(taskManager.getTaskList().size(), taskManager.getExistentTaskNamesAsList().size());

    for (int i = 0; i < taskList.size(); i++) {
      assertEquals(taskList.get(i).getName(), taskNames.get(i));
    }
  }

  @Test
  public void startTaskTest() {
    Task task = Task.withName("Test");

    taskManager.startTask(task);

    assertTrue(taskManager.isTaskActive(task));
    assertTrue(task.isActive());

    taskManager.stopTask(task);

    assertFalse(taskManager.isTaskActive(task));
    assertFalse(task.isActive());
  }

  @Test
  public void tasksAsEntryListTest() {
    assertEquals(taskManager.getTaskList().size(), taskManager.tasksAsEntryList().size());
  }

  @Test
  public void getOverallDurationForTaskTest() {
    Task task = Task.withName("Test");
    Date now = new Date();

    int interval1 = 60 * 1000; // 1min
    int interval2 = 120 * 1000; // 2min
    int interval3 = 300 * 1000; // 5min

    // Setup dates
    Date endDate1 = new Date();
    endDate1.setTime(now.getTime() + interval1);
    Date endDate2 = new Date();
    endDate2.setTime(now.getTime() + interval2);
    Date endDate3 = new Date();
    endDate3.setTime(now.getTime() + interval3);
    Record record1 = Record.withStartAndEnd(now, endDate1);
    Record record2 = Record.withStartAndEnd(now, endDate2);
    Record record3 = Record.withStartAndEnd(now, endDate3);

    // Add records to task
    task.addRecord(record1);
    task.addRecord(record2);
    task.addRecord(record3);

    assertEquals(interval1 + interval2 + interval3, taskManager.getOverallDurationForTask(task), 0);
  }
}