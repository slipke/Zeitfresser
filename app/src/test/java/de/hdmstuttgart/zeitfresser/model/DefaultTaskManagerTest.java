package de.hdmstuttgart.zeitfresser.model;

import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Method;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class DefaultTaskManagerTest {

  private DefaultTaskManager taskManager;

  @Before
  public void before() {
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
  public void getOverallDurationForTaskTest() {
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

    Task task = Task.withName("Test");

    // Add records to task
    task.addRecord(record1);
    task.addRecord(record2);
    task.addRecord(record3);

    assertEquals(interval1 + interval2 + interval3, taskManager.getOverallDurationForTask(task), 0);
  }

  @Test
  public void testFilterZeroDurationTasks() throws Exception {
    Task dummyTask1 = mock(Task.class);
    when(dummyTask1.getOverallDuration()).thenReturn(0.0f);

    Task dummyTask2 = mock(Task.class);
    when(dummyTask2.getOverallDuration()).thenReturn(1.0f);

    List<Task> tasks = new LinkedList<>();
    tasks.add(dummyTask1);
    tasks.add(dummyTask2);

    Method filterZeroDurationTasks = DefaultTaskManager.class.getSuperclass().getDeclaredMethod
            ("filterZeroDurationTasks", List.class);
    filterZeroDurationTasks.setAccessible(true);

    List<Task> filteredList = (List<Task>) filterZeroDurationTasks.invoke(taskManager, tasks);

    assertThat(filteredList, notNullValue());
    assertThat(filteredList.size(), not(equalTo(0)));
    assertThat(filteredList.contains(dummyTask2), equalTo(true));
    assertThat(filteredList.contains(dummyTask1), equalTo(false));

  }
}