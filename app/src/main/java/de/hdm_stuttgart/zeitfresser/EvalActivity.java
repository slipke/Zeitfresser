package de.hdm_stuttgart.zeitfresser;

import android.os.Bundle;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.List;


/**
 * EvalActivity ist die Aktivität, diebeim Klick auf den Eintrag
 * "Evaluation" im Drawer-Menü aufgerufen wird
 */
public class EvalActivity extends CommonActivity {

    /**
     * diese Methode muss nicht verändret werden, sie baut das Kuchendiagramm auf
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
