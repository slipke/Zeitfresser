package de.hdmstuttgart.zeitfresser;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.is;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.widget.ListView;

import de.hdmstuttgart.zeitfresser.model.Task;
import de.hdmstuttgart.zeitfresser.model.manager.TaskManager;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;

@RunWith(AndroidJUnit4.class)
public class TaskListTest {

  @Rule
  public ActivityTestRule<MainActivity> mainActivity =
          new ActivityTestRule<>(MainActivity.class);

  private TaskManager taskManager;
  private ListView listView;

  @Before
  public void setUp() {
    //onView(withContentDescription("Open navigation drawer")).perform(click());
    //onView(withText(R.string.dataInput)).perform(click());
    taskManager = mainActivity.getActivity().getTaskManager();

    /*taskManager1 = mainActivity.getActivity().getTaskManager();
    try {
      Field field = mainActivity.getActivity().getClass().getDeclaredField
              ("taskManager");
      field.setAccessible(true);
      taskManager = ((TaskManager) field.get(taskManager1));
    } catch (IllegalAccessException e) {
      e.printStackTrace();
    } catch (NoSuchFieldException e) {
      e.printStackTrace();
    }*/

    listView = (ListView) mainActivity.getActivity().findViewById(R.id.listView);
  }

  @Test
  public void initialTaskListTest() {
    onView(withId(R.id.listView)).check(matches(isDisplayed()));
  }

  /* test inital correctness of tasklist */
  @Test
  public void initialTaskListCorrectnessTest() {
    onView(withId(R.id.listView)).check(matches(isDisplayed()));
    taskManager.getTaskList();
    // get all items of list view

    List<String> tasksFromAdapter = new ArrayList<>();
    for (int i = 0; i < listView.getAdapter().getCount(); i++) {
      tasksFromAdapter.add(((Task) listView.getAdapter().getItem(i)).getName());
    }

    org.junit.Assert.assertArrayEquals(taskManager.asNamesList(taskManager.getTaskList())
            .toArray(), tasksFromAdapter.toArray());
  }

  /* test list item click behaviour */
  @Test
  public void taskListItemStartTaskTest() {
    onData(anything()).inAdapterView(withId(R.id.listView)).atPosition(0).perform(click());

    Task firstItem = (Task) listView.getAdapter().getItem(0);
    org.junit.Assert.assertTrue(taskManager.isTaskActive(firstItem));
  }

  /* test list item click behaviour */
  @Test
  public void taskListItemStartAndStopTaskTest() {
    onData(anything()).inAdapterView(withId(R.id.listView)).atPosition(0).perform(click(),click());
    Task firstItem = (Task) listView.getAdapter().getItem(0);
    org.junit.Assert.assertFalse(taskManager.isTaskActive(firstItem));
  }

}
