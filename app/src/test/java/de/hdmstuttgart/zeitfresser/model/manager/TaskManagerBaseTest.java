package de.hdmstuttgart.zeitfresser.model.manager;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import de.hdmstuttgart.zeitfresser.model.Task;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class TaskManagerBaseTest extends TaskManager {

  private static final int MILLIS_PER_DAY = 1000 * 3600 * 24;

  Date testFromDate = new Date();
  Date testUntilDate = new Date(testFromDate.getTime() + MILLIS_PER_DAY);


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
    when(task.hasRecordsBefore(eq(testUntilDate))).thenReturn(true);
    when(task.hasRecordsAfter(eq(testFromDate))).thenReturn(false);
    when(task.getOverallDuration()).thenReturn(0.0f);
    return task;
  }

  private Task createTaskWithJustRecordsStartedBeforeNowNonZeroDuration() {
    Task task = mock(Task.class);
    when(task.hasRecordsBefore(eq(testUntilDate))).thenReturn(true);
    when(task.hasRecordsAfter(eq(testFromDate))).thenReturn(false);
    when(task.getOverallDuration()).thenReturn(1.0f);
    return task;
  }

  private Task createTaskWithJustRecordsStartedAfterNowZeroDuration() {
    Task task = mock(Task.class);
    when(task.hasRecordsBefore(eq(testUntilDate))).thenReturn(false);
    when(task.hasRecordsAfter(eq(testFromDate))).thenReturn(true);
    when(task.getOverallDuration()).thenReturn(0.0f);
    return task;
  }

  private Task createTaskWithJustRecordsStartedAfterNowNonZeroDuration() {
    Task task = mock(Task.class);
    when(task.hasRecordsBefore(eq(testUntilDate))).thenReturn(false);
    when(task.hasRecordsAfter(eq(testFromDate))).thenReturn(true);
    when(task.getOverallDuration()).thenReturn(1.0f);
    return task;
  }

  private Task createTaskWithRecordsStartedBeforeAndAfterNowZeroDuration() {
    Task task = mock(Task.class);
    when(task.hasRecordsBefore(eq(testUntilDate))).thenReturn(true);
    when(task.hasRecordsAfter(eq(testFromDate))).thenReturn(true);
    when(task.getOverallDuration()).thenReturn(0.0f);
    return task;
  }

  private Task createTaskWithRecordsStartedBeforeAndAfterNowNonZeroDuration() {
    Task task = mock(Task.class);
    when(task.hasRecordsBefore(eq(testUntilDate))).thenReturn(true);
    when(task.hasRecordsAfter(eq(testFromDate))).thenReturn(true);
    when(task.getOverallDuration()).thenReturn(1.0f);
    return task;
  }

  private Task createTaskWithNoRecordsAtAll() {
    Task task = mock(Task.class);
    when(task.hasRecordsBefore(eq(testUntilDate))).thenReturn(false);
    when(task.hasRecordsAfter(eq(testFromDate))).thenReturn(false);
    when(task.getOverallDuration()).thenReturn(0.0f);
    return task;
  }

  void assertException(Throwable actual, Class<? extends Throwable> expectedType,
                       String expectedMessage) {
    assertThat(actual.getClass().equals(expectedType), is(true));
    assertThat(actual.getMessage(), equalTo(expectedMessage));
  }

}
