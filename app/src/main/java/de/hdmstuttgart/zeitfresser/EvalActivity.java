package de.hdmstuttgart.zeitfresser;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import de.hdmstuttgart.zeitfresser.db.DbManager;
import de.hdmstuttgart.zeitfresser.model.manager.DbTaskManager;
import de.hdmstuttgart.zeitfresser.model.Task;

import java.util.Calendar;
import java.util.Date;
import java.util.List;



public class EvalActivity extends CommonActivity {

  private DatePickerDialog fromDatePicker;
  private DatePickerDialog toDatePicker;
  private boolean fromDateSet = false;
  private boolean toDateSet = false;



  private DbTaskManager taskManager;

  private EditText fromEditText;
  private EditText toEditText;

  private PieChart pieChart;

  public PieChart getPieChart() {
    return pieChart;
  }

  public DbTaskManager getTaskManager() {
    return taskManager;
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    this.taskManager = DbTaskManager.createInstance(this, DbManager.DATABASE_NAME);

    setContentView(R.layout.activity_eval);
    createNavigationBars();
    initDatePickerDialogs();

    pieChart = (PieChart) findViewById(R.id.chart);

    // Set initial data
    pieChart.setData(generatePieData());
  }

  /**
   * Creates data for the PieChart depending on selected values from the two DatePickers.
   */
  private PieData generatePieData() {
    List<Task> taskList = getTaskList();
    List<String> labelList = this.taskManager.taskListToLabelList(taskList);
    List<Entry> entryList = this.taskManager.taskListToEntryList(taskList);

    PieDataSet dataSet = new PieDataSet(entryList, "Time spent");
    dataSet.setColors(ColorTemplate.COLORFUL_COLORS); // set the color
    dataSet.setValueTextSize(16);
    return new PieData(labelList, dataSet);
  }

  /**
   * Initialize the DatePicker dialogs.
   */
  private void initDatePickerDialogs() {
    Calendar calendar = Calendar.getInstance();

    // implement from date picker dialog
    fromDatePicker = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
      @Override
      public void onDateSet(DatePicker datePicker, int year, int month, int day) {
        fromDateSet = true;
        month++; // month counting begins at 0 - strange
        Toast.makeText(getApplicationContext(), "set from date: " + day + "." + month + "."
            + year, Toast.LENGTH_LONG).show();
        fromEditText.setText(day + "." + month + "." + year);
        updatePieChart();
      }
    }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar
        .DAY_OF_MONTH));

    // implement to date picker dialog
    toDatePicker = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
      @Override
      public void onDateSet(DatePicker datePicker, int year, int month, int day) {
        toDateSet = true;
        month++;
        Toast.makeText(getApplicationContext(), "set to date: " + day + "." + month + "." + year,
            Toast.LENGTH_LONG).show();
        toEditText.setText(day + "." + month + "." + year);
        updatePieChart();
      }
    }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar
        .DAY_OF_MONTH));

    // get date edit text fields
    fromEditText = (EditText) findViewById(R.id.fromDateEditText);
    toEditText = (EditText) findViewById(R.id.toDateEditText);

    // prevent showing keyboard
    fromEditText.setInputType(InputType.TYPE_NULL);
    // register from edit text listener
    fromEditText.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        fromDatePicker.show();
      }
    });

    // prevent showing keyboard
    toEditText.setInputType(InputType.TYPE_NULL);
    // register to edit text listener
    toEditText.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        toDatePicker.show();
      }
    });
  }

  /**
   * Updates the PieChart data and refreshed the PieChart.
   */
  private void updatePieChart() {
    pieChart.setData(generatePieData());
    // Refresh pieChart
    pieChart.invalidate();
  }

  /**
   * Returns the task list depending on the date values selected.
   */
  private List<Task> getTaskList() {
    Date from = null;
    Date to = null;

    if (fromDateSet) {
      from = new Date(
          fromDatePicker.getDatePicker().getYear() - 1900,
          fromDatePicker.getDatePicker().getMonth(),
          fromDatePicker.getDatePicker().getDayOfMonth()
      );
    }

    if (toDateSet) {
      to = new Date(
          toDatePicker.getDatePicker().getYear() - 1900,
          toDatePicker.getDatePicker().getMonth(),
          toDatePicker.getDatePicker().getDayOfMonth()
      );
    }

    return this.taskManager.getFilteredTasks(from, to);
  }
}
