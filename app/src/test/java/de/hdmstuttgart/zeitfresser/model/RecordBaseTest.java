package de.hdmstuttgart.zeitfresser.model;

import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.ExpectedException;

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
  public void setUp() {
    record = new Record();
  }

}
