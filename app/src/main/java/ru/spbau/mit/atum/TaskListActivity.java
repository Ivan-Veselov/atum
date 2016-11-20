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

    private ArrayList<AbstractFiltersHolder> filtersHolders;

    private final String TASK_NAME = "task_name";
    private final String IS_SCHEDULED = "is_scheduled";
    private ListView listView;
    private SimpleAdapter adapter;
    private ArrayList<Map<String, Object>> data;
    private Map<String, Object> m;

    private final int DELETE_ID = 0;
    private final int EDIT_ID = 1;

    private final int NEW_TASK_CODE = 0;
    private final int EDIT_TASK_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_list);

        filtersHolders = (ArrayList<AbstractFiltersHolder>)getIntent().getSerializableExtra("filter holders");

        data = new ArrayList<>();
        for (AbstractFiltersHolder task: filtersHolders) {
            addNewTask(task);
        }

        String[] from = { TASK_NAME, IS_SCHEDULED };
        int[] to = { R.id.task_name, R.id.is_scheduled };

        adapter = new SimpleAdapter(this, data, R.layout.item, from, to);

        listView = (ListView) findViewById(R.id.task_list);
        listView.setAdapter(adapter);
        registerForContextMenu(listView);

    }

    public void onNewTaskClick(View view) {
        Intent intent = new Intent(this, TaskEditorActivity.class);
        intent.putExtra("edit task", "null");
        startActivityForResult(intent, NEW_TASK_CODE);
    }

    private void addNewTask(AbstractFiltersHolder task) {
        m = new HashMap<>();
        m.put(TASK_NAME, task.getName());

        if (task instanceof UserDefinedTask) {
            UserDefinedTask userDefinedTask = (UserDefinedTask)task;
            if (userDefinedTask.getScheduledTime() == null) {
                m.put(IS_SCHEDULED, "not scheduled");
            } else {
                m.put(IS_SCHEDULED, "OK");
            }
        }

        data.add(m);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == NEW_TASK_CODE && resultCode == RESULT_OK) {
            UserDefinedTask newTask = (UserDefinedTask)data.getSerializableExtra("edit task");
            filtersHolders.add(newTask);
            addNewTask(newTask);
            adapter.notifyDataSetChanged();
        }
    }


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
            filtersHolders.remove(adapterContextMenuInfo.position);
            data.remove(adapterContextMenuInfo.position);
            Toast.makeText(getApplicationContext(), "delete", Toast.LENGTH_LONG).show();
            adapter.notifyDataSetChanged();
            return true;
        }
        if (item.getItemId() == EDIT_ID) {
            AdapterView.AdapterContextMenuInfo adapterContextMenuInfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

            Intent intent = new Intent(this, TaskEditorActivity.class);
            intent.putExtra("edit task", filtersHolders.get(adapterContextMenuInfo.position));
            startActivityForResult(intent, EDIT_TASK_CODE);

            adapter.notifyDataSetChanged();
            return true;
        }

        return false;
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra("filter holders", filtersHolders);

        setResult(RESULT_OK, intent);
        finish();
    }
}
