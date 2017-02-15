package de.hdmstuttgart.zeitfresser.model;

import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Field;
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
  public void testHasStartAndEndTime() {
    // TODO Implementation!
    //assertFalse(record.has);
  }

  @Test(expected = NullPointerException.class)
  public void testStartInitialRecord() {
    record.start();
    long start = getStartFieldValue().getTime();
    long end = getEndFieldValue().getTime();
    assertTrue(start >= end);
  }

  @Test(expected = IllegalStateException.class)
  public void testStopInitialRecord() {
    record.stop();
    long start = getStartFieldValue().getTime();
    long end = getEndFieldValue().getTime();
    assertTrue(start >= end);
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

  void timeout() {
    try {
      Thread.sleep(10);
    } catch (InterruptedException ex) {
      ex.printStackTrace();
    }
  }
}
