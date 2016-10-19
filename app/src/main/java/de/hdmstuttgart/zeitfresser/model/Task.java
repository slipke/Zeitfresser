package de.hdmstuttgart.zeitfresser.model;

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

  private long id;
  private boolean active;
  private String name;
  private List<Record> records;
  private Record activeRecord;

  protected static Task withName(String name) {
    return new Task(name);
  }

  private Task(String name) {
    Objects.requireNonNull(name);
    this.name = name;
    this.records = new LinkedList<>();
  }

  /**
   * Start the current task.
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
    records.add(record);
    activeRecord.start();
  }

  private void setActive() {
    active = true;
  }

  /**
   * Stop the current task.
   */
  public void stop() {
    if (isActive() && hasActiveRecord()) {
      disableCurrentActiveRecord();
      setInactive();
    } else {
      throw new IllegalStateException("Task has not been started yet!");
    }
  }

  /**
   * Returns whether the current task is active.
   *
   * @return boolean
   */
  public boolean isActive() {
    return active;
  }

  private boolean hasActiveRecord() {
    return activeRecord != null;
  }

  private void disableCurrentActiveRecord() {
    activeRecord.stop();
    activeRecord = null;
  }

  private void setInactive() {
    active = false;
  }

  public boolean hasRecords() {
    return !records.isEmpty();
  }

  /**
   * Returns the overall duration.
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
}
