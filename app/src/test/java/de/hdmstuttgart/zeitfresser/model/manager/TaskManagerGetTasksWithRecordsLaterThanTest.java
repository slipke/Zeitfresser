package de.hdmstuttgart.zeitfresser.model.manager;

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
 * A test class for {@link TaskManager#getTasksWithRecordsLaterThan(Date, List)}.
 *
 * @author patrick.kleindienst
 */

public class TaskManagerGetTasksWithRecordsLaterThanTest extends TaskManagerBaseTest {

  private Method getTasksWithRecordsLaterThan;

  /**
   * Setup the test.
   *
   * @throws Exception Exception
   */
  @Before
  public void setUp() throws Exception {
    getTasksWithRecordsLaterThan = TaskManager
        .class
        .getDeclaredMethod("getTasksWithRecordsLaterThan", Date.class, List.class);

    if (getTasksWithRecordsLaterThan != null) {
      getTasksWithRecordsLaterThan.setAccessible(true);
    }
  }

  /**
   * <h3>Requirement:</h3>
   * If first argument (date) is <code>null</code>, invocation must fail with an
   * {@link IllegalArgumentException}.
   * <br/>
   * <h3>Goal:</h3>
   * Write a test case which passes null as first argument and expects an exception to be thrown.
   * <br/>
   * <h3>Implementation:</h3>
   * Add an if statement which checks if first argument is null and throws an
   * <code>IllegalArgumentException</code> if the condition is true.
   * <br/>br/>
   *
   * @throws Exception if reflection call fails.
   */
  @Test
  public void testGetTasksWithRecordsLaterThanThrowsExceptionFirstArgNull() throws Exception {
    try {
      getTasksWithRecordsLaterThan.invoke(this, null, new LinkedList<>());
      fail("An exception should have been thrown.");
    } catch (InvocationTargetException ex) {
      assertException(ex.getCause(), IllegalArgumentException.class,
          "Argument 'date' must not be null!");
    }
  }


  /**
   * <h3>Requirement:</h3>
   * If second argument is <code>null</code>, we expect that an {@link IllegalArgumentException}
   * is thrown.
   * <br/>
   * <h3>Goal:</h3>
   * Write a test case that invokes the method with a second argument equal to null and checks if
   * the expected exception occurs.
   * <br/>
   * <h3>Implementation:</h3>
   * Add another if statement which checks if the second argument is null and throws the expected
   * exception in case that condition is true.
   * <br/><br/>
   *
   * @throws Exception if reflection call fails.
   */
  @Test
  public void testGetTasksWithRecordsLaterThanThrowsExceptionSecondArgNull() throws Exception {
    try {
      getTasksWithRecordsLaterThan.invoke(this, new Date(), null);
      fail("An exception should have been thrown.");
    } catch (InvocationTargetException ex) {
      assertException(ex.getCause(), IllegalArgumentException.class,
          "Argument 'tasks' must not be null!");
    }
  }

  /**
   * <h3>Requirement:</h3>
   * When at least one of the arguments passed to the method (date, tasks) is null, we want the
   * invocation to fail with an <code>IllegalArgumentException</code>.
   * <br/>
   * <h3>Goal:</h3>
   * This test performs three invocations to the method:<br/>
   * <ul>
   * <li>First call: <code>date</code> is null</li>
   * <li>Second call: <code>tasks</code> is null</li>
   * <li>Third call: both <code>date</code> and <code>tasks</code> are null</li>
   * </ul>
   * <br/>
   * We expect the method to fail with an <code>IllegalArgumentException</code> on each call.
   * <br/>
   * <h3>Implementation:</h3>
   * Adding the necessary null-checks to the methods makes the test pass. The third invocation
   * with both arguments equal to null could have been removed safely, since the test already
   * fails if only the first argument is null. So covering that doesn't really add value to our
   * tests.
   * <br/><br/>
   *
   * @throws Exception if reflection call fails.
   */
  @Test
  public void testGetTasksWithRecordsLaterThanThrowsExceptionBothArgsNull() throws Exception {
    try {
      getTasksWithRecordsLaterThan.invoke(this, null, null);
      fail("An exception should have been thrown.");
    } catch (InvocationTargetException ex) {
      assertException(ex.getCause(), IllegalArgumentException.class,
          "Argument 'date' must not be null!");
    }
  }


  /**
   * <h3>Requirement:</h3>
   * We want <code>getTasksWithRecordsLaterThan</code> to return an empty list if the list passed
   * as an argument is also empty.
   * <br/>
   * <h3>Goal:</h3>
   * This test passes and empty task list to the method and verifies it also returns an empty one.
   * <br/>
   * <h3>Implementation:</h3>
   * Adding a return statement like <code>return new LinkedList<>()</code> under the section of
   * null-checks makes the test pass.
   *<br/><br/>
   *
   * @throws Exception if reflection call fails.
   */
  @Test
  public void testGetTasksWithRecordsLaterThanReturnsEmptyList() throws Exception {
    Object result = getTasksWithRecordsLaterThan.invoke(this, new Date(), new LinkedList<>());

    assertThat(result, notNullValue());
    assertThat(result.getClass().equals(LinkedList.class), equalTo(true));
    assertThat(((LinkedList) result).size(), equalTo(0));
  }

  /**
   * <h3>Requirement:</h3>
   * From the mocked tasks returned by {@link #getTaskList()} we want
   * <code>getTasksWithRecordsLaterThan</code> to return only the tasks which return true when
   * <code>hasRecordsAfter</code> is called on them. The other tasks must not be contained in the
   * list returned by the method.
   * <br/>
   * <h3>Goal:</h3>
   * This test passes the task list returned by <code>getTaskList()</code> to the method under
   * test and verifies if it contains exactly two tasks, since only two of the four tasks in the
   * list return true when <code>hasRecordsAfter</code> is invoked on them.
   * <br/>
   * <h3>Implementation:</h3>
   * The hard-coded return of an empty list (see previous test case) is replaced by a loop which
   * invokes <code>hasRecordsAfter</code> on every task in the passed list. Every task that
   * returns true is added to the list which is returned after iteration has finished.
   * <br/><br/>
   *
   * @throws Exception if reflection call fails.
   */
  @Test
  public void testGetTasksWithRecordsLaterThanFiltersProperly() throws Exception {
    Object result = getTasksWithRecordsLaterThan.invoke(this, testFromDate, getTaskList());

    assertThat(result, notNullValue());
    assertThat(result.getClass().equals(LinkedList.class), equalTo(true));
    assertThat(((LinkedList) result).size(), equalTo(4));
  }

}
