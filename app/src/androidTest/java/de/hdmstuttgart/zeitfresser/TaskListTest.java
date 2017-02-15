package de.hdmstuttgart.zeitfresser;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.anything;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;


@RunWith(AndroidJUnit4.class)
public class TaskListTest {

  @Rule
  public ActivityTestRule<MainActivity> mainActivity =
          new ActivityTestRule<>(MainActivity.class);

  @Before public void setUp(){
    onView(withContentDescription("Open navigation drawer")).perform(click());
    onView(withText(R.string.dataInput)).perform(click());
  }

  @Test
  public void initialTaskListTest() {
    onView(withId(R.id.listView)).check(matches(isDisplayed()));
  }

  @Test
  public void taskListClickTest() {
    onView(withId(R.id.listView)).perform(click());
  }

  @Test
  public void taskListItemClickTest() {
    //is(instanceOf(ArrayAdapter.class))
    onData(anything()).inAdapterView(withId(R.id.listView)).atPosition(0)
            .perform(click());
    //onData(anything()).inAdapterView(withId(R.id.listView)).atPosition(0).check(matches(isSelected()));
  }
}
