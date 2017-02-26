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


  /*
      Equivalence classes for startTask(task):

      1) A task object different from null ( -> invokes start() on task)
      2) null (-> IllegalArgumentException)
   */

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


  /*
      Equivalence classes for stopTask(task):

      1) A task object different from null (-> invokes stop() on task)
      2) null (-> IllegalArgumentException)
   */

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


  /*
      Equivalence classes for isTaskActive(task):

      1) A task object different from null (-> invokes isActive() on task)
      2) null (-> IllegalArgumentException)
   */


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


  /*
      Equivalence classes for getOverallDurationForTask(task):

      1) A task object different from null (-> returns accumulative durations of all records)
      2) null (-> IllegalArgumentException)
   */

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
   * This test should fail, since the parameter to <code>getOverallDurationForTask()</code> is null.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testGetOverallDurationForTaskFailsOnNullArg() {
    this.getOverallDurationForTask(null);
  }


  /*
      Equivalence classes for asNamesList(taskList):

      1) A list containing n task instances, where n > 0 (-> returns a list of n task names)
      2) A list containing zero task instances (-> returns an empty list)
      3) null (-> IllegalArgumentException)
   */

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

  /**
   * If an empty list is passed as an argument to <code>asNamesList()</code>, we expect it to
   * return an empty list.
   */
  @Test
  public void testAsNamesReturnsEmptyListOnEmptyArgList() {
    List<String> result = this.asNamesList(new LinkedList<Task>());

    assertThat(result, notNullValue());
    assertThat(result.isEmpty(), is(true));
  }

  /**
   * When <code>asNamesList()</code> gets invoked with null as argument, we expect it to fail
   * with an {@link IllegalArgumentException}.
   */
  @Test
  public void testAsNamesListThrowsExceptionOnNullArg() {
    expectedException.expect(IllegalArgumentException.class);
    expectedException.expectMessage("Argument 'tasks' must not be null!");

    this.asNamesList(null);
  }


  /*
      Equivalence classes for asEntryList(taskList):

      1) A list containing n task instances, where n > 0 (-> returns list of n Entry instances)
      2) A list containing zero task instances (-> returns an empty list)
      3) null (-> IllegalArgumentException)
   */


  /**
   * We expect <code>asEntryList()</code> to return a list of entries for a valid list argument.
   */
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


  /**
   * We expect <code>asEntryList()</code> to return an empty list if the argument list is also
   * empty.
   */
  @Test
  public void testAsEntryListReturnsEmptyListOnEmptyArgList() {
    List<Entry> result = this.asEntryList(new LinkedList<Task>());

    assertThat(result, notNullValue());
    assertThat(result.isEmpty(), is(true));
  }


  /**
   * If <code>null</code> gets passed as an argument, an <code>IllegalArgumentException</code>
   * shall be thrown.
   */
  @Test
  public void testAsEntryListThrowsExceptionOnNullArg() {
    expectedException.expect(IllegalArgumentException.class);
    expectedException.expectMessage("Argument 'tasks' must not be null!");

    this.asEntryList(null);
  }

  /*
      For public method getFilteredTasks(fromDate, untilDate), the following equivalence classes
      exist:

      > Parameter "from":
        1) null (-> Argument ignored for filtering)
        2) valid Date object (-> lower bound for filtering)

      > Parameter "until":
        1) null (-> Argument ignored for filtering)
        2) valid Date object (-> upper bound for filtering)

      Testing any combination of these possible argument values leads to four test cases.
   */

  /**
   * Combination #1: Both arguments are <code>null</code>.
   */
  @Test
  public void testGetFilteredTasksForBothArgsNull() {
    List<Task> tasks = this.getFilteredTasks(null, null);

    assertThat(tasks, notNullValue());
    assertThat(tasks.size(), is(3));
  }

  /**
   * Combination #2: "From" is <code>null</code>, "Until" is valid.
   */
  @Test
  public void testGetFilteredTasksForFromIsNull() {
    List<Task> tasks = this.getFilteredTasks(null, testUntilDate);

    assertThat(tasks, notNullValue());
    assertThat(tasks.size(), is(2));
  }

  /**
   * Combination #3: "Until" is <code>null</code>, "From" is valid.
   */
  @Test
  public void testGetFilteredTasksForUntilIsNull() {
    List<Task> tasks = this.getFilteredTasks(testFromDate, null);

    assertThat(tasks, notNullValue());
    assertThat(tasks.size(), is(2));
  }

  /**
   * Combination #4: Both arguments are valid.
   */
  @Test
  public void testGetFilteredTasksForBothArgsValid() {
    List<Task> tasks = this.getFilteredTasks(testFromDate, testUntilDate);

    assertThat(tasks, notNullValue());
    assertThat(tasks.size(), is(1));
  }

  private void assertEntry(Entry entry, float duration, long taskId) {
    assertThat((double) entry.getVal(), closeTo(duration, 0.00001));
    assertThat(entry.getXIndex(), equalTo((int) taskId));
  }


}
