package de.hdmstuttgart.zeitfresser.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DbManager extends SQLiteOpenHelper {
  public static final int DATABASE_VERSION = 1;
  public static final String DATABASE_NAME = "zeitfresser.db";
  private Context context = null;

  public DbManager(Context context) {
    super(context, DATABASE_NAME, null, DATABASE_VERSION);
  }

  public DbManager(Context context, String dbName) {
    super(context, dbName, null, DATABASE_VERSION);
    this.context = context;
  }

  @Override
  public void onCreate(SQLiteDatabase sqLiteDatabase) {
    sqLiteDatabase.execSQL(DbStatements.SQL_CREATE_TASK_TABLE);
    sqLiteDatabase.execSQL(DbStatements.SQL_CREATE_RECORD_TABLE);
    insertInitialValues(sqLiteDatabase);
  }

  /**
   * Resets the database.
   *
   * @param dbName Name of the db
   */
  public void resetDatabase(String dbName) {
    if (this.context != null) {
      deleteDatabase(dbName);
      //onCreate(context.openOrCreateDatabase(dbName,Context.MODE_APPEND,null));
    }
  }

  /**
   * deletes a database.
   *
   * @param dbName Name of the db
   */
  public void deleteDatabase(String dbName) {
    if (this.context != null) {
      getWritableDatabase().execSQL("DROP TABLE " + DbStatements.TABLE_NAME_RECORD);
      getWritableDatabase().execSQL("DROP TABLE " + DbStatements.TABLE_NAME_TASK);
      this.context.deleteDatabase(dbName);
    }
  }

  @Override
  public void onUpgrade(SQLiteDatabase sqLiteDatabase, int version, int i1) {

  }

  private void insertInitialValues(SQLiteDatabase db) {
    ContentValues values = new ContentValues();
    values.put(DbStatements.COLUMN_NAME_TITLE, "Vorlesungen");
    long newRowId;
    newRowId = db.insert(
            DbStatements.TABLE_NAME_TASK,
            DbStatements.COLUMN_NAME_TITLE,
            values);

    values = new ContentValues();
    values.put(DbStatements.COLUMN_NAME_TITLE, "Mails");
    newRowId = db.insert(
            DbStatements.TABLE_NAME_TASK,
            DbStatements.COLUMN_NAME_TITLE,
            values);
    values = new ContentValues();
    values.put(DbStatements.COLUMN_NAME_TITLE, "Internet");
    newRowId = db.insert(
            DbStatements.TABLE_NAME_TASK,
            DbStatements.COLUMN_NAME_TITLE,
            values);
    values = new ContentValues();
    values.put(DbStatements.COLUMN_NAME_TITLE, "Lesen");
    newRowId = db.insert(
            DbStatements.TABLE_NAME_TASK,
            DbStatements.COLUMN_NAME_TITLE,
            values);
    values = new ContentValues();
    values.put(DbStatements.COLUMN_NAME_TITLE, "Spielen");
    newRowId = db.insert(
            DbStatements.TABLE_NAME_TASK,
            DbStatements.COLUMN_NAME_TITLE,
            values);
    values = new ContentValues();
    values.put(DbStatements.COLUMN_NAME_TITLE, "Putzen");
    newRowId = db.insert(
            DbStatements.TABLE_NAME_TASK,
            DbStatements.COLUMN_NAME_TITLE,
            values);


  }


}

