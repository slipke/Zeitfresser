package de.hdmstuttgart.zeitfresser.model;

import de.hdmstuttgart.zeitfresser.model.manager.TaskManager;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * This class is a dummy class for the task manager used for testing. The method getTaskList()
 * returns a fixed list of TaskDummy which contain a fixed list of records.
 */
class TaskManagerDummy extends TaskManager {

  @Override
  public List<Task> getTaskList() {
    // Create TaskList
    List<Task> taskList = new LinkedList<>();
    taskList.add(this.createDummy1Task());
    taskList.add(this.createDummy2Task());
    taskList.add(this.createDummy3Task());
    taskList.add(this.createDummy4Task());

    return taskList;
  }

  /**
   * Returns a record with given duration in hours.
   *
   * @param startDate Start date
   * @param duration Duration in hours
   * @return The record
   */
  private Record createRecordWithStartAndDuration(Date startDate, long duration) {
    Date endDate = new Date();
    endDate.setTime(startDate.getTime() + (duration * 60 * 60 * 1000));
    return Record.withStartAndEnd(startDate, endDate);
  }

  /**
   * Creates the first dummy task which has two records before the current date.
   *
   * @return The task
   */
  private Task createDummy1Task() {
    List<Record> recordList = new LinkedList<>();

    Date nowMinusTwoDays = new Date();
    nowMinusTwoDays.setTime(nowMinusTwoDays.getTime() - (48 * 60 * 60 * 1000));
    Date nowMinusOneDay = new Date();
    nowMinusOneDay.setTime(nowMinusOneDay.getTime() - (24 * 60 * 60 * 1000));

    recordList.add(this.createRecordWithStartAndDuration(nowMinusOneDay, 4));
    recordList.add(this.createRecordWithStartAndDuration(nowMinusTwoDays, 8));

    return this.createTaskWithRecordList("Dummy1 with Records before today", recordList);
  }

  /**
   * Creates the second dummy task which has two records 4 days in the future.
   *
   * @return The task
   */
  private Task createDummy2Task() {
    List<Record> recordList = new LinkedList<>();

    Date nowPlusFourDays = new Date();
    nowPlusFourDays.setTime(nowPlusFourDays.getTime() + (4 * 24 * 60 * 60 * 1000));

    recordList.add(this.createRecordWithStartAndDuration(nowPlusFourDays, 2));
    recordList.add(this.createRecordWithStartAndDuration(nowPlusFourDays, 4));

    return this.createTaskWithRecordList("Dummy2 with Records four days in the future",
            recordList);
  }

  /**
   * Creates the third dummy task with no records.
   *
   * @return The task
   */
  private Task createDummy3Task() {
    List<Record> recordList = new LinkedList<>();
    return this.createTaskWithRecordList("Dummy3 (empty)", recordList);
  }

  /**
   * Creates the fourth task with one record today.
   *
   * @return The task
   */
  private Task createDummy4Task() {
    List<Record> recordList = new LinkedList<>();

    Date now = new Date();

    recordList.add(this.createRecordWithStartAndDuration(now, 6));

    return this.createTaskWithRecordList("Dummy4 with one record today", recordList);
  }

  /**
   * Creates a task with a given name and recordList.
   *
   * @param name The name
   * @param recordList The recordList
   * @return The task
   */
  private Task createTaskWithRecordList(String name, List<Record> recordList) {
    return new TaskDummy(name, recordList);
  }
}
