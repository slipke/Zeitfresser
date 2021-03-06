package de.hdmstuttgart.zeitfresser.model;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;

import org.junit.Test;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.List;


/**
 * This is a test class for {@link Task} which focuses on validating the expected behavior on any
 * internal state transitions.
 *
 * @author patrick.kleindienst
 */

@SuppressWarnings("unchecked")
public class TaskStateTest extends TaskBaseTest {


  /*
      These are the state tests following the state diagram we describe in our documentation.
      Depending on the current state of a task, they verify if after a certain method call, the
      task is in the expected state.
   */

  /**
   * If a new task instance has been created, it is expected to be in the following initial state:
   * <br/>
   * <ul>
   * <li>Its record list must be empty.</li>
   * <li>It is not in active state.</li>
   * <li>It does not have an active record.</li>
   * <li>It has a valid name different from <code>null</code>.</li>
   * </ul>
   */
  @Test
  public void testInitialTaskState() {
    assertThat("Newly created task must not be in active state!",
        classUnderTest.isActive(), is(false));
    assertThat("Newly created task must not have any records!",
        classUnderTest.hasRecords(), is(false));
    assertThat("Newly created task must not have an active record!",
        classUnderTest.hasActiveRecord(), is(false));
    assertThat("Task name must not be null!", classUnderTest.getName(), notNullValue());
  }


  /**
   * After a newly created task has been started, it is expected to be in following state:
   * <br/>
   * <ul>
   * <li>It must be in active state.</li>
   * <li>Its record list must not be empty and contain exactly one element.</li>
   * <li>It must have an active record.</li>
   * <li>The record in the list must be equal to the active record.</li>
   * </ul>
   */
  @Test
  public void testStateAfterTaskStartedForFistTime() throws Exception {
    classUnderTest.start();

    Field recordsField = getFieldFromTestClass("records");
    final List<Record> records = (List<Record>) recordsField.get(classUnderTest);

    Field activeRecordField = getFieldFromTestClass("activeRecord");
    final Record activeRecord = (Record) activeRecordField.get(classUnderTest);

    assertThat("Task must be in active state!", classUnderTest.isActive(), is(true));
    assertThat("Task's record list must not be empty!",
        classUnderTest.hasRecords(), is(true));
    assertThat("Record list must contain exactly one element!", records.size(), is(1));
    assertThat("Task must have an active record!", classUnderTest.hasActiveRecord(), is(true));
    assertThat("Active record must be present in records list!",
        records.contains(activeRecord), is(true));
  }


  /**
   * In case an already activated task gets started a second time, an
   * {@link IllegalStateException} is expected to be thrown. Furthermore, the double-activation
   * shall have no undesired side-effects as far as the task's state is concerned. That means
   * that the task is expected to be in following state after having been stated twice:
   * <br/>
   * <ul>
   * <li>It must be in active state.</li>
   * <li>Its record list must not be empty and contain exactly one element.</li>
   * <li>It must have an active record.</li>
   * <li>The record in the list must be the same as the active record.</li>
   * </ul>
   */
  @Test
  public void testStartAlreadyActiveTaskThrowsException() throws Exception {
    expectedException.expect(IllegalStateException.class);
    expectedException.expectMessage("Task has already been started!");

    classUnderTest.start();
    classUnderTest.start();

    Field recordsField = getFieldFromTestClass("records");
    List<Record> records = (List<Record>) recordsField.get(classUnderTest);

    Field activeRecordField = getFieldFromTestClass("activeRecord");
    final Record activeRecord = (Record) activeRecordField.get(classUnderTest);

    assertThat("Double-activated task must still be active!",
        classUnderTest.isActive(), is(true));
    assertThat("Double-activated task must still have records!",
        classUnderTest.hasRecords(), is(true));
    assertThat("Record list must still contain a single element!", records.size(), is(1));
    assertThat("Double-activated task must still have an active record!",
        classUnderTest.hasActiveRecord(), is(true));
    assertThat("Active record must still be present in record list!",
        records.contains(activeRecord), is(true));
  }

  /**
   * Once a task has been started and stopped properly after a while, it is expected to be in the
   * following state:
   * <br/>
   * <ul>
   * <li>It must be in inactive state.</li>
   * <li>It must at least contain a single record.</li>
   * <li>It must not have an active record.</li>
   * </ul>
   */
  @Test
  public void testStopActiveTask() throws Exception {
    classUnderTest.start();
    classUnderTest.stop();

    Field recordsField = getFieldFromTestClass("records");
    List<Record> records = (List<Record>) recordsField.get(classUnderTest);

    assertThat("Stopped task must be inactive!", classUnderTest.isActive(), is(false));
    assertThat("Stopped task must have any records!",
        classUnderTest.hasRecords(), is(true));
    assertThat("Stopped task must have a single record!", records.size(), is(1));
    assertThat("Stopped task must not have an active record!",
        classUnderTest.hasActiveRecord(), is(false));
  }

  /**
   * Any inactive task, which has been started and then stopped, is expected to throw an
   * {@link IllegalStateException} if <code>stop() </code> gets called on that task.
   * Furthermore, the task is expected to be in exactly the same state as before the exception
   * occurred:
   * <br/>
   * <ul>
   * <li>It must be in inactive state.</li>
   * <li>Its record list is in the same state as before.</li>
   * <li>It is not allowed to have an active record.</li>
   * </ul>
   */
  @Test
  public void testStopAlreadyStoppedTaskThrowsException() throws Exception {
    expectedException.expect(IllegalStateException.class);
    expectedException.expectMessage("Can't stop inactive task");

    classUnderTest.start();
    classUnderTest.stop();
    classUnderTest.stop();

    Field recordsField = getFieldFromTestClass("records");
    List<Record> records = (List<Record>) recordsField.get(classUnderTest);

    assertThat("Inactive task must still be inactive!", classUnderTest.isActive(), is(false));
    assertThat("Inactive task must have any records!",
        classUnderTest.hasRecords(), is(true));
    assertThat("Inactive task must have exactly one record!", records.size(), is(1));
    assertThat("Inactive task must not have an active record!",
        classUnderTest.hasActiveRecord(), is(false));
  }


  /**
   * A newly created task which has never been started is expected to throw an {@link
   * IllegalStateException} if <code>stop()</code> is called on it. Additionally, the task's state
   * is expected to be the same as its initial state:
   * <br/>
   * <ul>
   * <li>It is in inactive state.</li>
   * <li>Its record list is empty.</li>
   * <li>It does not have an active record.</li>
   * </ul>
   */
  @Test
  public void testStopNeverStartedTaskThrowsException() {
    expectedException.expect(IllegalStateException.class);
    expectedException.expectMessage("Can't stop inactive task.");

    classUnderTest.stop();

    assertThat("Task must be in inactive state!", classUnderTest.isActive(), is(false));
    assertThat("Task must not have any records!", classUnderTest.hasRecords(), is(false));
    assertThat("Task must not have an active record!",
        classUnderTest.hasActiveRecord(), is(false));
  }

  /**
   * Any task, which has already been started and subsequently been stopped, is expected to
   * be in the following state after restart:
   * <br/>
   * <ul>
   * <li>It must be back in active state.</li>
   * <li>It must contain one more record in its list.</li>
   * <li>It must have an active record.</li>
   * <li>The active record must be the same as the new element in the record list.</li>
   * </ul>
   */
  @Test
  public void testRestartPreviouslyStoppedTask() throws Exception {
    classUnderTest.start();
    classUnderTest.stop();
    classUnderTest.start();

    Field recordsField = getFieldFromTestClass("records");
    List<Record> records = (List<Record>) recordsField.get(classUnderTest);

    Field activeRecordField = getFieldFromTestClass("activeRecord");
    final Record activeRecord = (Record) activeRecordField.get(classUnderTest);

    assertThat("Restarted task must be back in active state!",
        classUnderTest.isActive(), is(true));
    assertThat("Restarted task must have any records!",
        classUnderTest.hasRecords(), is(true));
    assertThat("Restarted task must exactly have two records!", records.size(), equalTo(2));
    assertThat("Restarted task must have an active record!",
        classUnderTest.hasActiveRecord(), is(true));
    assertThat("Active record must be present in record list!",
        records.contains(activeRecord), is(true));
  }


  /*
      The possible arguments for the addRecord() method can be categorized into two equivalence
      classes:

      1) Any record object different from null
      2) null

      The expected behavior for arguments of type 1 is that they're added to the list of records
      hold by a task.
      The expected behavior for arguments of type 2 is that an IllegalArgumentException is thrown.
   */


  /**
   * If <code>addRecord</code> gets called with a valid non-null {@link Record} argument,
   * the passed instance is expected to be contained within the task's record list.
   */
  @Test
  public void testAddValidRecord() throws Exception {
    Record recordMock = mock(Record.class);

    classUnderTest.addRecord(recordMock);

    Field recordsField = getFieldFromTestClass("records");
    List<Record> records = (List<Record>) recordsField.get(classUnderTest);
    assertThat(records.contains(recordMock), is(true));
  }

  /**
   * if <code>addRecord</code> is called with <code>null</code> as argument, the method is
   * expected to throw an {@link IllegalArgumentException}.
   * Additionally, the task's record list is expected to be not modified during the call.
   */
  @Test
  public void testAddNullToRecordsThrowsException() throws Exception {
    Method addRecordMethod = getMethodFromTestClass("addRecord", new Class[]{Record.class});

    try {
      addRecordMethod.invoke(classUnderTest, new Object[]{null});
    } catch (InvocationTargetException ex) {
      assertThat(ex.getTargetException().getClass().equals(IllegalArgumentException.class),
          equalTo(true));
      assertThat(ex.getTargetException().getMessage(),
          equalTo("Record argument must not be " + "null!"));
    }

    Field recordsField = getFieldFromTestClass("records");
    List<Record> records = (List<Record>) recordsField.get(classUnderTest);

    assertThat(records.size(), equalTo(0));
  }

}