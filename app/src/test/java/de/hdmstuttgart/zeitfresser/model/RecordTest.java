package de.hdmstuttgart.zeitfresser.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

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
    //assertFalse(record.has);
  }

  @Test
  public void testStartTimeLessOrEqualsCurrentTime() {
    record.start();
    try {
      Field startField = Record.class.getDeclaredField("start");
      startField.setAccessible(true);
      assertTrue(new Date().getTime() - ((Date) startField.get(record)).getTime() >= 0L);
    } catch (NoSuchFieldException | IllegalAccessException ex) {
      ex.printStackTrace();
      fail(ex.getMessage());
    }
  }

  @Test
  public void testEndTimeHigherThanStartTime() {

    record.start();
    record.stop();
    try {
      Field startField = Record.class.getDeclaredField("start");
      startField.setAccessible(true);
      Date start = (Date) startField.get(record);
      Field endField = Record.class.getDeclaredField("end");
      endField.setAccessible(true);
      Date end = (Date) endField.get(record);
      assertTrue(end.getTime() - start.getTime() >= 0);
    } catch (NoSuchFieldException | IllegalAccessException ex) {
      fail(ex.getMessage());
    }
  }

  @Test
  public void testGetDuration() {
    record.start();
    try {
      Thread.sleep(10);
    } catch (InterruptedException ex) {
      ex.printStackTrace();
    }
    record.stop();
    try {
      Field startField = Record.class.getDeclaredField("start");
      startField.setAccessible(true);
      Date start = (Date) startField.get(record);
      Field endField = Record.class.getDeclaredField("end");
      endField.setAccessible(true);
      Date end = (Date) endField.get(record);
      long expectedDuration = end.getTime() - start.getTime();
      assertEquals(expectedDuration, record.getDuration());
    } catch (NoSuchFieldException | IllegalAccessException ex) {
      fail(ex.getMessage());
    }
  }
}
