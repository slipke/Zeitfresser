package de.hdmstuttgart.zeitfresser;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import de.hdmstuttgart.zeitfresser.model.DefaultTaskManager;
import de.hdmstuttgart.zeitfresser.model.Task;
import de.hdmstuttgart.zeitfresser.model.TaskManager;

import java.util.List;


/**
 * MainActivity ist die Aktivität, die beim Start der App bzw. beim Klick auf den Eintrag "Data
 * input" im Drawer-Menü aufgerufen wird
 */
public class MainActivity extends CommonActivity {

  protected static final TaskManager taskManager = DefaultTaskManager.createInstance();

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
     * Aufruf der Methode, welche die Inhalte der dargestellten Liste liefert.
     */
    final List<Task> list = getListElements();

    /**
     * Der Adapter bildet die Elememnte aus der Liste "list" auf Einträge des Listen-Widgets
     * in der GUI ab.
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
        final Task item = (Task) parent.getItemAtPosition(position);

        int duration = Toast.LENGTH_SHORT;


        if (taskManager.isTaskActive(item)) {
          Log.v("MainActivity", "Stopping task " + item.getName());
          taskManager.stopTask(item);
          view.setBackgroundColor(getResources().getColor(android.R.color.transparent));
          Toast toast = Toast.makeText(
                  getApplicationContext(),
                  item
                          + " stopped. Duration: "
                          + (taskManager.getOverallDurationForTask(item) / 1000.0)
                          + " s",
                  duration);
          toast.show();
        } else {
          Log.v("MainActivity", "Starting task " + item);
          taskManager.startTask(item);
          view.setBackgroundColor(getResources().getColor(R.color.colorAccent));
          Toast toast = Toast.makeText(getApplicationContext(), item + " started", duration);
          toast.show();
        }
      }

    });
  }

  /**
   * TODO: diese Methode muss anwendungsspezifisch implementiert werden. Momentan liefert sie hard
   * kodierte Namen von Tätigkeiten. Im ersten Schritt der Anwendung sollen diese Namen aus weiteren
   * Objekten, z.B. Instanzen einer Klasse Task, ausgelesen und hier zu einer ArrayList
   * zusammengebaut werden
   */
  private List<Task> getListElements() {
    return taskManager.getTaskList();
  }
}
