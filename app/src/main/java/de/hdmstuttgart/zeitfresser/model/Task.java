package de.hdmstuttgart.zeitfresser.model;

import android.database.Cursor;

import de.hdmstuttgart.zeitfresser.db.DbStatements;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

/**
 * This class represents a task, which can be started, stopped and manages a collection of records.
 * Instead of following the JavaBean pattern comprising getters and setters, this domain class only
 * publishes business logic instead of entirely disclosing it's internals. This way, we can
 * circumvent the "anemic domain model", as described by
 * <a href="http://www.martinfowler.com/bliki/AnemicDomainModel.html">Martin Fowler</a>.
 */
public class Task {

  static int instanceCounter = 0;

  protected long id;
  protected boolean active;
  protected String name;
  protected List<Record> records;
  protected Record activeRecord;

  /**
   * Builds a single {@link Task} instance from a cursor.
   *
   * @param cursor The cursor to build the task from.
   * @return A single task.
   */
  public static Task fromCursor(Cursor cursor) {
    String name = cursor.getString(cursor.getColumnIndexOrThrow(DbStatements.COLUMN_NAME_TITLE));
    long id = cursor.getLong(cursor.getColumnIndexOrThrow(DbStatements._ID));
    return new Task(name, id);
  }

  public static Task withName(String name) {
    return new Task(name, ++instanceCounter);
  }

  protected Task() {
    this("Test", 1);
  }

  protected Task(String name, long id) {
    Objects.requireNonNull(name);
    this.name = name;
    this.records = new LinkedList<>();
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public long getId() {
    return id;
  }

  public Record getActiveRecord() {
    return this.activeRecord;
  }

  public boolean isActive() {
    return active;
  }

  protected void setActive() {
    active = true;
  }

  protected void setInactive() {
    active = false;
  }

  /**
   * Start the current task. This includes preparing a new {@link Record} which keeps
   * track of the elapsing time as well as setting the task's current status to "active". Throw
   * an {@link IllegalStateException} when {@code start()} is called on a task which is already
   * in active state.
   */
  public void start() {
    if (isActive()) {
      throw new IllegalStateException("Task has already been started!");
    } else {
      prepareNewRecord();
      setActive();
    }
  }

  /**
   * Add a record to the current task (needed for junit testing).
   */
  public void addRecord(Record record) {
    if (record == null) {
      throw new IllegalArgumentException("Record argument must not be null!");
    } else {
      records.add(record);
    }
  }


  /**
   * Stop the current task if it is active and has an active record which captures time. If this
   * method is called on an inactive task or on a task which has no active record, an
   * {@link IllegalStateException} is thrown.
   */
  public void stop() {
    if (isActive() && hasActiveRecord()) {
      disableCurrentActiveRecord();
      setInactive();
    } else {
      throw new IllegalStateException("Can't stop inactive task.");
    }
  }

  public boolean hasActiveRecord() {
    return activeRecord != null;
  }

  /**
   * Returns the overall duration of all records attached to a certain task.
   *
   * @return long
   */
  public float getOverallDuration() {
    long overallDuration = 0L;
    for (Record record : records) {
      overallDuration += record.getDuration();
    }
    return overallDuration;
  }

  /**
   * Check if the task has at least one single {@link Record}
   * whose start lies after the specified date.
   *
   * @param date The date against which the task's records are checked.
   * @return True, if task has a record starting after the specified date. False Otherwise.
   */
  public boolean hasRecordsAfter(Date date) {
    if (date == null) {
      throw new IllegalArgumentException("Argument for param 'date' must not be null!");
    } else {
      for (Record record : records) {
        if (record.getStart().after(date)) {
          return true;
        }
      }
      return false;
    }
  }

  /**
   * Check if the task has at least one single {@link Record}
   * whose start lies before the specified date.
   *
   * @param date The date against which the task's records are checked.
   * @return True, if task has a record starting before the specified date. False Otherwise.
   */
  public boolean hasRecordsBefore(Date date) {
    if (date == null) {
      throw new IllegalArgumentException("Argument for param 'date' must not be null!");
    } else {
      for (Record record : records) {
        // start has to be < than date
        if (record.getStart().before(date)) {
          return true;
        }
      }
      return false;
    }
  }

  public boolean hasRecords() {
    return !records.isEmpty();
  }

  public String toString() {
    return this.id + " " + this.name;
  }

  protected void prepareNewRecord() {
    Record record = createNewRecord();
    setAsActiveRecord(record);
    addRecord(record);
    startActiveRecord();
  }

  protected Record createNewRecord() {
    return new Record();
  }

  protected void setAsActiveRecord(Record record) {
    this.activeRecord = record;
  }

  protected void startActiveRecord() {
    this.activeRecord.start();
  }

  protected void disableCurrentActiveRecord() {
    stopActiveRecord();
    setAsActiveRecord(null);
  }

  protected void stopActiveRecord() {
    activeRecord.stop();
  }


  /**
   * Very simple implementation for object comparison, should fit our needs.
   *
   * @param other The other object
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
    if (!(other instanceof Task)) {
      return false;
    }

    Task otherTask = (Task) other;

    if (this.getId() != otherTask.getId()) {
      return false;
    }

    if (!this.getName().equals(otherTask.getName())) {
      return false;
    }

    if (!this.records.equals(otherTask.records)) {
      return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    int hashcode = 17;
    int multiplier = 5;

    hashcode = hashcode * multiplier + ((int) (id >>> 32) + (int) (id & 0xFFFFFFFF));
    hashcode = hashcode * multiplier + name.hashCode();
    hashcode = hashcode * multiplier + records.hashCode();

    return hashcode;
  }
}
