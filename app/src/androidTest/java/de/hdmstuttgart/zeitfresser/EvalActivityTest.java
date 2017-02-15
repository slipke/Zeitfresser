package de.hdmstuttgart.zeitfresser;

import android.app.DatePickerDialog;
import android.support.test.espresso.assertion.ViewAssertions;
import android.support.test.espresso.contrib.PickerActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.View;
import android.widget.DatePicker;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withClassName;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
public class EvalActivityTest {

  @Rule
  public ActivityTestRule<EvalActivity> evalActivity =
          new ActivityTestRule<>(EvalActivity.class);

  @Before
  public void setUp(){
    //onView(withContentDescription("Open navigation drawer")).perform(click());
    //onView(withText(R.string.eval_activity)).perform(click());
  }

  @Test
  public void initialPieChartTest() {
    onView(withId(R.id.chart)).check(ViewAssertions.matches(isDisplayed()));
  }

  /* test if start and end date could be altered and check if displaying date is correct*/

  @Test
  public void clickFromDateTest() {
    int year = 2017;
    int month = 1;
    int day = 10;
    onView(withId(R.id.fromDateEditText)).perform(click());
    onView(withClassName(Matchers.equalTo(DatePicker.class.getName()))).check
            (ViewAssertions.matches(isDisplayed()));
    onView(withClassName(Matchers.equalTo(DatePicker.class.getName()))).perform
            (PickerActions.setDate(2017, 1, 10));
    onView(withText("OK")).perform(click());
    onView(withId(R.id.fromDateEditText)).check(ViewAssertions.matches(withText(day+"."+month+"."
            +year)));
    //onView(withId(R.id.fromDateEditText)).perform(typeText("test text"));
  }

  @Test
  public void clickToDateTest() {
    int year = 2017;
    int month = 2;
    int day = 24;
    onView(withId(R.id.toDateEditText)).perform(click());
    onView(withClassName(Matchers.equalTo(DatePicker.class.getName()))).check
            (ViewAssertions.matches(isDisplayed()));
    onView(withClassName(Matchers.equalTo(DatePicker.class.getName()))).perform
            (PickerActions.setDate(year, month, day));
    onView(withText("OK")).perform(click());
    onView(withId(R.id.toDateEditText)).check(ViewAssertions.matches(withText(day+"."+month+"."
            +year)));
  }
}
