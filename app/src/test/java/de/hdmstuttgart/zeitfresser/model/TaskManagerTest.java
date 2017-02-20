package de.hdmstuttgart.zeitfresser.model;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.github.mikephil.charting.data.Entry;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class TaskManagerTest extends TaskManager {


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


  // TDD


  /**
   * TDD Design Decision: <br/>
   * We want to have a private method <code>filterZeroDurationTasks</code> which
   * takes a list of tasks as an argument and filters out tasks with a total duration of zero.
   * <br/><br/>
   * Goal: <br/>
   * This test checks if the
   * method exists and throws an Exception which makes the test fail if it doesn't.
   * <br/><br/>
   * Implementation: <br/>
   * Make test pass by adding the required method to class <code>TaskManager.</code>
   */
  @Test
  public void testFilterZeroDurationTasksExists() throws Exception {
    Method filterZeroDurationTasks = TaskManager.class.getDeclaredMethod(
        "filterZeroDurationTasks", List.class);

    assertThat(filterZeroDurationTasks, notNullValue());
    assertThat(filterZeroDurationTasks.getClass().equals(Method.class), is(true));
  }

  /**
   * TDD Design Decision: <br/>
   * If <code>null</code> is passed to <code>filterZeroDurationTasks</code>, an
   * {@link IllegalArgumentException} shall be thrown.
   * <br/><br/>
   * Goal: <br/>
   * This test invokes the method with a null argument and checks if the expected exception
   * is actually thrown. It fails if no exception has been thrown or the exception is not an
   * <code>IllegalArgumentException.</code>
   * <br/><br/>
   * Implementation: <br/>
   * We add a check to the method body which compares the passed list to <code>null</code>. If
   * the condition evaluates to true (argument is <code>null</code>), an
   * <code>IllegalArgumentException</code> is thrown.
   */
  @Test
  public void testFilterZeroDurationTasksFailsOnNullArg() throws Exception {
    Method filterZeroDurationTasks = TaskManager.class.getDeclaredMethod(
        "filterZeroDurationTasks", List.class);
    filterZeroDurationTasks.setAccessible(true);

    try {
      filterZeroDurationTasks.invoke(this, new Object[]{null});
      fail("An exception should have been thrown.");
    } catch (InvocationTargetException ex) {
      assertThat(ex.getCause().getClass().equals(IllegalArgumentException.class), is(true));
    }
  }


  /**
   * TDD Design Decision: <br/>
   * If an empty task list is passed to <code>filterZeroDurationTasks</code>, we expect the
   * method to do nothing but return an empty list itself.
   * <br/><br/>
   * Goal: <br/>
   * This test invokes the method with an empty task list as an argument and verifies that an
   * empty list is returned.
   * <br/><br/>
   * Implementation: <br/>
   * Inside the else-branch, which is empty at this stage of development, add a simple return
   * statement that returns an empty list, like <code>return new LinkedList<>()</code>.
   */
  @Test
  public void testFilterZeroDurationTasksReturnsEmptyList() throws Exception {
    Method filterZeroDurationTasks = TaskManager.class.getDeclaredMethod(
        "filterZeroDurationTasks", List.class);
    filterZeroDurationTasks.setAccessible(true);

    Object result = filterZeroDurationTasks.invoke(this, new LinkedList<>());

    assertThat(result.getClass().equals(LinkedList.class), is(true));
    assertThat(((LinkedList) result).size(), is(0));
  }

  /**
   * TDD Design Decision: <br/>
   * If a task list with size > 0 is passed as an argument to <code>filterZeroDurationTasks</code>,
   * every task in the list which has an overall duration equal to null is filtered by this method.
   * We therefore expect it to return a list of tasks which contains all the tasks from the input
   * list whose overall duration is > 0.
   * <br/><br/>
   * Goal: <br/>
   * This test creates a list containing to mocked task objects, of which one of them returns 0
   * if <code>getOverallDuration()</code> is called, whilst the other one returns a value greater
   * than 0. As specified, we expect the method to return a list which only contains the second
   * task mock object.
   * <br/><br/>
   * Implementation: <br/>
   * To make the test pass, we replace the <code>return new LinkedList<>()</code> statement by a
   * loop which iterates over all tasks in the argument list and only adds the current task to
   * the output list if its returns an overall duration > 0. After the iteration over the input
   * list is done, the output list is returned.
   */
  @Test
  public void testFilterZeroDurationTasksFiltersAsExpected() throws Exception {
    Task dummyTask1 = mock(Task.class);
    when(dummyTask1.getOverallDuration()).thenReturn(0.0f);

    Task dummyTask2 = mock(Task.class);
    when(dummyTask2.getOverallDuration()).thenReturn(1.0f);

    List<Task> tasks = new LinkedList<>();
    tasks.add(dummyTask1);
    tasks.add(dummyTask2);

    Method filterZeroDurationTasks = TaskManager.class
        .getDeclaredMethod("filterZeroDurationTasks", List.class);
    filterZeroDurationTasks.setAccessible(true);

    Object result = filterZeroDurationTasks.invoke(this, tasks);

    assertThat(result, notNullValue());
    assertThat(result.getClass().equals(LinkedList.class), equalTo(true));
    assertThat(((LinkedList) result).size(), not(equalTo(0)));

    // Dummy Task 2 must be in the list, since its duration is > 0.
    assertThat(((LinkedList) result).contains(dummyTask2), equalTo(true));

    // Dummy Task 2 must not be in the list, since its duration is 0.
    assertThat(((LinkedList) result).contains(dummyTask1), equalTo(false));
  }


  /**
   * TTD Design Decision: <br/>
   * For being able to filter tasks by date, we want to have a method
   * <code>getTasksWithRecordsLaterThan()</code> which filters out every task from a list which
   * does not have at least a single record which has been started AFTER the given date.
   * <br/><br/>
   * Goal: <br/>
   * This test simply checks if the method exists in the <code>TaskManager</code> class and
   * throws and exception if it doesn't.
   * <br/><br/>
   * Implementation: <br/>
   * Add a method <code>getTasksWithRecordsLaterThan()</code> to class <code>TaskManager</code>.
   * The body stays empty at first.
   *
   * @throws Exception
   */
  @Test
  public void testGetTasksWithRecordsLaterThanExists() throws Exception {
    TaskManager.class.getDeclaredMethod
        ("getTasksWithRecordsLaterThan", Date.class, List.class);

  }


  /**
   * TDD Design Decision: <br/>
   * When at least one of the arguments passed to the method (date, tasks) is null, we want the
   * invocation to fail with an <code>IllegalArgumentException</code>.
   * <br/><br/>
   * Goal: <br/>
   * This test performs three invocations to the method:<br/>
   * <ul>
   * <li>First call: <code>date</code> is null</li>
   * <li>Second call: <code>tasks</code> is null</li>
   * <li>Third call: both <code>date</code> and <code>tasks</code> are null</li>
   * </ul>
   * <br/>
   * We expect the method to fail with an <code>IllegalArgumentException</code> on each call.
   * <br/><br/>
   * Implementation: <br/>
   * Adding the necessary null-checks to the methods makes the test pass. The third invocation
   * with both arguments equal to null could have been removed safely, since the test already
   * fails if only the first argument is null. So covering that doesn't really add value to our
   * tests.
   *
   * @throws Exception
   */
  @Test
  public void testGetTasksWithRecordsLaterThanThrowsExceptionOnNullArgs() throws Exception {
    Method getTasksWithRecordsLaterThan = TaskManager.class.getDeclaredMethod
        ("getTasksWithRecordsLaterThan", Date.class, List.class);
    getTasksWithRecordsLaterThan.setAccessible(true);

    try {
      getTasksWithRecordsLaterThan.invoke(this, null, new LinkedList<>());
      fail("An exception should have been thrown.");
    } catch (InvocationTargetException ex) {
      assertThat(ex.getCause().getClass().equals(IllegalArgumentException.class), equalTo(true));
      assertThat(ex.getCause().getMessage(), equalTo("Argument 'date' must not be null!"));
    }

    try {
      getTasksWithRecordsLaterThan.invoke(this, new Date(), null);
      fail("An exception should have been thrown.");
    } catch (InvocationTargetException ex) {
      assertThat(ex.getCause().getClass().equals(IllegalArgumentException.class), equalTo(true));
      assertThat(ex.getCause().getMessage(), equalTo("Argument 'tasks' must not be null!"));
    }

    try {
      getTasksWithRecordsLaterThan.invoke(this, null, null);
      fail("An exception should have been thrown.");
    } catch (InvocationTargetException ex) {
      assertThat(ex.getCause().getClass().equals(IllegalArgumentException.class), equalTo(true));
      assertThat(ex.getCause().getMessage(), equalTo("Argument 'date' must not be null!"));
    }

  }


  /**
   * TDD Design Decision: <br/>
   * We want <code>getTasksWithRecordsLaterThan</code> to return an empty list if the list passed
   * as an argument is also empty.
   * <br/><br/>
   * Goal: <br/>
   * This test passes and empty task list to the method and verifies it also returns an empty one.
   * <br/><br/>
   * Implementation: <br/>
   * Adding a return statement like <code>return new LinkedList<>()</code> under the section of
   * null-checks makes the test pass.
   *
   * @throws Exception
   */
  @Test
  public void testGetTasksWithRecordsLaterThanReturnsEmptyList() throws Exception {
    Method getTasksWithRecordsLaterThan = TaskManager.class.getDeclaredMethod
        ("getTasksWithRecordsLaterThan", Date.class, List.class);
    getTasksWithRecordsLaterThan.setAccessible(true);

    Object result = getTasksWithRecordsLaterThan.invoke(this, new Date(), new LinkedList<>());

    assertThat(result, notNullValue());
    assertThat(result.getClass().equals(LinkedList.class), equalTo(true));
    assertThat(((LinkedList) result).size(), equalTo(0));
  }

  /**
   * TDD Design Decision: <br/>
   * From the mocked tasks returned by {@link #getTaskList()} we want
   * <code>getTasksWithRecordsLaterThan</code> to return only the tasks which return true when
   * <code>hasRecordsAfter</code> is called on them. The other tasks must not be contained in the
   * list returned by the method.
   * <br/><br/>
   * Goal: <br/>
   * This test passes the task list returned by <code>getTaskList()</code> to the method under
   * test and verifies if it contains exactly two tasks, since only two of the four tasks in the
   * list return true when <code>hasRecordsAfter</code> is invoked on them.
   * <br/><br/>
   * Implementation: <br/>
   * The hard-coded return of an empty list (see previous test case) is replaced by a loop which
   * invokes <code>hasRecordsAfter</code> on every task in the passed list. Every task that
   * returns true is added to the list which is returned after iteration has finished.
   *
   * @throws Exception
   */

  @Test
  public void testGetTasksWithRecordsLaterThanFiltersProperly() throws Exception {
    Method getTasksWithRecordsLaterThan = TaskManager.class.getDeclaredMethod
        ("getTasksWithRecordsLaterThan", Date.class, List.class);
    getTasksWithRecordsLaterThan.setAccessible(true);

    Object result = getTasksWithRecordsLaterThan.invoke(this, testDate, getTaskList());

    assertThat(result, notNullValue());
    assertThat(result.getClass().equals(LinkedList.class), equalTo(true));
    assertThat(((LinkedList) result).size(), equalTo(2));
  }


  @Test
  public void testGetTasksWithRecordsEarlierThanExists() throws Exception {
    TaskManager.class.getDeclaredMethod
        ("getTasksWithRecordsEarlierThan", Date.class, List.class);
  }

  @Test
  public void testGetTasksWithRecordsEarlierThanThrowsExceptionOnNullArgs() throws Exception {
    Method getTasksWithRecordsEarlierThan = TaskManager.class.getDeclaredMethod
        ("getTasksWithRecordsEarlierThan", Date.class, List.class);
    getTasksWithRecordsEarlierThan.setAccessible(true);

    try {
      getTasksWithRecordsEarlierThan.invoke(this, null, new LinkedList<>());
      fail("An exception should have been thrown.");
    } catch (InvocationTargetException ex) {
      assertThat(ex.getCause().getClass().equals(IllegalArgumentException.class), equalTo(true));
      assertThat(ex.getCause().getMessage(), equalTo("Argument 'date' must not be null!"));
    }


    try {
      getTasksWithRecordsEarlierThan.invoke(this, new Date(), null);
      fail("An exception should have been thrown.");
    } catch (InvocationTargetException ex) {
      assertThat(ex.getCause().getClass().equals(IllegalArgumentException.class), equalTo(true));
      assertThat(ex.getCause().getMessage(), equalTo("Argument 'tasks' must not be null!"));
    }

    try {
      getTasksWithRecordsEarlierThan.invoke(this, null, null);
      fail("An exception should have been thrown.");
    } catch (InvocationTargetException ex) {
      assertThat(ex.getCause().getClass().equals(IllegalArgumentException.class), equalTo(true));
      assertThat(ex.getCause().getMessage(), equalTo("Argument 'date' must not be null!"));
    }

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


  private Date testDate = new Date();

  @Override
  public List<Task> getTaskList() {
    List<Task> tasks = new LinkedList<>();
    tasks.add(createTaskWithJustRecordsStartedBeforeNow());
    tasks.add(createTaskWithJustRecordsStartedAfterNow());
    tasks.add(createTaskWithRecordsStartedBeforeAndAfterNow());
    tasks.add(createTaskWithNoRecordsAtAll());

    return tasks;
  }

  private Task createTaskWithJustRecordsStartedBeforeNow() {
    Task task = mock(Task.class);
    when(task.hasRecordsBefore(eq(testDate))).thenReturn(true);
    when(task.hasRecordsAfter(eq(testDate))).thenReturn(false);
    return task;
  }

  private Task createTaskWithJustRecordsStartedAfterNow() {
    Task task = mock(Task.class);
    when(task.hasRecordsBefore(eq(testDate))).thenReturn(false);
    when(task.hasRecordsAfter(eq(testDate))).thenReturn(true);
    return task;
  }

  private Task createTaskWithRecordsStartedBeforeAndAfterNow() {
    Task task = mock(Task.class);
    when(task.hasRecordsBefore(eq(testDate))).thenReturn(true);
    when(task.hasRecordsAfter(eq(testDate))).thenReturn(true);
    return task;
  }

  private Task createTaskWithNoRecordsAtAll() {
    Task task = mock(Task.class);
    when(task.hasRecordsBefore(eq(new Date()))).thenReturn(false);
    when(task.hasRecordsAfter(eq(new Date()))).thenReturn(false);
    return task;
  }


}
