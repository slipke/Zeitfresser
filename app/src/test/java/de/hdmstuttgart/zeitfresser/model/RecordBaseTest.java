package de.hdmstuttgart.zeitfresser.model;

import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.ExpectedException;

import java.lang.reflect.Field;

/**
 * A base class for test classes concerning {@link Record}.
 *
 * @author patrick.kleindienst
 */

public class RecordBaseTest {

  @Rule
  public ExpectedException expectedException = ExpectedException.none();

  protected Record record;

  @Before
  public void setUp() throws Exception {
    record = Record.create();
  }

  protected void setRecordFieldValue(Record record, String fieldName, Object value) throws
      NoSuchFieldException,
      IllegalAccessException {
    Field declaredField = Record.class.getDeclaredField(fieldName);
    declaredField.setAccessible(true);
    declaredField.set(record, value);
  }

}
