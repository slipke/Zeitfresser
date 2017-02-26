package de.hdmstuttgart.zeitfresser.model.manager;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import de.hdmstuttgart.zeitfresser.model.Task;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;

/**
 * This is a test class for {@link TaskManager#filterZeroDurationTasks(List)} method. This
 * method's productive implementation has been developed by applying the TDD paradigm.
 *
 * @author patrick.kleindienst
 */

public class TaskManagerFilterZeroDurationTasksTest extends TaskManagerBaseTest {


  private Method filterZeroDurationTasks;

  /**
   * Doing setup.
   *
   * @throws Exception if reflection call fails.
   */
  @Before
  public void setUp() throws Exception {
    filterZeroDurationTasks = TaskManager.class.getDeclaredMethod(
        "filterZeroDurationTasks", List.class);

    if (filterZeroDurationTasks != null) {
      filterZeroDurationTasks.setAccessible(true);
    }
  }


  /**
   * <h3>Requirement:</h3>
   * If <code>null</code> is passed to <code>filterZeroDurationTasks</code>, an
   * {@link IllegalArgumentException} shall be thrown.
   * <br/>
   * <h3>Goal:</h3>
   * This test invokes the method with a null argument and checks if the expected exception
   * is actually thrown. It fails if no exception has been thrown or the exception is not an
   * <code>IllegalArgumentException.</code>
   * <br/>
   * <h3>Implementation:</h3>
   * We add a check to the method body which compares the passed list to <code>null</code>. If
   * the condition evaluates to true (argument is <code>null</code>), an
   * <code>IllegalArgumentException</code> is thrown.
   * <br/><br/>
   *
   * @throws Exception if reflection call fails.
   */
  @Test
  public void testFilterZeroDurationTasksFailsOnNullArg() throws Exception {
    try {
      filterZeroDurationTasks.invoke(this, new Object[]{null});
      fail("An exception should have been thrown.");
    } catch (InvocationTargetException ex) {
      assertThat(ex.getCause().getClass().equals(IllegalArgumentException.class), is(true));
    }
  }


  /**
   * <h3>Requirement:</h3>
   * If an empty task list is passed to <code>filterZeroDurationTasks</code>, we expect the
   * method to do nothing but return an empty list itself.
   * <br/>
   * <h3>Goal:</h3>
   * This test invokes the method with an empty task list as an argument and verifies that an
   * empty list is returned.
   * <br/>
   * <h3>Implementation:</h3>
   * Inside the else-branch, which is empty at this stage of development, add a simple return
   * statement that returns an empty list, like <code>return new LinkedList<>()</code>.
   * <br/><br/>
   *
   * @throws Exception if reflection call fails.
   */
  @Test
  public void testFilterZeroDurationTasksReturnsEmptyList() throws Exception {
    Object result = filterZeroDurationTasks.invoke(this, new LinkedList<>());

    assertThat(result.getClass().equals(LinkedList.class), is(true));
    assertThat(((LinkedList) result).size(), is(0));
  }


  /**
   * <h3>Requirement:</h3>
   * If a task list with size > 0 is passed as an argument to <code>filterZeroDurationTasks</code>,
   * every task in the list which has an overall duration equal to null is filtered by this method.
   * We therefore expect it to return a list of tasks which contains all the tasks from the input
   * list whose overall duration is > 0.
   * <br/>
   * <h3>Goal:</h3>
   * This test creates a list containing to mocked task objects, of which one of them returns 0
   * if <code>getOverallDuration()</code> is called, whilst the other one returns a value greater
   * than 0. As specified, we expect the method to return a list which only contains the second
   * task mock object.
   * <br/>
   * <h3>Implementation:</h3>
   * To make the test pass, we replace the <code>return new LinkedList<>()</code> statement by a
   * loop which iterates over all tasks in the argument list and only adds the current task to
   * the output list if its returns an overall duration > 0. After the iteration over the input
   * list is done, the output list is returned.
   * <br/><br/>
   *
   * @throws Exception if reflection call fails.
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

    Object result = filterZeroDurationTasks.invoke(this, tasks);

    assertThat(result, notNullValue());
    assertThat(result.getClass().equals(LinkedList.class), equalTo(true));
    assertThat(((LinkedList) result).size(), not(equalTo(0)));

    // Dummy Task 2 must be in the list, since its duration is > 0.
    assertThat(((LinkedList) result).contains(dummyTask2), equalTo(true));

    // Dummy Task 2 must not be in the list, since its duration is 0.
    assertThat(((LinkedList) result).contains(dummyTask1), equalTo(false));
  }


}
