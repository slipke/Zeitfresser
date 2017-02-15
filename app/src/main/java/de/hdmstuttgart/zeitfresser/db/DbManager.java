package de.hdmstuttgart.zeitfresser.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DbManager extends SQLiteOpenHelper {
  public static final int DATABASE_VERSION = 1;
  public static final String DATABASE_NAME = "zeitfresser.db";

  public DbManager(Context context) {
    super(context, DATABASE_NAME, null, DATABASE_VERSION);
  }

  @Override
  public void onCreate(SQLiteDatabase sqLiteDatabase) {
    sqLiteDatabase.execSQL(DbStatements.SQL_CREATE_TASK_TABLE);
    sqLiteDatabase.execSQL(DbStatements.SQL_CREATE_RECORD_TABLE);
    insertInitialValues(sqLiteDatabase);
  }

  @Override
  public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

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

