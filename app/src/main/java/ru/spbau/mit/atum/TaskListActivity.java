package ru.spbau.mit.atum;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TaskListActivity extends AppCompatActivity {

    private ArrayList<UserDefinedTask> userDefinedTaskList;

    private final String TASK_NAME = "task_name";
    private final String IS_SCHEDULED = "is_scheduled";
    private ListView listView;
    private SimpleAdapter adapter;
    private ArrayList<Map<String, Object>> data;
    private Map<String, Object> m;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_list);

        userDefinedTaskList = (ArrayList<UserDefinedTask>)getIntent().getSerializableExtra("tasks");

        data = new ArrayList<>();
        for (UserDefinedTask task: userDefinedTaskList) {
            m = new HashMap<>();
            m.put(TASK_NAME, task.getName());
            if (task.getScheduledTime() == null) {
                m.put(IS_SCHEDULED, "not scheduled");
            } else {
                m.put(IS_SCHEDULED, "OK");
            }

            data.add(m);
        }

        String[] from = { TASK_NAME, IS_SCHEDULED };
        int[] to = { R.id.task_name, R.id.is_scheduled };

        adapter = new SimpleAdapter(this, data, R.layout.item, from, to);

        listView = (ListView) findViewById(R.id.task_list);
        listView.setAdapter(adapter);
        registerForContextMenu(listView);

    }

    private final int DELETE_ID = 0;
    private final int EDIT_ID = 1;

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        menu.add(0, DELETE_ID, 0, "Delete");
        menu.add(0, EDIT_ID, 0, "Edit");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if (item.getItemId() == DELETE_ID) {
            AdapterView.AdapterContextMenuInfo adapterContextMenuInfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
            userDefinedTaskList.remove(adapterContextMenuInfo.position);
            data.remove(adapterContextMenuInfo.position);
            Toast.makeText(getApplicationContext(), "delete", Toast.LENGTH_LONG).show();
            adapter.notifyDataSetChanged();
            return true;
        }
        if (item.getItemId() == EDIT_ID) {
            Toast.makeText(getApplicationContext(), "edit", Toast.LENGTH_LONG).show();
            adapter.notifyDataSetChanged();
            return true;
        }

        return false;
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra("tasks", userDefinedTaskList);

        setResult(RESULT_OK, intent);
        finish();
    }
}
