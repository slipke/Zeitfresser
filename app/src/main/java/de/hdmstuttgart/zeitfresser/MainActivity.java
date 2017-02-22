package de.hdmstuttgart.zeitfresser;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import de.hdmstuttgart.zeitfresser.db.DbManager;
import de.hdmstuttgart.zeitfresser.model.manager.DbTaskManager;
import de.hdmstuttgart.zeitfresser.model.Task;
import de.hdmstuttgart.zeitfresser.model.manager.TaskManager;

import java.util.List;

public class MainActivity extends CommonActivity {

  public TaskManager taskManager;

  public TaskManager getTaskManager() {
    return taskManager;
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    taskManager = DbTaskManager.createInstance(this, DbManager.DATABASE_NAME);

    setContentView(R.layout.activity_main);
    createNavigationBars();

    final ListView listview = (ListView) findViewById(R.id.listView);
    final List<Task> list = getListElements();
    final ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, list);
    listview.setAdapter(adapter);

    listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
      @Override
      public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {
        final Task item = (Task) parent.getItemAtPosition(position);
        int duration = Toast.LENGTH_SHORT;

        if (taskManager.isTaskActive(item)) {
          Log.v("MainActivity", "Stopping task " + item.getName());
          taskManager.stopTask(item);
          view.setBackgroundColor(getResources().getColor(android.R.color.transparent));
          Toast.makeText(
                  getApplicationContext(),
                  item + " stopped. Duration: "
                      + (taskManager.getOverallDurationForTask(item) / 1000.0) + " s",
                  duration)
                .show();
        } else {
          Log.v("MainActivity", "Starting task " + item);
          taskManager.startTask(item);
          view.setBackgroundColor(getResources().getColor(R.color.colorAccent));
          Toast.makeText(
                  getApplicationContext(),
                  item + " started",
                  duration)
                .show();
        }
      }
    });
  }

  /**
   * Returns a list of tasks to be displayed.
   */
  private List<Task> getListElements() {
    return taskManager.getTaskList();
  }
}
