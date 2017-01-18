package de.hdmstuttgart.zeitfresser.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import de.hdmstuttgart.zeitfresser.model.Record;
import de.hdmstuttgart.zeitfresser.model.Task;

public class DbCalls {

  private DbManager dbManager;

  DbManager createDbManager(Context context) {
    return new DbManager(context);
  }

  private String[] getProjection(String... columns) {
    return columns;
  }

  private String getSortOrder(String column, String sort) {
    return column + " " + sort;
  }

  public ArrayList<Task> getTasks(Context context) {
    dbManager = createDbManager(context);
    ArrayList<Task> result = new ArrayList<>();
    SQLiteDatabase db = dbManager.getReadableDatabase();

    Cursor c = db.query(
            DbStatements.TABLE_NAME_TASK,
            getProjection(DbStatements.COLUMN_NAME_TITLE, DbStatements._ID),
            null,
            null,
            null,
            null,
            getSortOrder(DbStatements.COLUMN_NAME_TITLE, DbStatements.ASC)
    );

    c.moveToFirst();
    while (!c.isAfterLast()) {
      Task task = Task.fromCursor(c);
      populateTaskWithRecords(context, task);
      result.add(task);
      c.moveToNext();
    }
    c.close();
    db.close();

    return result;
  }

  private void populateTaskWithRecords(Context context, Task task) {
    dbManager = createDbManager(context);
    ArrayList<Task> result = new ArrayList<>();
    SQLiteDatabase db = dbManager.getReadableDatabase();

    Cursor c = db.query(
            DbStatements.TABLE_NAME_RECORD,
            getProjection(
                    DbStatements._ID,
                    DbStatements.COLUMN_NAME_START,
                    DbStatements.COLUMN_NAME_END
            ),
            DbStatements.COLUMN_NAME_TASKID + " = " + task.getId(),
            null,
            null,
            null,
            null
    );

    c.moveToFirst();
    while (!c.isAfterLast()) {
      Record record = Record.fromCursor(c);
      task.addRecord(record);
      c.moveToNext();
    }
    c.close();
    db.close();
  }

  public void persistRecord(Context context, Record record, Task task) {
    dbManager = createDbManager(context);
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
    SQLiteDatabase db = dbManager.getWritableDatabase();

    ContentValues values = new ContentValues();
    values.put(DbStatements.COLUMN_NAME_TASKID, task.getId());
    values.put(DbStatements.COLUMN_NAME_START, formatter.format(record.getStart()));
    values.put(DbStatements.COLUMN_NAME_END, formatter.format(record.getEnd()));

    db.insert(DbStatements.TABLE_NAME_RECORD, null, values);
  }
}
