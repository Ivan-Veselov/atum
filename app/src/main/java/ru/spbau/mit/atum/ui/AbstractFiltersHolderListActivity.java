package ru.spbau.mit.atum.ui;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ru.spbau.mit.atum.model.AbstractFiltersHolder;
import ru.spbau.mit.atum.R;

public abstract class AbstractFiltersHolderListActivity<T extends AbstractFiltersHolder> extends UserDataEditorActivity {
    protected static final String TASK_NAME = "task_name";

    protected static final String IS_SCHEDULED = "is_scheduled";

    protected static final int DELETE_ID = 0;

    protected static final int EDIT_ID = 1;

    protected static final int NEW_TASK_CODE = 0;

    protected static final int EDIT_TASK_CODE = 1;

    protected List<T> filtersHolders;

    protected ListView listView;

    protected SimpleAdapter adapter;

    protected ArrayList<Map<String, Object>> data;

    protected TextView title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_task_list);

        initializeHolder();

        data = new ArrayList<>();
        for (AbstractFiltersHolder task: filtersHolders) {
            addNewTask(task);
        }

        title = (TextView) findViewById(R.id.title);
        setTitleText();

        String[] from = { TASK_NAME, IS_SCHEDULED };
        int[] to = { R.id.task_name, R.id.is_scheduled };

        adapter = new SimpleAdapter(this, data, R.layout.item, from, to);

        listView = (ListView) findViewById(R.id.task_list);
        listView.setAdapter(adapter);
        registerForContextMenu(listView);

    }

    protected abstract void setTitleText();

    protected abstract void initializeHolder();

    public void onNewTaskClick(View view) {
        Intent intent = initializeIntent();
        startActivityForResult(intent, NEW_TASK_CODE);
    }

    private void addNewTask(AbstractFiltersHolder task) {
        Map<String, Object> m = new HashMap<>();
        m.put(TASK_NAME, task.getName());

        addSchedule(m, task);
        data.add(m);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.i("myLog", "requestCode = " + requestCode + ", resultCode = " + resultCode);
        if (requestCode == NEW_TASK_CODE && resultCode == RESULT_OK) {
            Log.i("myLog", "HeyYOU");
            T newTask = data
                    .getParcelableExtra(AbstractFiltersHolderEditorActivity.EXTRA_FILTER_HOLDER);
            filtersHolders.add(newTask);
            addNewTask(newTask);

            adapter.notifyDataSetChanged();
            saveUserData();
        }
        if (requestCode == EDIT_TASK_CODE && resultCode == RESULT_OK) {
            int position = data.getIntExtra
                    (AbstractFiltersHolderEditorActivity.EXTRA_FILTER_HOLDER_POSITION, -1);
            T filtersHolder = data.getParcelableExtra
                    (AbstractFiltersHolderEditorActivity.EXTRA_FILTER_HOLDER);
            filtersHolders.set(position, filtersHolder);

            Map<String, Object> m = this.data.get(position);
            m.put(TASK_NAME, filtersHolder.getName());
            addSchedule(m, filtersHolder);

            adapter.notifyDataSetChanged();
            saveUserData();
        }
    }

    protected abstract void addSchedule(Map<String, Object> m, AbstractFiltersHolder task);

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
            saveUserData();
            return true;
        }
        if (item.getItemId() == EDIT_ID) {
            AdapterView.AdapterContextMenuInfo adapterContextMenuInfo
                    = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

            Intent intent = initializeEditIntent(filtersHolders.get(adapterContextMenuInfo.position));

            intent.putExtra(AbstractFiltersHolderEditorActivity.EXTRA_FILTER_HOLDER,
                    filtersHolders.get(adapterContextMenuInfo.position));

            intent.putExtra(AbstractFiltersHolderEditorActivity.EXTRA_FILTER_HOLDER_POSITION,
                    adapterContextMenuInfo.position);

            startActivityForResult(intent, EDIT_TASK_CODE);

            adapter.notifyDataSetChanged();
            return true;
        }

        return false;
    }

    protected abstract Intent initializeIntent();

    protected abstract Intent initializeEditIntent(AbstractFiltersHolder holder);

}
