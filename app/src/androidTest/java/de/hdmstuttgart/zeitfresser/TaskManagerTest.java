package de.hdmstuttgart.zeitfresser;

import static org.junit.Assert.assertEquals;

import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import de.hdmstuttgart.zeitfresser.model.manager.DbTaskManager;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;


@RunWith(AndroidJUnit4.class)
public class TaskManagerTest {

  private DbTaskManager taskManager;

  @Before
  public void before() {
    taskManager = DbTaskManager.createInstance(InstrumentationRegistry.getTargetContext(),
            "test.db");
  }

  @Rule
  public ActivityTestRule<MainActivity> mainActivity =
          new ActivityTestRule<>(MainActivity.class);

  @Test
  public void testGetFilteredTasks() throws NoSuchFieldException, IllegalAccessException {
    // Hier sollte noch ein Test mit der Datenbank rein
  }
}
