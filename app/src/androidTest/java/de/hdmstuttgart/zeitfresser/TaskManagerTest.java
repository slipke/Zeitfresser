package de.hdmstuttgart.zeitfresser;

import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.lang.reflect.Field;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import de.hdmstuttgart.zeitfresser.model.DbTaskManager;
import de.hdmstuttgart.zeitfresser.model.DefaultTaskManager;
import de.hdmstuttgart.zeitfresser.model.Record;
import de.hdmstuttgart.zeitfresser.model.Task;
import de.hdmstuttgart.zeitfresser.model.TaskManager;

import static org.junit.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class TaskManagerTest {

  private DbTaskManager taskManager;

  @Before
  public void before() {
    taskManager = DbTaskManager.createInstance(InstrumentationRegistry.getTargetContext());
  }

  @Rule
  public ActivityTestRule<MainActivity> mainActivity =
          new ActivityTestRule<>(MainActivity.class);

  @Test
  public void testGetFilteredTasks() throws NoSuchFieldException, IllegalAccessException {
    // @TODO Mocks + Spies?
    // Now + 2h
    Date from = new Date();
    from.setTime(from.getTime() + (2 * 60 * 60 * 1000));
    // Now + 4h
    Date to = new Date();
    to.setTime(to.getTime() + (4 * 60 * 60 * 1000));

    // Create records
    // Record not in test time
    // Now + 1h
    Date record1From = new Date();
    record1From.setTime(record1From.getTime() + (60 * 60 * 1000));
    // Now + 1h 5min
    Date record1To = new Date();
    record1To.setTime(record1To.getTime() + (65 * 60 * 1000));
    Record record1 = Record.withStartAndEnd(record1From, record1To);

    // Record in test time
    // Now + 2h 5min
    Date record2From = new Date();
    record2From.setTime(record2From.getTime() + (125 * 60 * 1000));
    // Now + 2h 10min
    Date record2To = new Date();
    record2To.setTime(record2To.getTime() + (130 * 60 * 1000));
    Record record2 = Record.withStartAndEnd(record2From, record2To);

    // Create RecordList
    List<Record> recordList = new LinkedList<>();
    recordList.add(record1);
    recordList.add(record2);

    // Create Task
    Task task = Task.withName("Dummy");
    Field recordsField = Task.class.getDeclaredField("records");
    recordsField.setAccessible(true);
    recordsField.set(task, recordList);

    // Create TaskList
    List<Task> taskList = new LinkedList<>();
    taskList.add(task);

    // Add taskList to taskManager
    Field taskListField = TaskManager.class.getDeclaredField("taskList");
    taskListField.setAccessible(true);
    taskListField.set(taskManager, taskList);

    List<Task> taskListToCheck = taskManager.getFilteredTasks(from, to);
    assertEquals(1, taskListToCheck.size());
  }
}
