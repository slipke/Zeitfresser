package de.hdmstuttgart.zeitfresser.db;

import android.provider.BaseColumns;

public class DbStatements implements BaseColumns {
  public static final String TABLE_NAME_TASK = "tasks";
  public static final String COLUMN_NAME_TITLE = "name";

  public static final String TABLE_NAME_RECORD = "record";
  public static final String COLUMN_NAME_TASKID = "taskId";
  public static final String COLUMN_NAME_START = "start";
  public static final String COLUMN_NAME_END = "end";

  private static final String TEXT_TYPE = " TEXT";
  private static final String INT_TYPE = " INTEGER";
  private static final String NUMERIC_TYPE = " NUMERIC";
  private static final String COMMA_SEP = ",";
  private static final String ID_DECL = " INTEGER PRIMARY KEY AUTOINCREMENT";
  private static final String FOREIGN_KEY_START = "FOREIGN KEY(";
  private static final String FOREIGN_KEY_REFERENCE = ") REFERENCES ";
  private static final String FOREIGN_KEY_REFERENCE_START = "(";
  private static final String FOREIGN_KEY_REFERENCE_END = ")";

  public static final String ASC = "ASC";

  static final String SQL_CREATE_TASK_TABLE =
          "CREATE TABLE " + TABLE_NAME_TASK + " ("
                  + _ID + ID_DECL + COMMA_SEP
                  + COLUMN_NAME_TITLE + TEXT_TYPE + " );";

  static final String SQL_CREATE_RECORD_TABLE =
          "CREATE TABLE " + TABLE_NAME_RECORD + " ("
                  + _ID + ID_DECL + COMMA_SEP
                  + COLUMN_NAME_TASKID + INT_TYPE + COMMA_SEP
                  + COLUMN_NAME_START + NUMERIC_TYPE + COMMA_SEP
                  + COLUMN_NAME_END + NUMERIC_TYPE + COMMA_SEP
                  + FOREIGN_KEY_START + COLUMN_NAME_TASKID + FOREIGN_KEY_REFERENCE
                  + TABLE_NAME_TASK + FOREIGN_KEY_REFERENCE_START + _ID
                  + FOREIGN_KEY_REFERENCE_END
                  + " );";
}
