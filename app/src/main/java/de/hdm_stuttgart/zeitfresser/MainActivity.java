package de.hdm_stuttgart.zeitfresser;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import de.hdm_stuttgart.zeitfresser.model.DefaultTaskManager;
import de.hdm_stuttgart.zeitfresser.model.TaskManager;

/**
 * MainActivity ist die Aktivität, die beim Start der App bzw. beim Klick auf den Eintrag
 * "Data input" im Drawer-Menü aufgerufen wird
 */
public class MainActivity extends CommonActivity {


    private static TaskManager taskManager = DefaultTaskManager.createInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /**
         * Nach dem Start der App wird die Ansicht angezeigt, die in der Ressource
         * activity_main.xml definiert ist
         */
        setContentView(R.layout.activity_main);

        /**
         * Hier wird das Drawer-Menü aufgebaut
         * Dieser Aufruf muss im Rahmen der Vorlesung nicht geändert werden
         */
        createNavigationBars();


        /**
         * Hier wird die Liste der Tätigkeiten aufgebaut. listview ist die Referenz auf das
         * Listenelement, das die Namen der Tätigkeiten enthält
         */
        final ListView listview = (ListView) findViewById(R.id.listView);

        /**
         * Aufruf der Methode, welche die Inhalte der dargestellten Liste liefert
         */
        final List<String> list = getListElements();

        /**
         * Der Adapter bildet die Elememnte aus der Liste "list" auf Einträge des Listen-Widgets
         * in der GUI ab
         */
        final ArrayAdapter adapter = new ArrayAdapter(this,
                android.R.layout.simple_list_item_1, list);
        listview.setAdapter(adapter);

        /**
         * Hier ist der Listener definiert, der auf Benutzerklicks reagiert
         */
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, final View view,
                                    int position, long id) {
                /**
                 * Abfrage, welches Listenelement geklickt wurde. "item" enthält den String, der
                 * im Listenelement dargestellt wird
                 */
                final String item = (String) parent.getItemAtPosition(position);

                /**
                 * TODO: hier ist die anwendungsspezifische Logik zu implementieren:
                 * Momentan wird als Reaktion auf den Benutzerklick ein "Toast"
                 * (Benachrichtigungsfenster) für die Dauer "duration" angezeigt, in dem
                 * als Text das angeklickte Element (item) steht
                 */
                int duration = Toast.LENGTH_SHORT;
                Toast toast = Toast.makeText(getApplicationContext(), item, duration);
                toast.show();

                /*Task task = taskList.getTaskForName(item);
                if(task.isActive()){
                    taskManager.stopTask(task);
                    task.setActive(false);
                } else {
                    taskManager.startTask(task);
                    task.setActive(true);
                }*/

                if (taskManager.isTaskActive(item)) {
                    taskManager.stopTask(item);
                } else {
                    taskManager.startTask(item);
                }

            }

        });
    }

    /**
     * TODO: diese Methode muss anwendungsspezifisch implementiert werden. Momentan liefert sie
     * hard kodierte Namen von Tätigkeiten. Im ersten Schritt der Anwendung sollen diese Namen aus
     * weiteren Objekten, z.B. Instanzen einer Klasse Task, ausgelesen und hier zu einer ArrayList
     * zusammengebaut werden
     */
    private List<String> getListElements() {
        return taskManager.getExistentTaskNamesAsList();
    }
}
