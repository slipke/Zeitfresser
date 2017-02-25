package de.hdmstuttgart.zeitfresser.model;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Test;

import java.util.Date;

/**
 * In order to test {@link Record#equals(Object)} method, the possible argument values can be
 * divided into the following equivalence classes:
 * <br/>
 * <ul>
 * <li>Equivalence class 1: <code>null</code> (-> false)</li>
 * <li>Equivalence class 2: <code>this</code> (-> true)</li>
 * <li>Equivalence class 3: <code>!(other instanceOf Record) (-> false)</code></li>
 * <li>Equivalence class 4: <code>!(this.getStart().equals(other.getStart())) (-> false)
 * </code></li>
 * <li>Equivalence class 5: <code>!(this.getEnd().equals(other.getEnd()))</code> (-> false)</li>
 * <li>Equivalence class 6: <code>this.getId() != other.getId()</code> (-> false)</li>
 * <li>Equivalence class 7: A record instances with same property values and a memory
 * address different from <code>this</code> (-> true)</li>
 * </ul>
 * <br/>
 * <br/>
 * For {@link Record#hashCode()} the testing procedure works as follows: We expect that two
 * record objects with identical property values return the same hash code. If there's a
 * difference in at least a single attribute, the hash codes must be different.
 *
 * @author benedikt.hensle, patrick.kleindienst
 */
public class RecordEqualsHashcodeTest extends RecordBaseTest {

  private Record otherRecord;

  @Override
  @Before
  public void setUp() throws Exception {
    super.setUp();

    Date start = new Date();
    Date end = new Date(start.getTime() + 100000);

    setRecordFieldValue(record, "start", start);
    setRecordFieldValue(record, "end", end);

    otherRecord = record.clone();
  }


  /*
      ### EQUALS ###
   */

  /**
   * Test case for equivalence class 1.
   */
  @Test
  public void testEqualsReturnsFalseOnNullArg() {
    boolean equals = record.equals(null);

    assertThat(equals, is(false));
  }


  /**
   * Test case for equivalence class 2.
   */
  @Test
  public void testEqualsReturnsTrueOnSelfArg() {
    boolean equals = record.equals(record);

    assertThat(equals, is(true));
  }


  /**
   * Test case for equivalence class 3.
   */
  @Test
  public void testEqualsReturnsFalseOnWrongArgType() {
    boolean equals = record.equals("Not a record");

    assertThat(equals, is(false));
  }


  /**
   * Test case for equivalence class 4.
   */
  @Test
  public void testEqualsReturnsFalseOnDifferentStartVal() throws Exception {
    setRecordFieldValue(otherRecord, "start", null);
    boolean equals = record.equals(otherRecord);

    assertThat(equals, is(false));

    equals = otherRecord.equals(record);

    assertThat(equals, is(false));
  }


  /**
   * Test case for equivalence class 5.
   */
  @Test
  public void testEqualsReturnsFalseOnDifferentEndVal() throws Exception {
    setRecordFieldValue(otherRecord, "end", null);
    boolean equals = record.equals(otherRecord);

    assertThat(equals, is(false));

    equals = otherRecord.equals(record);

    assertThat(equals, is(false));
  }


  /**
   * Test case for equivalence class 6.
   */
  @Test
  public void testEqualsReturnsFalseOnDifferentIdVal() throws Exception {
    setRecordFieldValue(otherRecord, "id", 42L);

    boolean equals = record.equals(otherRecord);

    assertThat(equals, is(false));
  }


  /**
   * Test case for equivalence class 7.
   */
  @Test
  public void testEqualsReturnsTrueOnIdenticalRecords() {
    boolean equals = record.equals(otherRecord);

    assertThat(equals, is(true));
  }


  /*
      ### HASHCODE ###
   */

  /**
   * We expect identical records (i.e. same property values) to produce identical hash codes.
   */
  @Test
  public void testHashcodeReturnsSameValueForIdenticalRecords() {
    int hashcodeA = record.hashCode();
    int hashcodeB = otherRecord.hashCode();

    assertThat(hashcodeA == hashcodeB, is(true));
  }

  /**
   *  Two records with different id property values are expected to produce different hash codes.
   *
   * @throws Exception in case setting field via reflection fails.
   */
  @Test
  public void testRecordsWithDifferentIDsProduceDifferentHashCodes() throws Exception {
    setRecordFieldValue(otherRecord, "id", 42L);
    int hashcodeA = record.hashCode();
    int hashcodeB = otherRecord.hashCode();

    assertThat(hashcodeA == hashcodeB, is(false));
  }


  /**
   *  Two records with different start property values are expected to produce different hash codes.
   *
   * @throws Exception in case setting field via reflection fails.
   */
  @Test
  public void testRecordsWithDifferentStartValProduceDifferentHashCodes() throws Exception {
    setRecordFieldValue(otherRecord, "start", new Date(otherRecord.getStart().getTime() + 100));
    int hashcodeA = record.hashCode();
    int hashcodeB = otherRecord.hashCode();

    assertThat(hashcodeA == hashcodeB, is(false));
  }


  /**
   *  Two records with different end property values are expected to produce different hash codes.
   *
   * @throws Exception in case setting field via reflection fails.
   */
  @Test
  public void testRecordsWithDifferentEndValProduceDifferentHashCodes() throws Exception {
    setRecordFieldValue(otherRecord, "end", new Date(otherRecord.getEnd().getTime() + 100));
    int hashcodeA = record.hashCode();
    int hashcodeB = otherRecord.hashCode();

    assertThat(hashcodeA == hashcodeB, is(false));
  }

}
