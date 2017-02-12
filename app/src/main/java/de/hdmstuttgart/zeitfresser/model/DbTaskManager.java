package de.hdmstuttgart.zeitfresser.model;

import android.content.Context;
import de.hdmstuttgart.zeitfresser.db.DbCalls;

import java.util.List;


public class DbTaskManager extends TaskManager {

  private DbCalls dbCalls = null;
  private Context context = null;


  public static DbTaskManager createInstance(Context context) {
    return new DbTaskManager(context);
  }

  private DbTaskManager(Context context) {
    this.context = context;
    this.dbCalls = new DbCalls();
  }

  @Override
  public List<Task> getTaskList() {
    return dbCalls.getTasks(this.context);
  }

  @Override
  public void stopTask(Task task) {
    // Get reference to last active record before stopping
    Record activeRecord = task.getActiveRecord();
    super.stopTask(task);
    dbCalls.persistRecord(context, activeRecord, task);
  }
}
