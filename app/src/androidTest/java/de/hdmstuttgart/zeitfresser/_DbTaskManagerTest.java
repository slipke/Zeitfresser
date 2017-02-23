package de.hdmstuttgart.zeitfresser;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import de.hdmstuttgart.zeitfresser.db.DbManager;
import de.hdmstuttgart.zeitfresser.model.Task;
import de.hdmstuttgart.zeitfresser.model.manager.DbTaskManager;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;


@RunWith(AndroidJUnit4.class)
public class _DbTaskManagerTest {

  private static DbManager dbManager;
  private static DbTaskManager taskManager;
  private static final String DATABASE_NAME = "test.db";

  @Rule
  public ActivityTestRule<MainActivity> mainActivity =
          new ActivityTestRule<>(MainActivity.class);

  /**
   * Setup the test.
   */
  @Before
  public void before() {
    dbManager = new DbManager(InstrumentationRegistry.getTargetContext(), DATABASE_NAME);
    taskManager = DbTaskManager.createInstance(InstrumentationRegistry.getTargetContext(),
            DATABASE_NAME);
  }

  /**
   * Cleanup the test.
   */
  @After
  public void after() {
    dbManager.deleteDatabase(DATABASE_NAME);
  }

  @AfterClass
  public static void teardown() {
    dbManager.deleteDatabase(DATABASE_NAME);
  }

  @Test
  public void testGetTasks() {
    List<Task> tasks = taskManager.getTaskList(); // production code tasks
    List<Task> bdmTasks = getTaskList(); // back door manipulation tasks

    for (int i = 0; i < bdmTasks.size(); i++) {
      boolean isNameEq = tasks.get(i).getName().equals(bdmTasks.get(i).getName());
      boolean isIdEq = tasks.get(i).getId() == bdmTasks.get(i).getId();
      org.junit.Assert.assertTrue(isNameEq && isIdEq);
    }
  }

  /**
   * Returns the task list.
   *
   * @return the task list
   */
  public List<Task> getTaskList() {
    SQLiteDatabase db = dbManager.getReadableDatabase();
    Cursor cursor = db.rawQuery("SELECT name, _id FROM tasks ORDER BY name ASC", null);
    ArrayList<Task> result = new ArrayList<>();

    cursor.moveToFirst();

    Constructor<Task> constructor = null;
    try {
      constructor = Task.class.getDeclaredConstructor(String.class, long.class);
      constructor.setAccessible(true);
    } catch (NoSuchMethodException exception) {
      exception.printStackTrace();
    }
    constructor.setAccessible(true);

    while (!cursor.isAfterLast()) {
      String name = cursor.getString(0);
      long id = cursor.getLong(1);

      try {
        result.add(constructor.newInstance(name, id));
      } catch (
              IllegalAccessException
                      | InstantiationException
                      | InvocationTargetException ex) {
        ex.printStackTrace();
      }
      cursor.moveToNext();
    }
    cursor.close();
    db.close();
    return result;
  }

  /**
   * the record db table is empty, ergo if the record table is after stopping the task empty, the
   * saving of the record was successful.
   */
  @Test
  public void testSaveRecordInDb() {
    List<Task> tasks = taskManager.getTaskList();
    Task task = tasks.get(0);
    taskManager.startTask(task);
    try {
      Thread.sleep(1000);
    } catch (InterruptedException exception) {
      exception.printStackTrace();
    }
    taskManager.stopTask(task);

    // back door manipulation
    boolean recordsInDbHigherZero = dbManager.getReadableDatabase()
            .rawQuery("SELECT * FROM record WHERE taskId = ?",
                    new String[]{task.getId() + ""})
            .getCount() > 0;

    org.junit.Assert.assertTrue(recordsInDbHigherZero);

    // round trip test
    Task taskFromDb = taskManager
            .dbCalls
            .getTasks(InstrumentationRegistry.getTargetContext())
            .get(0);
    org.junit.Assert.assertTrue(taskFromDb.getOverallDuration() > 0.0);
  }

}
