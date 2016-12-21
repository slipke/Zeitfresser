package de.hdmstuttgart.zeitfresser.model;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Date;

/**
 * This class represents a task, which can be started, stopped and manages a collection of records.
 * Instead of following the JavaBean pattern comprising getters and setters, this domain class only
 * publishes business logic instead of entirely disclosing it's internals. This way, we can
 * circumvent the "anemic domain model", as described by
 * <a href="http://www.martinfowler.com/bliki/AnemicDomainModel.html">Martin Fowler</a>.
 */
public class Task {

  private static int instanceCounter = 0;

  private long id;
  private boolean active;
  private String name;
  private List<Record> records;
  private Record activeRecord;

  public static Task withName(String name) {
    return new Task(name, ++instanceCounter);
  }

  private Task(String name, long id) {
    Objects.requireNonNull(name);
    this.name = name;
    this.records = new LinkedList<>();
    this.id = id;
  }

  /**
   * Start the current task. This includes preparing a new {@link Record} which keeps
   * track of the elapsing time as well as setting the task's current status to "active". Throw
   * an {@link IllegalStateException} when {@code start()} is called on a task which is already
   * in active state.
   */
  public void start() {
    if (!isActive()) {
      prepareNewRecord();
      setActive();
    } else {
      throw new IllegalStateException("Task has already been started!");
    }
  }

  private void prepareNewRecord() {
    Record record = new Record();
    activeRecord = record;
    addRecord(record);
    activeRecord.start();
  }

  /**
   * Add a record to the current task (needed for junit testing).
   */
  public void addRecord(Record record) {
    records.add(record);
  }

  private void setActive() {
    active = true;
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
      throw new IllegalStateException("Can't stop an inactive task");
    }
  }

  private void disableCurrentActiveRecord() {
    activeRecord.stop();
    activeRecord = null;
  }

  private void setInactive() {
    active = false;
  }

  /**
   * Returns whether the current task is active or not.
   *
   * @return boolean
   */
  public boolean isActive() {
    return active;
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

  public String getName() {
    return name;
  }

  public long getId() {
    return id;
  }

  public boolean hasRecordsAfter(Date date) {
    if (date != null) {
      for (Record record : records) {
        // start has to be > than date
        if (record.getStart().after(date)) {
          return true;
        }
      }
      return false;
    } else {
      throw new IllegalArgumentException("Date argument must not be null!");
    }
  }

  public boolean hasRecordsBefore(Date date) {
    for (Record record : records) {
      // start has to be < than date
      if (record.getStart().before(date)) {
        return true;
      }
    }
    return false;
  }

  public boolean hasAnyRecords() {
    return !records.isEmpty();
  }

  public String toString() {
    return this.id + " " + this.name;
  }
}
