package de.hdmstuttgart.zeitfresser.model;

import java.util.Date;
import java.util.Observable;

/**
 * This class represents a record, which in turn stands for a single phase of execution of a {@link
 * Task}. As the Task class, it's implementation style also avoids the anemic domain model style.
 */
public class Record extends Observable {

  private long id;
  private long duration;
  private Date start;
  private Date end;

  /**
   * This factory method is needed for junit testing (since we don't have mocking yet)
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

  public void start() {
    start = new Date();
  }

  public void stop() {
    end = new Date();
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
    if (duration == 0L && hasStartAndEndTime()) {
      duration = end.getTime() - start.getTime();
    }
    return duration;
  }

  private boolean hasStartAndEndTime() {
    return (start != null && end != null);
  }

}
