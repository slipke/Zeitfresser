package de.hdmstuttgart.zeitfresser.model;

import android.util.Log;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;


import java.lang.reflect.Field;
import java.util.Date;

import de.hdmstuttgart.zeitfresser.model.Record;

public class RecordTest {

    private Record record;

    @Before
    public void setUp() {
        record = new Record();
    }

    @After
    public void tearDown(){
        record = null;
    }

    @Test
    public void test(){
        //Log.v("test","test");
        record.start();
        assertTrue(true);
    }

    @Test
    public void testHasStartAndEndTime(){
        //assertFalse(record.has);
    }

    @Test
    public void testStartTimeLessOrEqualsCurrentTime() {
        record.start();
        try {
            Field startField = Record.class.getDeclaredField("start");
            startField.setAccessible(true);
            assertTrue(new Date().getTime() - ((Date) startField.get(record)).getTime() >= 0L);
        } catch (NoSuchFieldException | IllegalAccessException e){
            e.printStackTrace();
            fail(e.getMessage());
        }
    }

    @Test
    public void testEndTimeHigherThanStartTime(){

        record.start();
        record.stop();
        try {
            Field startField = Record.class.getDeclaredField("start");
            startField.setAccessible(true);
            Date start = (Date) startField.get(record);
            Field endField   = Record.class.getDeclaredField("end");
            endField.setAccessible(true);
            Date end = (Date) endField.get(record);
            assertTrue(end.getTime() - start.getTime() >= 0);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testGetDuration(){
        record.start();
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        record.stop();
        try {
            Field startField = Record.class.getDeclaredField("start");
            startField.setAccessible(true);
            Date start = (Date) startField.get(record);
            Field endField   = Record.class.getDeclaredField("end");
            endField.setAccessible(true);
            Date end = (Date) endField.get(record);
            long expectedDuration = end.getTime() - start.getTime();
            assertEquals(expectedDuration, record.getDuration());
        } catch (NoSuchFieldException | IllegalAccessException e) {
            fail(e.getMessage());
        }
    }
}
