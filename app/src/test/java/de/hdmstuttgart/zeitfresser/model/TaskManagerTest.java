package de.hdmstuttgart.zeitfresser.model;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.github.mikephil.charting.data.Entry;

import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Method;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class TaskManagerTest {

  private TaskManagerDummy taskManager;

  @Before
  public void before() {
    taskManager = new TaskManagerDummy();
  }

  /**
   * The startTask() method should invoke the start() method of our mocked Task once.
   */
  @Test
  public void testStartTask() {
    Task dummyTask = mock(Task.class);
    taskManager.startTask(dummyTask);

    verify(dummyTask, times(1)).start();
  }

  /**
   * This test should fail, since the parameter to startTask() is null.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testStartTaskFails() {
    taskManager.startTask(null);
  }

  /**
   * The stopTask() method should invoke the stop() method of our mocked Task once.
   */
  @Test
  public void testStopTask() {
    Task dummyTask = mock(Task.class);
    taskManager.stopTask(dummyTask);

    verify(dummyTask, times(1)).stop();
  }

  /**
   * This test should fail, since the parameter to stopTask() is null.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testStopTaskFails() {
    taskManager.stopTask(null);
  }

  /**
   * The isTaskActive() method should invoke the isActive() of our mocked Task once.
   */
  @Test
  public void testIsActiveTask() {
    Task dummyTask = mock(Task.class);
    taskManager.isTaskActive(dummyTask);

    verify(dummyTask, times(1)).isActive();
  }

  /**
   * This test should fail, since the parameter to isTaskActive() is null.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testIsTaskActiveFails() {
    taskManager.isTaskActive(null);
  }

  /**
   * The getOverallDurationForTask() method should invoke the getOverallDuration() method of our.
   * mocked Task once
   */
  @Test
  public void testGetOverallDurationForTask() {
    Task dummyTask = mock(Task.class);
    taskManager.getOverallDurationForTask(dummyTask);

    verify(dummyTask, times(1)).getOverallDuration();
  }

  /**
   * This test should fail, since the parameter to getOverallDurationForTask() is null.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testGetOverallDurationForTaskFails() {
    taskManager.getOverallDurationForTask(null);
  }

  /**
   * The method filterZeroDurationTasks() should return a cleaned list of Tasks which does not
   * contain Tasks with no overall duration.
   */
  @Test
  public void testFilterZeroDurationTasks() throws Exception {
    Task dummyTask1 = mock(Task.class);
    when(dummyTask1.getOverallDuration()).thenReturn(0.0f);

    Task dummyTask2 = mock(Task.class);
    when(dummyTask2.getOverallDuration()).thenReturn(1.0f);

    List<Task> tasks = new LinkedList<>();
    tasks.add(dummyTask1);
    tasks.add(dummyTask2);

    Method filterZeroDurationTasks = DefaultTaskManager
            .class
            .getSuperclass()
            .getDeclaredMethod("filterZeroDurationTasks", List.class);
    filterZeroDurationTasks.setAccessible(true);

    List<Task> filteredList = (List<Task>) filterZeroDurationTasks.invoke(taskManager, tasks);

    assertThat(filteredList, notNullValue());
    assertThat(filteredList.size(), not(equalTo(0)));
    assertThat(filteredList.contains(dummyTask2), equalTo(true));
    assertThat(filteredList.contains(dummyTask1), equalTo(false));
  }

  /**
   * The taskListToEntryList() method should convert a list of Task to a list of Entry.
   */
  @Test
  public void taskListToEntryListTest() {
    // Create taskList with entries
    List<Task> taskList = new LinkedList<>();
    Task task1 = Task.withName("Dummy 1");
    Task task2 = Task.withName("Dummy 2");
    Task task3 = Task.withName("Dummy 3");
    taskList.add(task1);
    taskList.add(task2);
    taskList.add(task3);

    List<Entry> entryList = taskManager.taskListToEntryList(taskList);

    assertTrue(taskList.size() == entryList.size());
    for (Entry entry : entryList) {
      assertEquals(Entry.class, entry.getClass());
    }
  }

  /**
   * The taskListToLabelList() method should convert a list of Task to a list of String.
   */
  @Test
  public void taskListToLabelListTest() {
    // Create taskList with entries
    List<Task> taskList = new LinkedList<>();
    Task task1 = Task.withName("Dummy 1");
    Task task2 = Task.withName("Dummy 2");
    Task task3 = Task.withName("Dummy 3");
    taskList.add(task1);
    taskList.add(task2);
    taskList.add(task3);

    List<String> labelList = taskManager.taskListToLabelList(taskList);

    assertTrue(taskList.size() == labelList.size());
    for (String label : labelList) {
      assertEquals(String.class, label.getClass());
    }
  }

  /**
   * The getFilteredTasks() method with from and to set as null should only return the
   * filterZeroDuration() Tasks.
   */
  @Test
  public void testGetFilteredTasksWithEmptyValues() {
    List<Task> taskList = taskManager.getFilteredTasks(null, null);
    assertEquals(3, taskList.size());
  }

  @Test
  public void testGetFilteredTasksWithFrom() {
    Date from = new Date();
    List<Task> taskList = taskManager.getFilteredTasks(from, null);
    assertEquals(1, taskList.size());
  }

  @Test
  public void testGetFilteredTasksWithTo() {
    Date to = new Date();
    List<Task> taskList = taskManager.getFilteredTasks(null, to);
    assertEquals(1, taskList.size());
  }

  @Test
  public void testGetFilteredTasksWithFromAndTo() {
    Date from = new Date();
    // Now + 2days
    from.setTime(from.getTime() + (2 * 24 * 60 * 60 * 1000));
    Date to = new Date();
    // Now + 8days
    to.setTime(to.getTime() + (8 * 24 * 60 * 60 * 1000));

    List<Task> taskList = taskManager.getFilteredTasks(from, to);
    assertEquals(1, taskList.size());
  }
}
