package de.hdmstuttgart.zeitfresser.model;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by patrick on 22.02.17.
 */

public class TaskManagerGetTasksWithRecordsLaterThanTest extends TaskManagerBaseTest {

  private Method getTasksWithRecordsLaterThan;

  @Before
  public void setUp() throws Exception {
    getTasksWithRecordsLaterThan = TaskManager.class.getDeclaredMethod
        ("getTasksWithRecordsLaterThan", Date.class, List.class);

    if (getTasksWithRecordsLaterThan != null) {
      getTasksWithRecordsLaterThan.setAccessible(true);
    }
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
    // TODO Split up into several test cases.
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
    Object result = getTasksWithRecordsLaterThan.invoke(this, testDate, getTaskList());

    assertThat(result, notNullValue());
    assertThat(result.getClass().equals(LinkedList.class), equalTo(true));
    assertThat(((LinkedList) result).size(), equalTo(2));
  }

}
