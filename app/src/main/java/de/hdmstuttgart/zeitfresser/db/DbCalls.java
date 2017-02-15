package de.hdmstuttgart.zeitfresser.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import de.hdmstuttgart.zeitfresser.model.Record;
import de.hdmstuttgart.zeitfresser.model.Task;

import java.text.SimpleDateFormat;
import java.util.ArrayList;


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

  /**
   * Fetch all tasks from database.
   *
   * @param context The current Activity context.
   * @return A list of tasks.
   */
  public ArrayList<Task> getTasks(Context context) {
    dbManager = createDbManager(context);
    ArrayList<Task> result = new ArrayList<>();
    SQLiteDatabase db = dbManager.getReadableDatabase();

    Cursor cursor = db.query(
            DbStatements.TABLE_NAME_TASK,
            getProjection(DbStatements.COLUMN_NAME_TITLE, DbStatements._ID),
            null,
            null,
            null,
            null,
            getSortOrder(DbStatements.COLUMN_NAME_TITLE, DbStatements.ASC)
    );

    cursor.moveToFirst();
    while (!cursor.isAfterLast()) {
      Task task = Task.fromCursor(cursor);
      populateTaskWithRecords(context, task);
      result.add(task);
      cursor.moveToNext();
    }
    cursor.close();
    db.close();

    return result;
  }

  private void populateTaskWithRecords(Context context, Task task) {
    dbManager = createDbManager(context);
    SQLiteDatabase db = dbManager.getReadableDatabase();

    Cursor cursor = db.query(
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

    cursor.moveToFirst();
    while (!cursor.isAfterLast()) {
      Record record = Record.fromCursor(cursor);
      task.addRecord(record);
      cursor.moveToNext();
    }
    cursor.close();
    db.close();
  }

  /**
   * Add a single {@link Record} to the database.
   *
   * @param context The current Activity context.
   * @param record The {@link Record} to persist.
   * @param task The {@link Task} the record is attached to.
   */
  public void persistRecord(Context context, Record record, Task task) {
    dbManager = createDbManager(context);
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

    ContentValues values = new ContentValues();
    values.put(DbStatements.COLUMN_NAME_TASKID, task.getId());
    values.put(DbStatements.COLUMN_NAME_START, formatter.format(record.getStart()));
    values.put(DbStatements.COLUMN_NAME_END, formatter.format(record.getEnd()));

    SQLiteDatabase db = dbManager.getWritableDatabase();
    db.insert(DbStatements.TABLE_NAME_RECORD, null, values);
  }
}
