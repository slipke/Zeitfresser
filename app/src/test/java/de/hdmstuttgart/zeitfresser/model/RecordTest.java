package de.hdmstuttgart.zeitfresser.model;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import android.database.Cursor;

import de.hdmstuttgart.zeitfresser.db.DbStatements;
import org.junit.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class RecordTest extends RecordBaseTest {


  /**
   * This test case verifies a records's initial state. Both start and end date must be null.
   */
  @Test
  public void testNewRecordHasNoStartAndEndSet() {
    assertThat(record.getStart(), nullValue());
    assertThat(record.getEnd(), nullValue());
  }

  /**
   * Stopping a record which has not been started yet marks a forbidden state transition. This test
   * ensures that an {@link IllegalArgumentException} is thrown in case <code>stop()</code> gets
   * called on a record which has never been started before.
   */
  @Test
  public void testStopNeverActiveRecordThrowsException() {
    expectedException.expect(IllegalStateException.class);
    expectedException.expectMessage("Unable to stop inactive record!");

    record.stop();

    assertThat(record.getStart(), nullValue());
    assertThat(record.getEnd(), nullValue());
  }

  /**
   * Starting a fresh record is an allowed state transition and moves it from inactive into
   * active state. In that state, the start date must be set while the end date must still be
   * <code>null</code>.
   */
  @Test
  public void testStartNeverActiveRecord() {
    record.start();

    assertThat(record.getStart(), notNullValue());
    assertThat(record.getEnd(), nullValue());
  }

  /**
   * Stopping a record which is currently in running state is also an allowed state transition.
   * After calling <code>stop()</code> a record is back in inactive state. However, it must
   * posses a valid start and end date different from null.
   */
  @Test
  public void testStopActiveRecord() {
    record.start();
    record.stop();

    assertThat(record.getStart(), notNullValue());
    assertThat(record.getEnd(), notNullValue());
    assertThat(record.getStart().getTime() <= record.getEnd().getTime(), is(true));
  }

  /**
   * Calling <code>start()</code> on a record which is already in running state must not be
   * allowed. This test ensures that an {@link IllegalStateException} is thrown in that case.
   * Beyond that, it verifies that the record is still in active state and that its internals
   * aren't corrupted by calling <code>start()</code> twice.
   */
  @Test
  public void testStartAlreadyActiveRecordThrowsException() {
    expectedException.expect(IllegalStateException.class);
    expectedException.expectMessage("Unable to start already active record!");

    record.start();
    record.start();

    assertThat(record.getStart(), notNullValue());
    assertThat(record.getEnd(), nullValue());
  }

  /**
   * Any record which has been started and then stopped (i.e. it has a start as well as an end
   * date) has arrived in it's "end state". Calling <code>start()</code> on such a record again
   * should throw an {@link IllegalStateException}.
   */
  @Test
  public void testStartFinishedRecordAgainThrowsException() {
    expectedException.expect(IllegalStateException.class);
    expectedException.expectMessage("Unable to re-start finished record!");

    record.start();
    record.stop();
    record.start();

    assertThat(record.getStart(), notNullValue());
    assertThat(record.getEnd(), notNullValue());
    assertThat(record.getStart().getTime() <= record.getEnd().getTime(), is(true));
  }

  /**
   * Any record which has been started and then stopped (i.e. it has a start as well as an end
   * date) has arrived in it's "end state". Calling <code>stop()</code> on such a record again
   * should throw an {@link IllegalStateException}.
   */
  @Test
  public void testStopAlreadyFinishedRecordThrowsException() {
    expectedException.expect(IllegalStateException.class);
    expectedException.expectMessage("Unable to stop already inactive record!");

    record.start();
    record.stop();
    record.stop();

    assertThat(record.getStart(), notNullValue());
    assertThat(record.getEnd(), notNullValue());
    assertThat(record.getStart().getTime() <= record.getEnd().getTime(), is(true));
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
