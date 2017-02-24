package de.hdmstuttgart.zeitfresser.model;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;
import static org.mockito.Mockito.mock;

import org.junit.Test;

/**
 * Created by patrick on 24.02.17.
 */

public class TaskEqualsHashcodeTest extends TaskBaseTest {


  /**
   * Test the equals method.
   */
  @Test
  public void testEquals() {
    Task task1 = new Task();

    assertFalse(task1.equals(null));
    assertTrue(task1.equals(task1));
    assertFalse(task1.equals(new Object()));

    Task task2 = new Task();

    assertTrue(task1.equals(task2));

    task1.id = 1;
    task2.id = 2;
    assertFalse(task1.equals(task2));

    task2.id = 1;
    assertTrue(task1.equals(task2));

    Record testRecord = mock(Record.class);
    task1.addRecord(testRecord);

    assertFalse(task1.equals(task2));

    task2.addRecord(testRecord);
    assertTrue(task1.equals(task2));

    Record testRecord2 = mock(Record.class);

    task2.addRecord(testRecord2);
    assertFalse(task2.equals(task1));
  }
}
