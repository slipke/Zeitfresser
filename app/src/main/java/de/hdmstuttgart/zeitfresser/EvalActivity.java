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

import java.util.Calendar;
import java.util.List;
import java.util.Date;

import de.hdmstuttgart.zeitfresser.model.Task;


/**
 * EvalActivity ist die Aktivität, die beim Klick auf den Eintrag "Evaluation" im Drawer-Menü
 * aufgerufen wird.
 */
public class EvalActivity extends CommonActivity {

  private DatePickerDialog fromDatePicker;
  private DatePickerDialog toDatePicker;
  private boolean fromDateSet = false;
  private boolean toDateSet = false;

  private EditText fromEditText;
  private EditText toEditText;

  private PieChart pieChart;

  /**
   * diese Methode muss nicht verändert werden, sie baut das Kuchendiagramm auf.
   */
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_eval);
    createNavigationBars();
    initDatePickerDialogs();

    pieChart = (PieChart) findViewById(R.id.chart);

    // Set initial data
    pieChart.setData(generatePieData());
  }

  private PieData generatePieData() {
    List<Task> taskList = getTaskList();
    List<String> labelList = MainActivity.taskManager.taskListToLabelList(taskList);
    List<Entry> entryList = MainActivity.taskManager.taskListToEntryList(taskList);

    PieDataSet dataSet = new PieDataSet(entryList, "Time spent");
    dataSet.setColors(ColorTemplate.COLORFUL_COLORS); // set the color
    dataSet.setValueTextSize(16);
    return new PieData(labelList, dataSet);
  }

  private void initDatePickerDialogs() {
    Calendar calendar = Calendar.getInstance();

    // implement from date picker dialog
    fromDatePicker = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
      @Override
      public void onDateSet(DatePicker datePicker, int year, int month, int day) {
        fromDateSet = true;
        month++; // month counting begins at 0 - strange
        Toast.makeText(getApplicationContext(), "set from date: " + day + "." + month + "." + year, Toast
                .LENGTH_LONG).show();
        ;
        fromEditText.setText(day + "." + month + "." + year);
        updatePieChart();
      }
    }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));

    // implement to date picker dialog
    toDatePicker = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
      @Override
      public void onDateSet(DatePicker datePicker, int year, int month, int day) {
        toDateSet = true;
        month++;
        Toast.makeText(getApplicationContext(), "set to date: " + day + "." + month + "." + year, Toast
                .LENGTH_LONG).show();
        ;
        toEditText.setText(day + "." + month + "." + year);
        updatePieChart();
      }
    }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));

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

  private void updatePieChart() {
    // Update data (calls notifyDataSetChanged() automatically)
    pieChart.setData(generatePieData());
    // Refresh pieChart
    pieChart.invalidate();
  }

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

    return MainActivity.taskManager.getFilteredTasks(from, to);
  }
}
