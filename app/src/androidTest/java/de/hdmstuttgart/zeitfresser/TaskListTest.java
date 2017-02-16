package de.hdmstuttgart.zeitfresser;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.anything;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.widget.ListView;

import de.hdmstuttgart.zeitfresser.model.Task;
import de.hdmstuttgart.zeitfresser.model.TaskManager;

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

    org.junit.Assert.assertArrayEquals(taskManager.taskListToLabelList(taskManager.getTaskList())
            .toArray(), tasksFromAdapter.toArray());
  }

  /* TODO: test list item click behaviour */
  @Test
  public void taskListItemClickTest() {
    onData(anything()).inAdapterView(withId(R.id.listView)).atPosition(0).perform(click());
    //onData(anything()).inAdapterView(withId(R.id.listView)).atPosition(1).perform(click());
    try {
      Thread.sleep(3000);
    } catch (InterruptedException ex) {
      ex.printStackTrace();
    }
    //Task firstItem = (Task) listView.getAdapter().getItem(0);
    Task firstItem = taskManager.getTaskList().get(0);
    if (firstItem.isActive()) {
      onData(anything()).inAdapterView(withId(R.id.listView)).atPosition(0).perform(click());
    } else {
      onData(anything()).inAdapterView(withId(R.id.listView)).atPosition(2).perform(click());
    }


    //org.junit.Assert.assertFalse(firstItem.isActive());
    //org.junit.Assert.assertEquals(firstItem.toString(), listView.getAdapter().getItem(0)
    //.toString());

  }

  /* TODO: test list item click behaviour */
  @Test
  public void taskListItemDoubleClickTest() {
    onData(anything()).inAdapterView(withId(R.id.listView)).atPosition(0).perform(click());
    Task firstItem = (Task) listView.getAdapter().getItem(0);
    //onData(anything()).inAdapterView(withId(R.id.listView)).atPosition(0).perform(click());
    taskManager = mainActivity.getActivity().getTaskManager();
    //org.junit.Assert.assertEquals(firstItem.isActive(), taskManager.getTaskList().get(0)
    // .isActive());
    //firstItem = (Task) listView.getAdapter().getItem(0);
    org.junit.Assert.assertTrue(taskManager.getTaskList().get(0).isActive());
  }

}
