package de.hdmstuttgart.zeitfresser.model;

import android.database.Cursor;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.List;

import de.hdmstuttgart.zeitfresser.db.DbStatements;


/**
 * This is a test class for {@link Task}.
 *
 * @author patrick.kleindienst
 */

@SuppressWarnings("unchecked")
public class TaskTest {

  private Task classUnderTest;

  @Rule
  public ExpectedException expectedException = ExpectedException.none();

  @Before
  public void setUp() throws Exception {
    classUnderTest = Task.withName("DummyTask");
  }

  /**
   * If a new instance is created from the factory method with a DB-Cursor as parameter, the
   * title should fit the one from the DB
   */
  @Test
  public void testFactoryFromCursor() {
    int id = 1;
    String title = "testTitle";

    Cursor cursorMock = mock(Cursor.class);
    when(cursorMock.getColumnIndexOrThrow(DbStatements.COLUMN_NAME_TITLE)).thenReturn(id);
    when(cursorMock.getString(cursorMock.getColumnIndexOrThrow(DbStatements.COLUMN_NAME_TITLE)))
            .thenReturn(title);
    Task task = Task.fromCursor(cursorMock);

    assertEquals(title, task.getName());
  }


  /**
   * If a new task instance has been created, it is expected to be in the following original state:
   * <br/>
   * <ul>
   * <li>Its record list must be empty.</li>
   * <li>It is not in active state.</li>
   * <li>It does not have an active record.</li>
   * <li>It has a valid name different from <em>null</em>.</li>
   * </ul>
   */
  @Test
  public void testInitialTaskState() {
    assertThat("Newly created task must not be in active state!",
        classUnderTest.isActive(), equalTo(false));
    assertThat("Newly created task must not have any records!",
        classUnderTest.hasAnyRecords(), equalTo(false));
    assertThat("Newly created task must not have an active record!",
        classUnderTest.hasActiveRecord(), equalTo(false));
    assertThat("Task name must not be null!", classUnderTest.getName(), notNullValue());
  }


  /**
   * After a newly created task has been started, it is expected to be in following state:
   * <br/>
   * <ul>
   * <li>It must be in active state.</li>
   * <li>Its record list must not be empty and contain exactly one element.</li>
   * <li>It must have an active record.</li>
   * <li>The record in the list must be the same as the active record.</li>
   * </ul>
   */
  @Test
  public void testStateAfterStartingNewlyCreatedTask() throws Exception {
    classUnderTest.start();

    Field recordsField = getFieldFromTestClass("records");
    List<Record> records = (List<Record>) recordsField.get(classUnderTest);

    Field activeRecordField = getFieldFromTestClass("activeRecord");
    final Record activeRecord = (Record) activeRecordField.get(classUnderTest);

    assertThat("Task must be in active state!", classUnderTest.isActive(), equalTo(true));
    assertThat("Task's record list must not be empty!",
        classUnderTest.hasAnyRecords(), equalTo(true));
    assertThat("Record list must contain exactly one element!", records.size(), equalTo(1));
    assertThat("Task must have an active record!", classUnderTest.hasActiveRecord(), equalTo(true));
    assertThat("Active record must be present in records list!",
        records.contains(activeRecord), equalTo(true));
  }


  /**
   * In case an already activated task gets started a second time, an
   * {@link IllegalStateException} is expected to be thrown. Furthermore, the double-activation
   * shall have no undesired side-effects as far as the task's state is concerned. That means
   * that the task is expected to be in following state after being stated twice:
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
        classUnderTest.isActive(), equalTo(true));
    assertThat("Double-activated task must still have records!",
        classUnderTest.hasAnyRecords(), equalTo(true));
    assertThat("Record list must still contain a single element!", records.size(), equalTo(1));
    assertThat("Double-activated task must still have an active record!",
        classUnderTest.hasActiveRecord(), equalTo(true));
    assertThat("Active record must still be present in record list!",
        records.contains(activeRecord), equalTo(true));
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

    assertThat("Stopped task must be inactive!", classUnderTest.isActive(), equalTo(false));
    assertThat("Stopped task must have any records!",
        classUnderTest.hasAnyRecords(), equalTo(true));
    assertThat("Stopped task must have a single record!", records.size(), equalTo(1));
    assertThat("Stopped task must not have an active record!",
        classUnderTest.hasActiveRecord(), equalTo(false));
  }

  /**
   * Any inactive task, which may be a newly created task or a task which has been started and
   * stopped again, is expected to throw an {@link IllegalStateException} if <code>stop()</code>
   * gets called on that task.
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
  public void testStopInactiveTaskThrowsException() throws Exception {
    expectedException.expect(IllegalStateException.class);
    expectedException.expectMessage("Can't stop inactive task");

    classUnderTest.start();
    classUnderTest.stop();
    classUnderTest.stop();

    Field recordsField = getFieldFromTestClass("records");
    List<Record> records = (List<Record>) recordsField.get(classUnderTest);

    assertThat("Inactive task must still be inactive!", classUnderTest.isActive(), equalTo(false));
    assertThat("Inactive task must have any records!",
        classUnderTest.hasAnyRecords(), equalTo(true));
    assertThat("Inactive task must have exactly one record!", records.size(), equalTo(1));
    assertThat("Inactive task must not have an active record!",
        classUnderTest.hasActiveRecord(), equalTo(false));
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
        classUnderTest.isActive(), equalTo(true));
    assertThat("Restarted task must have any records!",
        classUnderTest.hasAnyRecords(), equalTo(true));
    assertThat("Restarted task must exactly have two records!", records.size(), equalTo(2));
    assertThat("Restarted task must have an active record!",
        classUnderTest.hasActiveRecord(), equalTo(true));
    assertThat("Active record must be present in record list!",
        records.contains(activeRecord), equalTo(true));
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
  public void testStopNewlyCreatedTaskCausesException() {
    expectedException.expect(IllegalStateException.class);
    expectedException.expectMessage("Can't stop inactive task.");

    classUnderTest.stop();

    assertThat("Task must be in inactive state!", classUnderTest.isActive(), equalTo(false));
    assertThat("Task must not have any records!", classUnderTest.hasAnyRecords(), equalTo(false));
    assertThat("Task must not have an active record!",
        classUnderTest.hasActiveRecord(), equalTo(false));
  }

  @Test
  public void testTaskHasNoRecordAfter() throws Exception {
    addDummyRecordWithStartDateOffset(-600000L);
    Date currentDate = new Date();

    boolean result = classUnderTest.hasRecordsAfter(currentDate);

    assertThat("Task must not have any records after 'currentDate'!",
        result, equalTo(false));
  }

  @Test
  public void testTaskHasRecordAfter() throws Exception {
    addDummyRecordWithStartDateOffset(600000L);
    Date currentDate = new Date();

    boolean result = classUnderTest.hasRecordsAfter(currentDate);

    assertThat("Task must have any records after 'currentDate'!",
        result, equalTo(true));
  }

  @Test
  public void testTaskHasRecordAfterThrowsExceptionOnNullArgument() {
    expectedException.expect(IllegalArgumentException.class);
    expectedException.expectMessage("Date argument must not be null!");

    classUnderTest.hasRecordsAfter(null);
  }

  @Test
  public void testTaskHasRecordBefore() throws Exception {
    addDummyRecordWithStartDateOffset(-1000000L);
    Date currentDate = new Date();

    boolean result = classUnderTest.hasRecordsBefore(currentDate);

    assertThat("Task must have any record with start date before 'currentDate'!",
        result, equalTo(true));
  }

  @Test
  public void testTaskHasNoRecordBefore() throws Exception {
    addDummyRecordWithStartDateOffset(1000000L);
    Date currentDate = new Date();

    boolean result = classUnderTest.hasRecordsBefore(currentDate);

    assertThat("Task must not have any record with start date before 'currentDate'!",
        result, equalTo(false));
  }

  @Test
  public void testTaskHasRecordsBeforeThrowsExceptionOnNullArgument() throws Exception {
    expectedException.expect(IllegalArgumentException.class);
    expectedException.expectMessage("Date argument must not be null!");

    classUnderTest.hasRecordsBefore(null);
  }

  /**
   * If <code>addRecord</code> gets called with a valid non-null {@link Record} argument,
   * the passed instance is expected to be contained within the task's record list.
   */
  @Test
  public void testAddValidRecord() throws Exception {
    Record recordMock = mock(Record.class);

    Method addRecordMethod = getMethodFromTestClass("addRecord", new Class[]{Record.class});
    addRecordMethod.invoke(classUnderTest, recordMock);

    Field recordsField = getFieldFromTestClass("records");
    List<Record> records = (List<Record>) recordsField.get(classUnderTest);

    assertThat(records.contains(recordMock), equalTo(true));
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

  @Test
  public void testComputeOverallDurationWithRecords() throws Exception {
    addDummyRecordWithDuration(1000);
    addDummyRecordWithDuration(2000);
    float duration = classUnderTest.getOverallDuration();

    assertThat(duration, equalTo(3000f));
  }

  @Test
  public void testComputeOverallDurationWithoutAnyRecord() {
    float duration = classUnderTest.getOverallDuration();

    assertThat(duration, equalTo(0f));
  }

  private void addDummyRecordWithStartDateOffset(long offsetInMillis) throws Exception {
    Date startDate = new Date(System.currentTimeMillis() + offsetInMillis);
    Date endDate = new Date(startDate.getTime() + 1000);
    Record record = Record.withStartAndEnd(startDate, endDate);

    Field recordsField = getFieldFromTestClass("records");
    List<Record> records = (List<Record>) recordsField.get(classUnderTest);
    records.add(record);
    recordsField.set(classUnderTest, records);
  }

  private void addDummyRecordWithDuration(int offset) throws Exception {
    Date startDate = new Date();
    Date endDate = new Date(startDate.getTime() + offset);
    Record testRecord = Record.withStartAndEnd(startDate, endDate);

    Field recordsField = getFieldFromTestClass("records");
    List<Record> records = (List<Record>) recordsField.get(classUnderTest);
    records.add(testRecord);
    recordsField.set(classUnderTest, records);
  }


  private Field getFieldFromTestClass(String fieldName) throws NoSuchFieldException {
    if (fieldName != null && !fieldName.isEmpty()) {
      Field field = Task.class.getDeclaredField(fieldName);
      field.setAccessible(true);
      return field;
    }
    throw new IllegalArgumentException("Field name must not be null or empty!");
  }

  private Method getMethodFromTestClass(String methodName, Class<?>[] params) throws
      NoSuchMethodException {
    if (methodName != null && !methodName.isEmpty()) {
      Method method = Task.class.getDeclaredMethod(methodName, params);
      method.setAccessible(true);
      return method;
    }
    throw new IllegalArgumentException("Method name must not be null or empty!");
  }
}