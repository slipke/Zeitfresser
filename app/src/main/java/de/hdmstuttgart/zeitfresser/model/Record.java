package de.hdmstuttgart.zeitfresser.model;

import java.util.Date;
import java.util.Observable;

/**
 * This class represents a record, which in turn stands for a single phase of execution of a {@link
 * Task}. As the Task class, it's implementation style also avoids the anemic domain model style.
 */
public class Record extends Observable {

  private long id = 0;
  private Date start = null;
  private Date end = null;

  /**
   * Create Record (factory method)
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

  public Record() {

  }

  public Date getStart() {
    return start;
  }

  public long getId() {
    return id;
  }

  /**
   * Get the record duration.
   *
   * @return long
   */
  public long getDuration() {
    return end.getTime() - start.getTime();
  }

  public void start() {
    if (start != null && end != null) {
      if (start.getTime() > end.getTime()) {
        /* if start time is even bigger than end time then
           start was called twice */
        throw new IllegalStateException();
      }
    } else if (start != null && end == null) {
      // record was started twice, but not stopped
      throw new IllegalStateException();
    }
    start = new Date();
  }

  public void stop() {
    if (start == null) {
      throw new IllegalStateException();
    }
    if (end != null && start != null) {
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

}
