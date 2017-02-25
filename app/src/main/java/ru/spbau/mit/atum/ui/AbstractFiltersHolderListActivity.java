package ru.spbau.mit.atum.ui;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ru.spbau.mit.atum.model.AbstractFiltersHolder;
import ru.spbau.mit.atum.R;

public abstract class AbstractFiltersHolderListActivity<T extends AbstractFiltersHolder>
        extends UserDataEditorActivity {
    protected static final int NEW_TASK_CODE = 0;

    protected static final int EDIT_TASK_CODE = 1;

    protected List<T> filtersHolders;

    protected ListView listView;

    protected ListOfFiltersHoldersAdapter adapter;

    protected TextView title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_task_list);

        initializeHolder();

        title = (TextView) findViewById(R.id.title);
        setTitleText();

        adapter = new ListOfFiltersHoldersAdapter(filtersHolders, this);

        listView = (ListView) findViewById(R.id.task_list);
        listView.setAdapter(adapter);
    }

    protected abstract void setTitleText();

    protected abstract void initializeHolder();

    public void onNewTaskClick(View view) {
        Intent intent = initializeIntent();
        startActivityForResult(intent, NEW_TASK_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.i("myLog", "requestCode = " + requestCode + ", resultCode = " + resultCode);
        if (requestCode == NEW_TASK_CODE && resultCode == RESULT_OK) {
            Log.i("myLog", "HeyYOU");
            T newTask = data
                    .getParcelableExtra(AbstractFiltersHolderEditorActivity.EXTRA_FILTER_HOLDER);
            filtersHolders.add(newTask);

            adapter.notifyDataSetChanged();
            saveUserData();
        }
        if (requestCode == EDIT_TASK_CODE && resultCode == RESULT_OK) {
            int position = data.getIntExtra
                    (AbstractFiltersHolderEditorActivity.EXTRA_FILTER_HOLDER_POSITION, -1);
            T filtersHolder = data.getParcelableExtra
                    (AbstractFiltersHolderEditorActivity.EXTRA_FILTER_HOLDER);
            filtersHolders.set(position, filtersHolder);

            adapter.notifyDataSetChanged();
            saveUserData();
        }
    }

    protected abstract String getScheduleStatus(AbstractFiltersHolder task);

    protected abstract boolean getScheduleStatusBool(AbstractFiltersHolder task);

    protected abstract String getTaskType(AbstractFiltersHolder task);

    protected abstract Intent initializeIntent();

    protected abstract Intent initializeEditIntent(AbstractFiltersHolder holder);

    private class ListOfFiltersHoldersAdapter extends BaseAdapter implements ListAdapter {
        private List<T> list;

        private Context context;

        public ListOfFiltersHoldersAdapter(List<T> list, Context context) {
            this.list = list;
            this.context = context;
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(final int position, View view, ViewGroup viewGroup) {
            if (view == null) {
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = inflater.inflate(R.layout.item, null);
            }

            TextView name = (TextView) view.findViewById(R.id.task_name);
            name.setText(list.get(position).getName());

            TextView isScheduled = (TextView) view.findViewById(R.id.is_scheduled);
            isScheduled.setText(getTaskType(list.get(position)));

            Button isScheduledButton = (Button)view.findViewById(R.id.item_scheduled_button);

            if (getScheduleStatusBool(list.get(position))) isScheduledButton.setVisibility(View.VISIBLE);
            else isScheduledButton.setVisibility(View.INVISIBLE);

            Button editBtn = (Button)view.findViewById(R.id.item_edit_button);
            Button deleteBtn = (Button)view.findViewById(R.id.item_delete_button);

            editBtn.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    Intent intent = initializeEditIntent(list.get(position));

                    intent.putExtra(AbstractFiltersHolderEditorActivity.EXTRA_FILTER_HOLDER,
                            list.get(position));

                    intent.putExtra(AbstractFiltersHolderEditorActivity.EXTRA_FILTER_HOLDER_POSITION, position);

                    startActivityForResult(intent, EDIT_TASK_CODE);
                    notifyDataSetChanged();
                }
            });

            deleteBtn.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    list.remove(position);
                    saveUserData();
                    notifyDataSetChanged();
                }
            });

            return view;
        }
    }

}
