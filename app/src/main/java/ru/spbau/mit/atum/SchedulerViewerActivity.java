package ru.spbau.mit.atum;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SchedulerViewerActivity extends AppCompatActivity {

    private ListView listView;

    private SimpleAdapter adapter;

    private ArrayList<Map<String, Object>> data = new ArrayList<>();

    private Map<String, Object> m;

    private ArrayList<UserDefinedTask> tasks;

    private static final String TASK_NAME = "task_name";

    private static final String SCHEDULE_TIME = "schedule_time";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scheduler_viewer);

        tasks = getIntent().getParcelableArrayListExtra("all tasks");
        for (UserDefinedTask task: tasks) {
            if (task.getScheduledTime() != null) {
                addNewTask(task);
            }
        }
        String[] from = { TASK_NAME, SCHEDULE_TIME };
        int[] to = { R.id.scheduled_task_name, R.id.scheduled_time };

        adapter = new SimpleAdapter(this, data, R.layout.scheduled_item, from, to);

        listView = (ListView) findViewById(R.id.scheduled_task_list);
        listView.setAdapter(adapter);
        registerForContextMenu(listView);

   //     Log.i("my_tag", ((Integer)data.size()).toString());
    }

    private void addNewTask(UserDefinedTask task) {
        m = new HashMap<>();
        m.put(TASK_NAME, task.getName());
        m.put(SCHEDULE_TIME, task.getScheduledTime().toString());

        data.add(m);
    }

}
