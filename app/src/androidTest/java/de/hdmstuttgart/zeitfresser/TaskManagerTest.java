package de.hdmstuttgart.zeitfresser;

import static org.junit.Assert.assertEquals;

import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import de.hdmstuttgart.zeitfresser.model.DbTaskManager;
import de.hdmstuttgart.zeitfresser.model.Record;
import de.hdmstuttgart.zeitfresser.model.Task;
import de.hdmstuttgart.zeitfresser.model.TaskManager;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.lang.reflect.Field;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;




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
    // Hier sollte noch ein Test mit der Datenbank rein
  }
}
