package de.hdmstuttgart.zeitfresser.model;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.lang.reflect.Field;
import java.util.Date;
import java.util.List;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;


/**
 * Created by patrick on 08.11.16.
 */

@SuppressWarnings("unchecked")
public class TaskTest {

  private Task classUnderTest;

  private final int durationOffset = 1000;

  @Rule
  public ExpectedException expectedException = ExpectedException.none();

  @Before
  public void setUp() throws Exception {
    classUnderTest = Task.withName("DummyTask");

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
    Record activeRecord = (Record) activeRecordField.get(classUnderTest);

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
    Record activeRecord = (Record) activeRecordField.get(classUnderTest);

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

  @Test
  public void testAddRecord() throws Exception {
    Record record = Record.withStartAndEnd(new Date(), new Date());
    classUnderTest.addRecord(record);

    Field recordsField = getFieldFromTestClass("records");
    List<Record> records = (List<Record>) recordsField.get(classUnderTest);

    assertThat(records.contains(record), equalTo(true));
  }

  @Test
  public void testTaskIsInitiallyInactive() {
    assertThat(classUnderTest.isActive(), equalTo(false));
  }

  @Test
  public void testTaskIsActiveAfterStartup() {
    classUnderTest.start();

    assertThat(classUnderTest.isActive(), equalTo(true));
  }

  @Test
  public void testActiveRecordIsInitiallyNull() throws Exception {
    Field activeRecord = Task.class.getDeclaredField("activeRecord");
    activeRecord.setAccessible(true);
    Record record = (Record) activeRecord.get(classUnderTest);

    assertThat(record, nullValue());
  }

  @Test
  public void testActiveRecordIsSetAfterStartup() throws Exception {
    classUnderTest.start();

    Field activeRecord = getFieldFromTestClass("activeRecord");
    Record record = (Record) activeRecord.get(classUnderTest);

    assertThat(record, notNullValue());
  }


  @Test
  public void testRecordListContainsActiveRecordAfterStartup() throws Exception {
    classUnderTest.start();

    Field recordList = getFieldFromTestClass("records");
    List<Record> records = (List<Record>) recordList.get(classUnderTest);
    Field activeRecordField = getFieldFromTestClass("activeRecord");
    Record activeRecord = (Record) activeRecordField.get(classUnderTest);

    assertThat(records, notNullValue());
    assertThat(records.contains(activeRecord), equalTo(true));
  }


  @Test(expected = IllegalStateException.class)
  public void testExceptionThrownWhenStartingActiveTask() {
    classUnderTest.start();
    classUnderTest.start();
  }

  @Test(expected = IllegalStateException.class)
  public void testExceptionThrownWhenStoppingInactiveTask() {
    classUnderTest.stop();
  }

  @Test
  public void testActiveRecordIsNullAfterStop() throws Exception {
    classUnderTest.start();
    classUnderTest.stop();

    Field activeRecordField = getFieldFromTestClass("activeRecord");
    Record activeRecord = (Record) activeRecordField.get(classUnderTest);

    assertThat(activeRecord, nullValue());
  }

  @Test
  public void testTaskIsInactiveAfterStop() {
    classUnderTest.start();
    classUnderTest.stop();

    assertThat(classUnderTest.isActive(), equalTo(false));
  }

  @Test
  public void testComputeOverallDuration() throws Exception {
    addDummyRecordWithDuration(1000);
    addDummyRecordWithDuration(2000);
    float duration = classUnderTest.getOverallDuration();

    assertThat(duration, equalTo((float) (3000)));
  }

  private void addDummyRecordWithDuration(int offset) throws NoSuchFieldException,
      IllegalAccessException {
    Date startDate = new Date();
    Date endDate = new Date(startDate.getTime() + offset);
    Record testRecord = Record.withStartAndEnd(startDate, endDate);
    classUnderTest.addRecord(testRecord);
  }


  private Field getFieldFromTestClass(String fieldName) throws NoSuchFieldException {
    if (fieldName != null && !fieldName.isEmpty()) {
      Field field = Task.class.getDeclaredField(fieldName);
      field.setAccessible(true);
      return field;
    }
    throw new IllegalArgumentException("Field name must not be null or empty!");
  }
}