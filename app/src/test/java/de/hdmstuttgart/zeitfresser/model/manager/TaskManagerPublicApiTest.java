package de.hdmstuttgart.zeitfresser.model.manager;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.number.IsCloseTo.closeTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.github.mikephil.charting.data.Entry;
import de.hdmstuttgart.zeitfresser.model.Task;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * A test class for verifying the behavior of the public methods of {@link Task}.
 *
 * @author patrick.kleindienst
 */
public class TaskManagerPublicApiTest extends TaskManagerBaseTest {


  @Rule
  public ExpectedException expectedException = ExpectedException.none();


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
  public void testStartTaskFailsOnNullArg() {
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
  public void testStopTaskFailsOnNullArg() {
    this.stopTask(null);
  }

  /**
   * The isTaskActive() method should invoke the isActive() of our mocked Task once.
   */
  @Test
  public void testIsTaskActive() {
    Task mockedTask = mock(Task.class);
    this.isTaskActive(mockedTask);

    verify(mockedTask, times(1)).isActive();
  }


  /**
   * This test should fail, since the parameter to isTaskActive() is null.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testIsTaskActiveFailsOnNullArg() {
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
  public void testGetOverallDurationForTaskFailsOnNullArg() {
    this.getOverallDurationForTask(null);
  }


  /**
   * The asNamesList() method should convert a list of tasks to a list containing their
   * names. These are used as labels in the GUI.
   */
  @Test
  public void testAsNamesList() {
    List<Task> tasks = new LinkedList<>();
    Task mockedTask1 = mock(Task.class);
    Task mockedTask2 = mock(Task.class);

    String task1Name = "Task#1";
    String task2Name = "Task#2";

    when(mockedTask1.getName()).thenReturn(task1Name);
    when(mockedTask2.getName()).thenReturn(task2Name);

    tasks.addAll(Arrays.asList(mockedTask1, mockedTask2));

    List<String> result = this.asNamesList(tasks);

    assertThat(result, notNullValue());
    assertThat(result.size(), is(2));
    assertThat(result.contains(task1Name), is(true));
    assertThat(result.contains(task2Name), is(true));
  }

  @Test
  public void testAsNamesListThrowsExceptionOnNullArg() {
    expectedException.expect(IllegalArgumentException.class);
    expectedException.expectMessage("Argument 'tasks' must not be null!");

    this.asNamesList(null);
  }

  @Test
  public void testAsNamesReturnsEmptyListOnEmptyArgList() {
    List<String> result = this.asNamesList(new LinkedList<Task>());

    assertThat(result, notNullValue());
    assertThat(result.isEmpty(), is(true));
  }

  @Test
  public void testAsEntryList() {
    Task mockedTask1 = mock(Task.class);
    Task mockedTask2 = mock(Task.class);

    float task1Duration = 0.0f;
    long task1Id = 1;

    float task2Duration = 1.0f;
    long task2Id = 2;

    when(mockedTask1.getOverallDuration()).thenReturn(task1Duration);
    when(mockedTask1.getId()).thenReturn(task1Id);

    when(mockedTask2.getOverallDuration()).thenReturn(task2Duration);
    when(mockedTask2.getId()).thenReturn(task2Id);

    List<Task> tasks = new LinkedList<>();

    tasks.addAll(Arrays.asList(mockedTask1, mockedTask2));


    List<Entry> result = this.asEntryList(tasks);

    assertThat(result, notNullValue());
    assertThat(result.size(), equalTo(2));

    assertEntry(result.get(0), task1Duration, task1Id);
    assertEntry(result.get(1), task2Duration, task2Id);
  }


  @Test
  public void testAsEntryListThrowsExceptionOnNullArg() {
    expectedException.expect(IllegalArgumentException.class);
    expectedException.expectMessage("Argument 'tasks' must not be null!");

    this.asEntryList(null);
  }


  @Test
  public void testAsEntryListReturnsEmptyListOnEmptyArgList() {
    List<Entry> result = this.asEntryList(new LinkedList<Task>());

    assertThat(result, notNullValue());
    assertThat(result.isEmpty(), is(true));
  }


  @Test
  public void testGetFilteredTasksForBothArgsNull() {
    List<Task> tasks = this.getFilteredTasks(null, null);

    assertThat(tasks, notNullValue());
    assertThat(tasks.size(), equalTo(3));
  }

  @Test
  public void testGetFilteredTasksForFromIsNull() {
    List<Task> tasks = this.getFilteredTasks(null, testUntilDate);

    assertThat(tasks, notNullValue());
    assertThat(tasks.size(), equalTo(2));
  }

  @Test
  public void testGetFilteredTasksForUntilIsNull() {
    List<Task> tasks = this.getFilteredTasks(testFromDate, null);

    assertThat(tasks, notNullValue());
    assertThat(tasks.size(), equalTo(2));
  }

  private void assertEntry(Entry entry, float duration, long taskId) {
    assertThat((double) entry.getVal(), closeTo(duration, 0.00001));
    assertThat(entry.getXIndex(), equalTo((int) taskId));
  }


}
