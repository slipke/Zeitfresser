package de.hdmstuttgart.zeitfresser.model;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;

import org.junit.Before;
import org.junit.Test;

import java.util.Collections;
import java.util.LinkedList;

/**
 * An approach for testing the equals() method is splitting up the potential arguments into
 * equivalence classes:
 * <br/>
 * <ul>
 * <li>Equivalence class 1: null ( -> false)</li>
 * <li>Equivalence class 2: this ( -> true)</li>
 * <li>Equivalence class 3: <code>!(other instanceof Task)</code> ( -> false)</li>
 * <li>Equivalence class 4: <code>this.getId() != other.getId()</code> ( -> false)</li>
 * <li>Equivalence class 5: <code>!this.getName().equals(other.Name())</code> ( -> false)</li>
 * <li>Equivalence class 6:<code>!this.getRecords().equals(other.getRecords())</code>
 * ( -> false)</li>
 * <li>Equivalence class 7: Task objects with identical attributes as test task ( -> true)</li>
 * </ul>
 * <br/>
 * By categorizing the different types of arguments that way, we can also make sure that every
 * if-branch in our equals() method is entered at least once and we therefore get a high coverage.
 * <br/>
 * <br/>
 * For testing hashcode(), we need two Task instances and four test cases: One test case where two
 * identical Task objects must produce the same hashcode, and three tests where the two objects
 * differ in at least a single property value. In that case, the hashcodes must not be equal.
 *
 * @author patrick.kleindienst
 */
public class TaskEqualsHashcodeTest extends TaskBaseTest {

  private Task otherTask;

  @Override
  @Before
  public void setUp() throws Exception {
    super.setUp();

    otherTask = Task.withName("otherTask");
    otherTask.id = classUnderTest.getId();
    otherTask.name = classUnderTest.getName();
    otherTask.active = classUnderTest.isActive();
    otherTask.records = classUnderTest.records;
    otherTask.activeRecord = classUnderTest.getActiveRecord();
  }

  /*
      ### EQUALS ###
   */

  /**
   * Test case for equivalence class 1.
   */
  @Test
  public void testEqualsReturnsFalseOnNullArg() {
    boolean equals = classUnderTest.equals(null);

    assertThat(equals, is(false));
  }

  /**
   * Test case for equivalence class 2.
   */
  @Test
  public void testEqualsReturnsTrueOnSelfArg() {
    boolean equals = classUnderTest.equals(classUnderTest);

    assertThat(equals, is(true));
  }

  /**
   * Test case for equivalence class 3.
   */
  @Test
  public void testEqualsReturnsFalseOnNoTaskArg() {
    boolean equals = classUnderTest.equals("Not a task");

    assertThat(equals, is(false));
  }

  /**
   * Test case for equivalence class 4.
   */
  @Test
  public void testEqualsReturnsFalseOnDifferentId() {
    otherTask.id++;
    boolean equals = classUnderTest.equals(otherTask);

    assertThat(equals, is(false));
  }

  /**
   * Test case for equivalence class 5.
   */
  @Test
  public void testEqualsReturnsFalseOnDifferentName() {
    otherTask.name = "Other Name";
    boolean equals = classUnderTest.equals(otherTask);

    assertThat(equals, is(false));
  }

  /**
   * Test case for equivalence class 6.
   * This one is a bit tricky, since <code>equals()</code> cannot be mocked by Mockito. We
   * therefore create two lists, from which one is empty and the
   * other one contains a mocked record.
   */
  @Test
  public void testEqualsReturnsFalseOnDifferentRecords() {
    otherTask.records = new LinkedList<>();
    classUnderTest.records = Collections.singletonList(mock(Record.class));

    boolean equals = classUnderTest.equals(otherTask);

    assertThat(equals, is(false));
  }

  /**
   * Test case for equivalence class 7.
   */
  @Test
  public void testEqualsReturnsTrueOnIdenticalTasks() {
    boolean equals = classUnderTest.equals(otherTask);

    assertThat(equals, is(true));
  }


  /*
      ### HASHCODE ###
   */


  /**
   * Verify that two identical task objects produce identical hashcodes.
   */
  @Test
  public void testIdenticalTasksProduceSameHashcode() {
    int hashCodeA = classUnderTest.hashCode();
    int hashCodeB = otherTask.hashCode();

    assertThat(hashCodeA, equalTo(hashCodeB));
  }

  /**
   * Verify that two tasks with different IDs produce different hashcodes.
   */
  @Test
  public void testTasksWithDifferentIdsProduceDifferentHashcodes() {
    int hashCodeA = classUnderTest.hashCode();
    otherTask.id++;
    int hashCodeB = otherTask.hashCode();

    assertThat(hashCodeA, not(equalTo(hashCodeB)));
  }

  /**
   * Verify that two tasks with different names produce different hashcodes.
   */
  @Test
  public void testTasksWithDifferentNamesProduceDifferentHashcodes() {
    int hashCodeA = classUnderTest.hashCode();
    otherTask.name = "Other Name";
    int hashCodeB = otherTask.hashCode();

    assertThat(hashCodeA, not(equalTo(hashCodeB)));
  }

  /**
   * Verify that two tasks with different records produce different hashcodes.
   */
  @Test
  public void testTasksWithDifferentRecordsProduceDifferentHashcodes() {
    classUnderTest.records = Collections.singletonList(mock(Record.class));
    int hashCodeA = classUnderTest.hashCode();

    otherTask.records = Collections.emptyList();
    int hashCodeB = otherTask.hashCode();

    assertThat(hashCodeA, not(equalTo(hashCodeB)));
  }
}
