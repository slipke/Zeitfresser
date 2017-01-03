package de.hdmstuttgart.zeitfresser.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.internal.matchers.Null;

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

  @Test(expected = NullPointerException.class)
  public void testStartInitialRecord(){
    record.start();
    long start  = getStartFieldValue().getTime();
    long end    = getEndFieldValue()  .getTime();
    assertTrue(start >= end);
  }

  @Test(expected = IllegalStateException.class)
  public void testStopInitialRecord(){
    record.stop();
    long start  = getStartFieldValue().getTime();
    long end    = getEndFieldValue()  .getTime();
    assertTrue(start >= end);
  }

  @Test
  public void testStopStartedRecord(){
    record.start();
    long start  = getStartFieldValue().getTime();
    timeout();
    record.stop();
    long end    = getEndFieldValue()  .getTime();
    assertTrue(end > start);
  }

  @Test
  public void testStartStoppedRecord(){
    record.start();
    timeout();

    record.stop();
    long end    = getEndFieldValue()  .getTime();

    timeout();
    record.start();
    long start  = getStartFieldValue().getTime();

    assertTrue(start >= end);
  }

  @Test(expected=IllegalStateException.class)
  public void testStopStoppedRecord(){
    record.start();
    timeout();
    long start  = getStartFieldValue().getTime();

    record.stop();
    timeout();

    record.stop();
    long end    = getEndFieldValue()  .getTime();

    assertTrue(end > start);
  }

  @Test(expected=IllegalStateException.class)
  public void testStartStartedRecord(){
    record.start();
    timeout();

    record.start();
    long start  = getStartFieldValue().getTime();
    long end    = getEndFieldValue().getTime();
    assertTrue(end > start);
  }

  // helper method,return start field via reflection
  public Date getStartFieldValue() {
    try{
      Field startField = Record.class.getDeclaredField("start");
      startField.setAccessible(true);
      return (Date) startField.get(record);
    } catch (IllegalAccessException | NoSuchFieldException e){
      e.printStackTrace();
      return null;
    }
  }

  // helper method, returns end field via reflection
  public Date getEndFieldValue() {
    try{
      Field endField = Record.class.getDeclaredField("end");
      endField.setAccessible(true);
      return (Date) endField.get(record);
    } catch (IllegalAccessException | NoSuchFieldException e){
      e.printStackTrace();
      return null;
    }
  }

  void timeout(){
    try {
      Thread.sleep(10);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

  /********************************************************************/


}
