package de.hdmstuttgart.zeitfresser.model;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.notNullValue;
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
import java.util.LinkedList;
import java.util.List;



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
  }

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
  }
}