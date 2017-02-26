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
 * A test class for {@link TaskManager#getTasksWithRecordsEarlierThan(Date, List)}.
 *
 * @author patrick.kleindienst
 */

public class TaskManagerGetTasksWithRecordsEarlierThanTest extends TaskManagerBaseTest {

  private Method getTasksWithRecordsEarlierThan;


  /**
   * Performing necessary setup.
   *
   * @throws Exception if reflection call fails.
   */
  @Before
  public void setUp() throws Exception {
    getTasksWithRecordsEarlierThan = TaskManager
            .class
            .getDeclaredMethod("getTasksWithRecordsEarlierThan", Date.class, List.class);

    if (getTasksWithRecordsEarlierThan != null) {
      getTasksWithRecordsEarlierThan.setAccessible(true);
    }
  }

  /**
   * <h3>Requirement:</h3>
   * When the first argument (date) is null, we want the invocation to fail with an
   * {@link IllegalArgumentException}.
   * <br/>
   * <h3>Goal:</h3>
   * Write a test which fails with the expected exception when <code>null</code> is passed as
   * first argument.
   * <br/>
   * <h3>Implementation:</h3>
   * Use an if-statement which checks if date == null. If this condition evaluates to
   * <code>true</code>, throw an <code>IllegalArgumentException</code>.
   * <br/><br/>
   *
   * @throws Exception if reflection call fails.
   */
  @Test
  public void testGetTasksWithRecordsEarlierThanThrowsExceptionFirstArgIsNull() throws Exception {
    try {
      getTasksWithRecordsEarlierThan.invoke(this, null, new LinkedList<>());
      fail("An exception should have been thrown.");
    } catch (InvocationTargetException ex) {
      assertException(ex.getCause(), IllegalArgumentException.class,
          "Argument 'date' must not be null!");
    }
  }

  /**
   * <h3>Requirement:</h3>
   * If the second argument (list of tasks to filter) is <code>null</code>, the invocation must
   * fail with an {@link IllegalArgumentException}.
   * <br/>
   * <h3>Goal:</h3>
   * Write a test case which fails if the method is called with a second argument equal to null.
   * <br/>
   * <h3>Implementation:</h3>
   * Use another if-statement which checks if the second argument is null. If <code>true</code>,
   * throw an <code>IllegalArgumentException</code>.
   * <br/><br/>
   *
   * @throws Exception if reflection call fails.
   */
  @Test
  public void testGetTasksWithRecordsEarlierThanThrowsExceptionSecondArgIsNull() throws Exception {
    try {
      getTasksWithRecordsEarlierThan.invoke(this, new Date(), null);
      fail("An exception should have been thrown.");
    } catch (InvocationTargetException ex) {
      assertException(ex.getCause(), IllegalArgumentException.class,
          "Argument 'tasks' must not be null!");
    }
  }

  /**
   * <h3>Requirement:</h3>
   *  If both arguments are <code>null</code>, an {@link IllegalArgumentException} is thrown. We
   *  added this test case only for completeness, since this requirement is already covered by
   *  the previous test cases and therefore not really necessary.
   *  <br/>
   *  <h3>Goal:</h3>
   *  Write a test which fails if both arguments are null.
   *  <br/>
   *  <h3>Implementation:</h3>
   *  Not needed, since existing implementation already covers that.
   *  <br/><br/>
   *
   * @throws Exception if reflection call fails.
   */
  @Test
  public void testGetTasksWithRecordsEarlierThanThrowsExceptionBothArgsAreNull() throws
      Exception {
    try {
      getTasksWithRecordsEarlierThan.invoke(this, null, null);
      fail("An exception should have been thrown.");
    } catch (InvocationTargetException ex) {
      assertException(ex.getCause(), IllegalArgumentException.class,
          "Argument 'date' must not be null!");
    }
  }


  /**
   * <h3>Requirement:</h3>
   * If an empty task list is provided as second argument, the method must return an empty list
   * itself since there's nothing to filter.
   * <br/>
   * <h3>Goal:</h3>
   * Create a test case which checks if an emtpy list is returned in that case.
   * <br/>
   * <h3>Implementation:</h3>
   * Add a return statement after the null-checks which statically returns an empty list.
   * <br/><br/>
   *
   * @throws Exception if reflection call fails.
   */
  @Test
  public void testGetTasksWithRecordsEarlierThanReturnsEmptyList() throws Exception {
    Object result = getTasksWithRecordsEarlierThan.invoke(this, new Date(), new LinkedList<>());

    assertThat(result, notNullValue());
    assertThat(result.getClass().equals(LinkedList.class), equalTo(true));
    assertThat(((LinkedList) result).isEmpty(), equalTo(true));
  }

  /**
   * <h3>Requirement:</h3>
   * If a valid date as well as a non-empty list are given as argument, we expect the method to
   * filter out all tasks which don't have any records starting before the given date.
   * <br/>
   * <h3>Goal:</h3>
   * Implement a test case which checks if only the expected tasks are returned.
   * <br/>
   * <h3>Implementation:</h3>
   * Statically return an empty list is not appropriate any more. Iterate over all tasks in the
   * list provided as argument and only collect these tasks which have at least a single record
   * that starts earlier than the given date.
   * <br/><br/>
   *
   * @throws Exception if reflection call fails.
   */
  @Test
  public void testGetTasksWithRecordsEarlierThanFiltersProperly() throws Exception {
    Object result = getTasksWithRecordsEarlierThan.invoke(this, testUntilDate, getTaskList());

    assertThat(result, notNullValue());
    assertThat(result.getClass().equals(LinkedList.class), equalTo(true));
    assertThat(((LinkedList) result).size(), equalTo(4));
  }

}
