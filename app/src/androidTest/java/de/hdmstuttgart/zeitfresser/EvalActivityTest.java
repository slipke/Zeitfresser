package de.hdmstuttgart.zeitfresser;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

import android.support.test.espresso.assertion.ViewAssertions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;


@RunWith(AndroidJUnit4.class)
public class EvalActivityTest {

  @Rule
  public ActivityTestRule<EvalActivity> evalActivity =
          new ActivityTestRule<>(EvalActivity.class);

  @Before
  public void setUp(){
    onView(withContentDescription("Open navigation drawer")).perform(click());
    onView(withText(R.string.eval_activity)).perform(click());
  }

  @Test
  public void initialPieChartTest() {
    onView(withId(R.id.chart)).check(ViewAssertions.matches(isDisplayed()));
  }

  @Test
  public void clickFromDateTest() {
    onView(withId(R.id.fromDateEditText)).perform(click());
    //onView(withId(R.id.fromDateEditText)).perform(typeText("test text"));
  }

  @Test
  public void clickToDateTest() {
    onView(withId(R.id.toDateEditText)).perform(click());
  }
}
