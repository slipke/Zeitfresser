package de.hdmstuttgart.zeitfresser.model;

import android.database.Cursor;
import android.util.Log;

import de.hdmstuttgart.zeitfresser.db.DbStatements;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Observable;

/**
 * This class represents a record, which in turn stands for a single phase of execution of a {@link
 * Task}. As the Task class, it's implementation style also avoids the anemic domain model style.
 */
public class Record extends Observable {

  private long id = 0;
  private long recordId;
  private Date start = null;
  private Date end = null;

  /**
   * Create Record (factory method).
   *
   * @param start Startdate
   * @param end Enddate
   *
   * @return The new record object
   */
  public static Record withStartAndEnd(Date start, Date end) {
    Record record = new Record();
    record.start = start;
    record.end = end;
    return record;
  }

  /**
   *  Builds a single {@link Record} instance from a cursor.
   *
   * @param context The current Activity context.
   * @return A single record.
   */
  public static Record fromCursor(Cursor context) {
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
    String start = context.getString(context.getColumnIndexOrThrow(DbStatements.COLUMN_NAME_START));
    String end = context.getString(context.getColumnIndexOrThrow(DbStatements.COLUMN_NAME_END));
    long id = context.getLong(context.getColumnIndexOrThrow(DbStatements._ID));
    Record record = new Record();

    try {
      record.start = formatter.parse(start);
      record.end = formatter.parse(end);
    } catch (ParseException exception) {
      // This should not happen
      Log.e("Record", "Failed to parse date");
    }

    record.id = id;
    return record;
  }

  /**
   * Getter for start date.
   *
   * @return The start date
   */
  public Date getStart() {
    return start != null ? new Date(start.getTime()) : null;
  }

  /**
   * Getter for end date.
   *
   * @return The end date
   */
  public Date getEnd() {
    return end != null ? new Date(end.getTime()) : null;
  }

  /**
   * Getter for id.
   *
   * @return The id
   */
  public long getId() {
    return id;
  }

  /**
   * Get the record duration.
   *
   * @return long
   */
  public long getDuration() {
    if (end == null || start == null) {
      return 0;
    }

    return end.getTime() - start.getTime();
  }

  /**
   * Start the record.
   */
  public void start() {
    if (start != null && end != null) {
      if (start.getTime() > end.getTime()) {
        // if start time is even bigger than end time then
        // start was called twice
        throw new IllegalStateException();
      }
    } else if (start != null && end == null) {
      // record was started twice, but not stopped
      throw new IllegalStateException();
    }
    start = new Date();
  }

  /**
   * Stop the record.
   */
  public void stop() {
    if (start == null) {
      throw new IllegalStateException();
    }
    if (end != null) {
      if (end.getTime() >= start.getTime()) {
        /* if end time is even bigger than start time then
           stop was called twice */
        throw new IllegalStateException();
      }
    }
    end = new Date();
  }

  private boolean hasStartAndEndTime() {
    return (start != null && end != null);
  }

  /**
   * Simple object comparison which should fit our needs.
   *
   * @param other Object to compare
   * @return boolean
   */
  @Override
  public boolean equals(Object other) {
    if (other == null) {
      return false;
    }
    if (other == this) {
      return true;
    }
    if (!(other instanceof Record)) {
      return false;
    }
    Record recordClass = (Record) other;

    // For the null checks
    if (this.getEnd() == null && recordClass.getEnd() != null) {
      return false;
    }
    if (this.getStart() == null && recordClass.getStart() != null) {
      return false;
    }

    if (this.getStart() != null && !this.getStart().equals(recordClass.getStart())) {
      return false;
    }

    if (this.getEnd() != null && !this.getEnd().equals(recordClass.getEnd())) {
      return false;
    }

    if (this.getId() != recordClass.getId()) {
      return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    assert false : "hashCode not designed";
    return 42; // any arbitrary constant will do
  }
}
