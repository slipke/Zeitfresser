package de.hdmstuttgart.zeitfresser;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withClassName;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

import android.support.test.espresso.assertion.ViewAssertions;
import android.support.test.espresso.contrib.PickerActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.widget.DatePicker;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;

import de.hdmstuttgart.zeitfresser.model.Task;
import de.hdmstuttgart.zeitfresser.model.TaskManager;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RunWith(AndroidJUnit4.class)
public class EvalActivityTest {

  @Rule
  public ActivityTestRule<EvalActivity> evalActivity =
          new ActivityTestRule<>(EvalActivity.class);

  TaskManager taskManager;

  @Before
  public void setUp() {
    //onView(withContentDescription("Open navigation drawer")).perform(click());
    //onView(withText(R.string.eval_activity)).perform(click());
    taskManager = evalActivity.getActivity().getTaskManager();
  }

  @Test
  public void initialPieChartTest() {
    onView(withId(R.id.chart)).check(ViewAssertions.matches(isDisplayed()));
  }

  /* test if pie chart correctly shows inital data  */
  @Test
  public void initialPieChartCorrectnessTest() {
    onView(withId(R.id.chart)).check(ViewAssertions.matches(isDisplayed()));

    PieChart pieChart = evalActivity.getActivity().getPieChart();

    List<String> chartXVals = pieChart.getData().getXVals();
    List<String> taskManagerLabels = taskManager.taskListToLabelList(taskManager.getTaskList());

    List<Entry> chartEntries = pieChart.getData().getDataSet().getYVals();
    List<Entry> taskManagerEntries = taskManager.taskListToEntryList(taskManager.getTaskList());
    List<Float> chartYVals = new ArrayList<>();
    List<Float> taskManagerDurations = new ArrayList<>();

    for (Entry entry : chartEntries) {
      chartYVals.add(entry.getVal());
    }

    for (Entry entry : taskManagerEntries) {
      taskManagerDurations.add(entry.getVal());
    }

    // test pie shows all labels with values not null
    org.junit.Assert.assertTrue(taskManagerLabels.containsAll(chartXVals));

    // test if pie shows the correct values
    org.junit.Assert.assertArrayEquals(taskManagerDurations.toArray(), chartYVals.toArray());
  }

  /* test if start and end date could be altered and check if displaying date is correct*/
  @Test
  public void clickFromDateTest() {
    int year = 2017;
    int month = 1;
    int day = 10;
    onView(withId(R.id.fromDateEditText)).perform(click());
    onView(withClassName(Matchers.equalTo(DatePicker.class.getName()))).check(
            ViewAssertions.matches(isDisplayed()));
    onView(withClassName(Matchers.equalTo(DatePicker.class.getName()))).perform(
            PickerActions.setDate(year, month, day));
    onView(withText("OK")).perform(click());
    onView(withId(R.id.fromDateEditText)).check(ViewAssertions.matches(withText(day + "." + month
            + "."
            + year)));
    //onView(withId(R.id.fromDateEditText)).perform(typeText("test text"));
  }

  @Test
  public void clickToDateTest() {
    int year = 2017;
    int month = 2;
    int day = 24;
    onView(withId(R.id.toDateEditText)).perform(click());
    onView(withClassName(Matchers.equalTo(DatePicker.class.getName()))).check(
            ViewAssertions.matches(isDisplayed()));
    onView(withClassName(Matchers.equalTo(DatePicker.class.getName()))).perform(
            PickerActions.setDate(year, month, day));
    onView(withText("OK")).perform(click());
    onView(withId(R.id.toDateEditText)).check(ViewAssertions.matches(withText(day + "."
            + month + "." + year)));
  }


  /* test if pie chart shows correct data of time interval*/
  @Test
  public void chartTimeIntervalTest() {
    int year = 2017;
    int month = 2;
    int day1 = 17;
    int day2 = 19;

    onView(withId(R.id.fromDateEditText)).perform(click());
    onView(withClassName(Matchers.equalTo(DatePicker.class.getName()))).check(
            ViewAssertions.matches(isDisplayed()));
    onView(withClassName(Matchers.equalTo(DatePicker.class.getName()))).perform(
            PickerActions.setDate(year, month, day1));
    onView(withText("OK")).perform(click());

    onView(withId(R.id.toDateEditText)).perform(click());
    onView(withClassName(Matchers.equalTo(DatePicker.class.getName()))).check(
            ViewAssertions.matches(isDisplayed()));
    onView(withClassName(Matchers.equalTo(DatePicker.class.getName()))).perform(
            PickerActions.setDate(year, month, day2));
    onView(withText("OK")).perform(click());

    PieChart pieChart = evalActivity.getActivity().getPieChart();

    List<String> chartXVals = pieChart.getData().getXVals();
    List<Entry> chartEntries = pieChart.getData().getDataSet().getYVals();
    List<Float> chartYVals = new ArrayList<>();

    for (Entry entry : chartEntries) {
      chartYVals.add(entry.getVal());
    }

    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
    Date startDate = null;
    Date endDate = null;

    try {
      startDate = formatter.parse(year+"-"+month+"-"+day1);
      endDate = formatter.parse(year+"-"+month+"-"+day2);

    } catch (ParseException e) {
      e.printStackTrace();
    }
    List<Task> filteredTask = taskManager.getFilteredTasks(startDate,endDate);

    List<String> labels = new ArrayList<>();
    List<Float> durations = new ArrayList<>();
    for(Task task : filteredTask){
      durations.add(task.getOverallDuration());
      labels.add(task.getName());
    }

    org.junit.Assert.assertArrayEquals(labels.toArray(), chartXVals.toArray());
    org.junit.Assert.assertArrayEquals(durations.toArray(), chartYVals.toArray());
  }
}
