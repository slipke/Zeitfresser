package de.hdmstuttgart.zeitfresser.model;

import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Date;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;


/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class TaskManagerTest {

  private DefaultTaskManager taskManager;

  @Before
  public void before() {
    taskManager = DefaultTaskManager.createInstance();
  }

  @Test
  public void testStartTask() {
    Task dummyTask = mock(Task.class);
    taskManager.startTask(dummyTask);

    verify(dummyTask, times(1)).start();
  }

  @Test
  public void testStopTask() {
    Task dummyTask = mock(Task.class);
    taskManager.stopTask(dummyTask);

    verify(dummyTask, times(1)).stop();
  }

  @Test
  public void testIsActiveTask() {
    Task dummyTask = mock(Task.class);
    taskManager.isTaskActive(dummyTask);

    verify(dummyTask, times(1)).isActive();
  }

  @Test
  public void testGetOverallDurationForTask() {
    Task dummyTask = mock(Task.class);
    taskManager.getOverallDurationForTask(dummyTask);

    verify(dummyTask, times(1)).getOverallDuration();
  }

  @Test
  public void testGetFilteredTasks() throws NoSuchFieldException, IllegalAccessException {
    // Now + 2h
    Date from = new Date();
    from.setTime(from.getTime() + (2 * 60 * 60 * 1000));
    // Now + 4h
    Date to = new Date();
    to.setTime(to.getTime() + (4 * 60 * 60 * 1000));

    // Create records
    // Record not in test time
    // Now + 1h
    Date record1From = new Date();
    record1From.setTime(record1From.getTime() + (60 * 60 * 1000));
    // Now + 1h 5min
    Date record1To = new Date();
    record1To.setTime(record1To.getTime() + (65 * 60 * 1000));
    Record record1 = Record.withStartAndEnd(record1From, record1To);

    // Record in test time
    // Now + 2h 5min
    Date record2From = new Date();
    record2From.setTime(record2From.getTime() + (125 * 60 * 1000));
    // Now + 2h 10min
    Date record2To = new Date();
    record2To.setTime(record2To.getTime() + (130 * 60 * 1000));
    Record record2 = Record.withStartAndEnd(record2From, record2To);

    // Create RecordList
    List<Record> recordList = new LinkedList<>();
    recordList.add(record1);
    recordList.add(record2);

    // Create Task
    Task task = Task.withName("Dummy");
    Field recordsField = Task.class.getDeclaredField("records");
    recordsField.setAccessible(true);
    recordsField.set(task, recordList);

    // Create TaskList
    List<Task> taskList = new LinkedList<>();
    taskList.add(task);

    // Add taskList to taskManager
    Field taskListField = TaskManager.class.getDeclaredField("taskList");
    taskListField.setAccessible(true);
    taskListField.set(taskManager, taskList);

    List<Task> taskListToCheck = taskManager.getFilteredTasks(from, to);
    assertEquals(1, taskListToCheck.size());
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

  @Test
  public void taskListToEntryListTest() {

  }

  @Test
  public void taskListToLabelListTest() {

  }
}