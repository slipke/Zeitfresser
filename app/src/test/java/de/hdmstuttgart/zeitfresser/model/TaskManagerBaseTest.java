package de.hdmstuttgart.zeitfresser.model;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.github.mikephil.charting.data.Entry;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class TaskManagerBaseTest extends TaskManager {


  @Before
  public void before() {
  }

  /**
   * The startTask() method should invoke the start() method of our mocked Task once.
   */
  @Test
  public void testStartTask() {
    Task mockedTask = mock(Task.class);
    this.startTask(mockedTask);

    verify(mockedTask, times(1)).start();
  }

  /**
   * This test should fail, since the parameter to startTask() is null.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testStartTaskFails() {
    this.startTask(null);
  }

  /**
   * The stopTask() method should invoke the stop() method of our mocked Task once.
   */
  @Test
  public void testStopTask() {
    Task mockedTask = mock(Task.class);
    this.stopTask(mockedTask);

    verify(mockedTask, times(1)).stop();
  }

  /**
   * This test should fail, since the parameter to stopTask() is null.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testStopTaskFails() {
    this.stopTask(null);
  }

  /**
   * The isTaskActive() method should invoke the isActive() of our mocked Task once.
   */
  @Test
  public void testIsActiveTask() {
    Task mockedTask = mock(Task.class);
    this.isTaskActive(mockedTask);

    verify(mockedTask, times(1)).isActive();
  }

  /**
   * This test should fail, since the parameter to isTaskActive() is null.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testIsTaskActiveFails() {
    this.isTaskActive(null);
  }

  /**
   * The getOverallDurationForTask() method should invoke the getOverallDuration() method of our.
   * mocked Task once
   */
  @Test
  public void testGetOverallDurationForTask() {
    Task mockedTask = mock(Task.class);
    this.getOverallDurationForTask(mockedTask);

    verify(mockedTask, times(1)).getOverallDuration();
  }

  /**
   * This test should fail, since the parameter to getOverallDurationForTask() is null.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testGetOverallDurationForTaskFails() {
    this.getOverallDurationForTask(null);
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

    List<Entry> entryList = this.taskListToEntryList(taskList);

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

    List<String> labelList = this.taskListToLabelList(taskList);

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
  @Ignore
  public void testGetFilteredTasksWithEmptyValues() {
    List<Task> taskList = this.getFilteredTasks(null, null);
    assertEquals(3, taskList.size());
  }

  @Test
  @Ignore
  public void testGetFilteredTasksWithFrom() {
    Date from = new Date();
    List<Task> taskList = this.getFilteredTasks(from, null);
    assertEquals(1, taskList.size());
  }

  @Test
  @Ignore
  public void testGetFilteredTasksWithTo() {
    Date to = new Date();
    List<Task> taskList = this.getFilteredTasks(null, to);
    assertEquals(1, taskList.size());
  }

  @Test
  @Ignore
  public void testGetFilteredTasksWithFromAndTo() {
    Date from = new Date();
    // Now + 2days
    from.setTime(from.getTime() + (2 * 24 * 60 * 60 * 1000));
    Date to = new Date();
    // Now + 8days
    to.setTime(to.getTime() + (8 * 24 * 60 * 60 * 1000));

    List<Task> taskList = this.getFilteredTasks(from, to);
    assertEquals(1, taskList.size());
  }


  protected Date testDate = new Date();

  @Override
  public List<Task> getTaskList() {
    List<Task> tasks = new LinkedList<>();

    tasks.add(createTaskWithJustRecordsStartedBeforeNowZeroDuration());
    tasks.add(createTaskWithJustRecordsStartedBeforeNowNonZeroDuration());

    tasks.add(createTaskWithJustRecordsStartedAfterNowZeroDuration());
    tasks.add(createTaskWithJustRecordsStartedAfterNowNonZeroDuration());

    tasks.add(createTaskWithRecordsStartedBeforeAndAfterNowZeroDuration());
    tasks.add(createTaskWithRecordsStartedBeforeAndAfterNowNonZeroDuration());

    tasks.add(createTaskWithNoRecordsAtAll());

    return tasks;
  }

  private Task createTaskWithJustRecordsStartedBeforeNowZeroDuration() {
    Task task = mock(Task.class);
    when(task.hasRecordsBefore(eq(testDate))).thenReturn(true);
    when(task.hasRecordsAfter(eq(testDate))).thenReturn(false);
    when(task.getOverallDuration()).thenReturn(0.0f);
    return task;
  }

  private Task createTaskWithJustRecordsStartedBeforeNowNonZeroDuration() {
    Task task = mock(Task.class);
    when(task.hasRecordsBefore(eq(testDate))).thenReturn(true);
    when(task.hasRecordsAfter(eq(testDate))).thenReturn(false);
    when(task.getOverallDuration()).thenReturn(1.0f);
    return task;
  }

  private Task createTaskWithJustRecordsStartedAfterNowZeroDuration() {
    Task task = mock(Task.class);
    when(task.hasRecordsBefore(eq(testDate))).thenReturn(false);
    when(task.hasRecordsAfter(eq(testDate))).thenReturn(true);
    when(task.getOverallDuration()).thenReturn(0.0f);
    return task;
  }

  private Task createTaskWithJustRecordsStartedAfterNowNonZeroDuration() {
    Task task = mock(Task.class);
    when(task.hasRecordsBefore(eq(testDate))).thenReturn(false);
    when(task.hasRecordsAfter(eq(testDate))).thenReturn(true);
    when(task.getOverallDuration()).thenReturn(1.0f);
    return task;
  }

  private Task createTaskWithRecordsStartedBeforeAndAfterNowZeroDuration() {
    Task task = mock(Task.class);
    when(task.hasRecordsBefore(eq(testDate))).thenReturn(true);
    when(task.hasRecordsAfter(eq(testDate))).thenReturn(true);
    when(task.getOverallDuration()).thenReturn(0.0f);
    return task;
  }

  private Task createTaskWithRecordsStartedBeforeAndAfterNowNonZeroDuration() {
    Task task = mock(Task.class);
    when(task.hasRecordsBefore(eq(testDate))).thenReturn(true);
    when(task.hasRecordsAfter(eq(testDate))).thenReturn(true);
    when(task.getOverallDuration()).thenReturn(1.0f);
    return task;
  }

  private Task createTaskWithNoRecordsAtAll() {
    Task task = mock(Task.class);
    when(task.hasRecordsBefore(eq(new Date()))).thenReturn(false);
    when(task.hasRecordsAfter(eq(new Date()))).thenReturn(false);
    when(task.getOverallDuration()).thenReturn(0.0f);
    return task;
  }

  void assertException(Throwable actual, Class<? extends Throwable> expectedType,
                       String expectedMessage) {
    assertThat(actual.getClass().equals(expectedType), is(true));
    assertThat(actual.getMessage(), equalTo(expectedMessage));
  }

}
