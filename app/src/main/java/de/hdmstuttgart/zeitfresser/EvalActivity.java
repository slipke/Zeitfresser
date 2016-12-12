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


/**
 * EvalActivity ist die Aktivität, diebeim Klick auf den Eintrag
 * "Evaluation" im Drawer-Menü aufgerufen wird.
 */
public class EvalActivity extends CommonActivity {

  private DatePickerDialog fromDatePicker;
  private DatePickerDialog toDatePicker;

  private EditText fromEditText;
  private EditText toEditText;
  /**
   * diese Methode muss nicht verändret werden, sie baut das Kuchendiagramm auf.
   */
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_eval);
    createNavigationBars();

    //Content of the pie chart
    PieChart pieChart = (PieChart) findViewById(R.id.chart);

    PieDataSet dataset = new PieDataSet(getEntries(), "Time spent");

    //Set the data
    PieData data = new PieData(getLabels(), dataset); // initialize Piedata
    pieChart.setData(data); //set data into chart

    dataset.setColors(ColorTemplate.COLORFUL_COLORS); // set the color
    dataset.setValueTextSize(16);

    initDatePickerDialogs();
  }

  private void initDatePickerDialogs(){
    Calendar calendar = Calendar.getInstance();

    // implement from date picker dialog
    fromDatePicker = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
      @Override
      public void onDateSet(DatePicker datePicker, int year, int month, int day) {
        //TODO: implement logic here
        month++; // month counting begins at 0 - strange
        Toast.makeText(getApplicationContext(), "set from date: "+day+"."+month+"."+year, Toast
                .LENGTH_LONG).show();;
        fromEditText.setText(day+"."+month+"."+year);
      }
    },calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH));

    // implement to date picker dialog
    toDatePicker = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
      @Override
      public void onDateSet(DatePicker datePicker, int year, int month, int day) {
        //TODO: implement logic here
        month++;
        Toast.makeText(getApplicationContext(), "set to date: "+day+"."+month+"."+year, Toast
                .LENGTH_LONG).show();;
        toEditText.setText(day+"."+month+"."+year);
      }
    },calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH));

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
   * TODO: diese Methode muss anwendungsspezifisch überschrieben werden
   * Sie liefert eine Liste von Zahlen, die im Kuchendiagramm angezeigt werden
   * Die Zahlen werden in einem Entry-Objekt gespeichert: es enthält die darzustellende
   * Zahl sowie einen eindeutigen Index. Der Index dient zur beschreibung der Reihenfolge
   * der Entries
   * In der Anwendung entsprechen die Zahlen der Dauern, die im Kuchendiagramm angezeigt werden
   * sollen
   */
  private List<Entry> getEntries() {
    return MainActivity.taskManager.tasksAsEntryList();

  }

  /**
   * TODO: diese Methode muss anwendungsspezifisch überschrieben werden
   * Sie liefert eine Liste von Labels, mit denen die Zahlen aus der  Methode "getEntries"
   * im Kuchendiagramm beschriftet werden. Die Reihenfolge der Labels muss zu der Reihenfolge
   * der Entries passen
   */
  private List<String> getLabels() {
    return MainActivity.taskManager.getExistentTaskNamesAsList();
  }
}
