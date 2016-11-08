package de.hdmstuttgart.zeitfresser.model;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Field;
import java.util.Date;
import java.util.List;




/**
 * Created by patrick on 08.11.16.
 */

@SuppressWarnings("unchecked")
public class TaskTest {

  private Task classUnderTest;

  private final int durationOffset = 1000;

  @Before
  public void setUp() throws Exception {
    classUnderTest = Task.withName("DummyTask");

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