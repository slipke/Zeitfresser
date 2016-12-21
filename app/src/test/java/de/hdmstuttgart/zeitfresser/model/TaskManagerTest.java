package de.hdmstuttgart.zeitfresser.model;

import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;

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
  public void testGetFilteredTasks() {
    // @TODO TDD
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