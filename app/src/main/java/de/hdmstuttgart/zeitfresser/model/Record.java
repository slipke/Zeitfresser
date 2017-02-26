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
public class Record extends Observable implements Cloneable {

  private long id = 0;
  private Date start = null;
  private Date end = null;

  /**
   * Create Record (factory method).
   *
   * @return The new record object
   */
  public static Record create() {
    return new Record();
  }

  private Record() {

  }

  /**
   * Builds a single {@link Record} instance from a cursor.
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
    if (start == null) {
      start = new Date();
    } else {
      if (end != null) {
        throw new IllegalStateException("Unable to re-start finished record!");
      } else {
        throw new IllegalStateException("Unable to start already active record!");
      }
    }
  }

  /**
   * Stop the record.
   */
  public void stop() {
    if (start == null) {
      throw new IllegalStateException("Unable to stop inactive record!");
    } else {
      if (end != null) {
        throw new IllegalStateException("Unable to stop already inactive record!");
      } else {
        end = new Date();
      }
    }
  }


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

    Record otherRecord = (Record) other;

    if (this.getStart() != null && !this.getStart().equals(otherRecord.getStart())) {
      return false;
    }

    if (otherRecord.getStart() != null && !otherRecord.getStart().equals(this.getStart())) {
      return false;
    }

    if (this.getEnd() != null && !this.getEnd().equals(otherRecord.getEnd())) {
      return false;
    }

    if (otherRecord.getEnd() != null && !otherRecord.getEnd().equals(this.getEnd())) {
      return false;
    }

    if (this.getId() != otherRecord.getId()) {
      return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    int hashcode = 17;
    int multiplier = 5;

    hashcode = hashcode * multiplier + ((int) (id >>> 32) + (int) (id & 0xFFFFFFFF));
    hashcode = hashcode * multiplier + ((start == null) ? 0 : start.hashCode());
    hashcode = hashcode * multiplier + ((end == null) ? 0 : end.hashCode());

    return hashcode;
  }


  @Override
  public Record clone() {
    Record record = null;

    try {
      record = (Record) super.clone();
      record.id = this.id;
      record.start = this.start;
      record.end = this.end;
    } catch (CloneNotSupportedException ex) {
      ex.printStackTrace();
    }

    return record;
  }
}
