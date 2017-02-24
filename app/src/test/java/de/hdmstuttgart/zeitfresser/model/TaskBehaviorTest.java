package de.hdmstuttgart.zeitfresser.model;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import android.database.Cursor;

import de.hdmstuttgart.zeitfresser.db.DbStatements;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedList;

/**
 * A test class for {@link Task} that focuses on behavior tests.
 *
 * @author patrick.kleindienst
 */

public class TaskBehaviorTest extends TaskBaseTest {

  private Date testDate;

  private Record testRecordA;
  private Record testRecordB;

  @Override
  @Before
  public void setUp() throws Exception {
    super.setUp();

    testDate = new Date();
    testRecordA = mock(Record.class);
    testRecordB = mock(Record.class);


    Field records = getFieldFromTestClass("records");
    records.set(classUnderTest, Arrays.asList(testRecordA, testRecordB));
  }

  /**
   * Verify that {@link Task#hasRecordsAfter(Date)} returns <code>false</code> for
   *  a task which has no record starting after <code>testDate</code>.
   */
  @Test
  public void testTaskHasNoRecordAfter() {
    when(testRecordA.getStart()).thenReturn(new Date(testDate.getTime()));
    when(testRecordB.getStart()).thenReturn(new Date(testDate.getTime()));

    boolean result = classUnderTest.hasRecordsAfter(testDate);

    assertThat("Task must not have any records after 'currentDate'!",
        result, is(false));
  }

  /**
   * Verify that {@link Task#hasRecordsAfter(Date)} returns <code>true</code> for
   *  a task which has at least a single record starting after <code>testDate</code>.
   */
  @Test
  public void testTaskHasRecordAfter() throws Exception {
    when(testRecordA.getStart()).thenReturn(new Date(testDate.getTime() + 1000));
    when(testRecordB.getStart()).thenReturn(new Date(testDate.getTime()));

    boolean result = classUnderTest.hasRecordsAfter(testDate);

    assertThat("Task must have any records after 'currentDate'!",
        result, is(true));
  }


  /**
   * Verify that {@link Task#hasRecordsAfter(Date)} throws an {@link IllegalArgumentException}
   * in case <code>null</code> is passed in as an argument.
   */
  @Test
  public void testTaskHasRecordAfterThrowsExceptionOnNullArgument() {
    expectedException.expect(IllegalArgumentException.class);
    expectedException.expectMessage("Argument for param 'date' must not be null!");

    classUnderTest.hasRecordsAfter(null);
  }


  /**
   * Verify that {@link Task#hasRecordsBefore(Date)} returns <code>true</code> for
   *  a task which has at least a single record starting before <code>testDate</code>.
   */
  @Test
  public void testTaskHasRecordBefore() throws Exception {
    when(testRecordA.getStart()).thenReturn(new Date(testDate.getTime() - 10000));
    when(testRecordB.getStart()).thenReturn(new Date(testDate.getTime()));

    boolean result = classUnderTest.hasRecordsBefore(testDate);

    assertThat("Task must have any record with start date before 'currentDate'!",
        result, is(true));
  }


  /**
   * Verify that {@link Task#hasRecordsBefore(Date)} returns <code>false</code> for
   *  a task which does not have at least a single record starting after <code>testDate</code>.
   */
  @Test
  public void testTaskHasNoRecordBefore() throws Exception {
    when(testRecordA.getStart()).thenReturn(new Date(testDate.getTime()));
    when(testRecordB.getStart()).thenReturn(new Date(testDate.getTime()));

    boolean result = classUnderTest.hasRecordsBefore(testDate);

    assertThat("Task must not have any record with start date before 'currentDate'!",
        result, is(false));
  }


  /**
   * Verify that {@link Task#hasRecordsBefore(Date)} throws an {@link IllegalArgumentException}
   * in case <code>null</code> is passed in as an argument.
   */
  @Test
  public void testTaskHasRecordsBeforeThrowsExceptionOnNullArgument() throws Exception {
    expectedException.expect(IllegalArgumentException.class);
    expectedException.expectMessage("Argument for param 'date' must not be null!");

    classUnderTest.hasRecordsBefore(null);
  }


  /**
   * Verify that {@link Task#getOverallDuration()} sums up the durations of individual records
   * properly.
   */
  @Test
  public void testComputeOverallDurationWithRecords() throws Exception {
    when(testRecordA.getDuration()).thenReturn(1000L);
    when(testRecordB.getDuration()).thenReturn(2000L);

    float duration = classUnderTest.getOverallDuration();

    assertThat(duration, is(3000f));
  }


  /**
   * Verify that {@link Task#getOverallDuration()} returns 0 as total duration for tasks with no
   * records.
   */
  @Test
  public void testComputeOverallDurationWithoutAnyRecord() throws Exception {
    clearRecords();
    float duration = classUnderTest.getOverallDuration();

    assertThat(duration, is(0f));
  }


  /**
   * If a new instance is created from the factory method with a DB-Cursor as parameter, the
   * title should fit the one from the DB.
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


  private void clearRecords() throws Exception {
    Field records = getFieldFromTestClass("records");
    records.set(classUnderTest, new LinkedList<>());
  }


}
