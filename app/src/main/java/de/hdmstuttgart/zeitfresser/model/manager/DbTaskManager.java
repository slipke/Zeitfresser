package de.hdmstuttgart.zeitfresser.model.manager;

import android.content.Context;
import de.hdmstuttgart.zeitfresser.db.DbCalls;
import de.hdmstuttgart.zeitfresser.model.Record;
import de.hdmstuttgart.zeitfresser.model.Task;

import java.util.List;


public class DbTaskManager extends TaskManager {

  public DbCalls dbCalls = null;
  private Context context = null;

  public void setDbCalls(DbCalls dbCalls) {
    this.dbCalls = dbCalls;
  }

  public static DbTaskManager createInstance(Context context, String databaseName) {
    return new DbTaskManager(context, databaseName);
  }

  private DbTaskManager(Context context, String databaseName) {
    this.context = context;
    this.dbCalls = new DbCalls(databaseName);
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
