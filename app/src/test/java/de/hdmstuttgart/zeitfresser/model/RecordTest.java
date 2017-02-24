package de.hdmstuttgart.zeitfresser.model;

import android.database.Cursor;

import de.hdmstuttgart.zeitfresser.db.DbStatements;

import static junit.framework.Assert.assertFalse;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Field;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class RecordTest {

  private Record record;


  @Before
  public void setUp() {
    record = new Record();
  }

  @After
  public void tearDown() {
    record = null;
  }

  @Test
  public void testStopStartedRecord() {
    record.start();
    long start = getStartFieldValue().getTime();
    timeout();
    record.stop();
    long end = getEndFieldValue().getTime();
    assertTrue(end > start);
  }

  @Test
  public void testStartStoppedRecord() {
    record.start();
    timeout();

    record.stop();
    long end = getEndFieldValue().getTime();

    timeout();
    record.start();
    long start = getStartFieldValue().getTime();

    assertTrue(start >= end);
  }

  @Test(expected = IllegalStateException.class)
  public void testStopStoppedRecord() {
    record.start();
    timeout();

    record.stop();
    timeout();

    record.stop();
    long end = getEndFieldValue().getTime();

    long start = getStartFieldValue().getTime();
    assertTrue(end > start);
  }

  @Test(expected = IllegalStateException.class)
  public void testStartStartedRecord() {
    record.start();
    timeout();

    record.start();
    long start = getStartFieldValue().getTime();
    long end = getEndFieldValue().getTime();
    assertTrue(end > start);
  }

  /**
   * returns the value of the private start field attribute
   * of the Record object.
   *
   * @return date object
   */
  private Date getStartFieldValue() {
    try {
      Field startField = Record.class.getDeclaredField("start");
      startField.setAccessible(true);
      return (Date) startField.get(record);
    } catch (IllegalAccessException | NoSuchFieldException ex) {
      ex.printStackTrace();
      return null;
    }
  }

  /**
   * returns the value of the private end field attribute
   * of the Record object.
   *
   * @return date object
   */
  private Date getEndFieldValue() {
    try {
      Field endField = Record.class.getDeclaredField("end");
      endField.setAccessible(true);
      return (Date) endField.get(record);
    } catch (IllegalAccessException | NoSuchFieldException ex) {
      ex.printStackTrace();
      return null;
    }
  }

  /**
   * waits for 10 milliseconds.
   */
  void timeout() {
    try {
      Thread.sleep(10);
    } catch (InterruptedException ex) {
      ex.printStackTrace();
    }
  }

  /**
   * Test the equals method.
   */
  @Test
  public void testEquals() {
    Record record1 = new Record();
    Record record2 = new Record();

    assertFalse(record1.equals(null));
    assertTrue(record1.equals(record1));
    assertFalse(record1.equals(new Object()));
    assertTrue(record1.equals(record2));
    assertTrue(record2.equals(record1));
  }

  /**
   * If a new instance is created from the factory method with a DB-Cursor as parameter, the
   * title should fit the one from the DB.
   */
  @Test
  public void testFactoryFromCursor() throws ParseException {
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
    String start = "2017-02-12T12:00:00";
    Date startDate = formatter.parse(start);

    Cursor cursorMock = mock(Cursor.class);
    when(cursorMock.getString(cursorMock.getColumnIndexOrThrow(DbStatements.COLUMN_NAME_START)))
            .thenReturn(start);
    Record record = Record.fromCursor(cursorMock);

    assertEquals(startDate, record.getStart());
  }
}
