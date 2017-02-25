package ru.spbau.mit.atum.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ru.spbau.mit.atum.model.AbstractFiltersHolder;
import ru.spbau.mit.atum.model.IntervalFilter;
import ru.spbau.mit.atum.R;import ru.spbau.mit.atum.model.TimeFilter;import ru.spbau.mit.atum.model.WeekFilter;

/**
 * Activity, в которой пользователь создает или редактирует одного из наследников
 * AbstractFiltersHolder.
 * Чтобы activity открыла объект на редактирование ей нужно в качестве Extra значения с ключом
 * EXTRA_FILTER_HOLDER передать этот самый объект. В противном случае пользователь будет создавать
 * новый объект с нуля.
 * Результат своей работы (инициализированный объект) activity возвращает как Extra значение с
 * ключом EXTRA_FILTER_HOLDER.
 * Вместе со значением EXTRA_FILTER_HOLDER в activity нужно передавать значение типа int по ключу
 * EXTRA_FILTER_HOLDER_POSITION. Это же значение по этому же ключу вернется обратно. В этом значении
 * необходимо сохранить позицию объекта (который редактируется) в массиве, чтобы в дальнейшем его
 * можно было заменить.
 */
public abstract class AbstractFiltersHolderEditorActivity extends AppCompatActivity {
    public static final String EXTRA_FILTER_HOLDER =
            "ru.spbau.mit.atum.AbstractFiltersHolderEditorActivity.EXTRA_FILTER_HOLDER";

    public static final String EXTRA_FILTER_HOLDER_POSITION =
            "ru.spbau.mit.atum.AbstractFiltersHolderEditorActivity.EXTRA_FILTER_HOLDER_POSITION";

    private static final String STATE_TIME_FILTERS = "TIME_FILTERS";

    private static final int ADD_FILTER_REQUEST = 0;

    private static final int EDIT_FILTER_REQUEST = 1;

    protected EditText nameField;

    protected EditText descriptionField;

    protected ListView timeFilterListView;

    protected ArrayList<TimeFilter> timeFilters;

    private ListOfTimeFiltersAdapter timeFilterListViewAdapter;

    /**
     * Классы наследники должны перегрузить этот метод. В нем они должны инициализировать элементы
     * layout.
     */
    protected abstract void initializeLayout();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initializeLayout();

        AbstractFiltersHolder holderToEdit = getIntent().getParcelableExtra(EXTRA_FILTER_HOLDER);

        if (savedInstanceState != null) {
            timeFilters = savedInstanceState.getParcelableArrayList(STATE_TIME_FILTERS);
        } else if (holderToEdit == null) {
            timeFilters = new ArrayList<>();
        } else {
            timeFilters = new ArrayList<>(holderToEdit.getFilterSet().getFilterList());

            nameField.setText(holderToEdit.getName());
            descriptionField.setText(holderToEdit.getDescription());
        }

        timeFilterListViewAdapter = new ListOfTimeFiltersAdapter(timeFilters, this);
        timeFilterListView.setAdapter(timeFilterListViewAdapter);
    }

    public void onClickAddFilterButton(View view) {
        Intent intent = new Intent(this, FiltersTabbedActivity.class);
        startActivityForResult(intent, ADD_FILTER_REQUEST);
    }

    @Override
    public void onBackPressed() {
        if (getParent() == null) {
            setResult(RESULT_CANCELED, null);
        } else {
            getParent().setResult(RESULT_CANCELED, null);
        }
        finish();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(STATE_TIME_FILTERS, timeFilters);
    }

    /**
     * Классы наследники должны перегрузить этот метод. В нем они должны собирать объект класса
     * наследника AbstractFiltersHolder из данных, которые ввел пользователь. Если объект собрать не
     * получается, то метод должен возвращать null.
     */
    protected abstract @Nullable AbstractFiltersHolder buildFiltersHolder();

    public void onClickApplyButton(View view) {
        AbstractFiltersHolder resultingHolder = buildFiltersHolder();
        if (resultingHolder == null) {
            return;
        }

        Intent result = new Intent();
        result.putExtra(EXTRA_FILTER_HOLDER, resultingHolder);

        int position = getIntent().getIntExtra(EXTRA_FILTER_HOLDER_POSITION, -1);
        if (position >= 0) {
            result.putExtra(EXTRA_FILTER_HOLDER_POSITION, position);
        }

        if (getParent() == null) {
            setResult(RESULT_OK, result);
        } else {
            getParent().setResult(RESULT_OK, result);
        }
        finish();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case ADD_FILTER_REQUEST:
                if (resultCode == RESULT_OK) {
                    TimeFilter filter = data.getParcelableExtra("filter");

                    timeFilters.add(filter);
                    timeFilterListViewAdapter.notifyDataSetChanged();
                }

                break;

            case EDIT_FILTER_REQUEST:
                if (resultCode == RESULT_OK) {
                    TimeFilter filter = data.getParcelableExtra(FilterEditorActivity.EXTRA_FILTER);

                    int position = data.getIntExtra(FilterEditorActivity.EXTRA_FILTER_POSITION, 0);

                    timeFilters.set(position, filter);
                    timeFilterListViewAdapter.notifyDataSetChanged();
                }

                break;

            default:
                break;
        }
    }

    private class ListOfTimeFiltersAdapter extends BaseAdapter implements ListAdapter {
        private List<TimeFilter> list;

        private Context context;

        public ListOfTimeFiltersAdapter(List<TimeFilter> list, Context context) {
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
            name.setText(list.get(position).getDescription());

            TextView filterType = (TextView) view.findViewById(R.id.is_scheduled);
            filterType.setText(
                    list.get(position).getTypeDescription(
                            AbstractFiltersHolderEditorActivity.this));

            Button editBtn = (Button)view.findViewById(R.id.item_edit_button);
            Button deleteBtn = (Button)view.findViewById(R.id.item_delete_button);

            editBtn.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    TimeFilter filter = timeFilters.get(position);
                    Intent intent = null;

                    if (filter instanceof IntervalFilter) {
                        intent = new Intent(AbstractFiltersHolderEditorActivity.this,
                                IntervalFilterEditorActivity.class);
                    } else if (filter instanceof WeekFilter) {
                        intent = new Intent(AbstractFiltersHolderEditorActivity.this,
                                WeekFilterEditorActivity.class);
                    }

                    intent.putExtra(FilterEditorActivity.EXTRA_FILTER, filter);
                    intent.putExtra(FilterEditorActivity.EXTRA_FILTER_POSITION, position);
                    startActivityForResult(intent, EDIT_FILTER_REQUEST);
                    notifyDataSetChanged();
                }
            });

            deleteBtn.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    timeFilters.remove(position);
                    notifyDataSetChanged();
                }
            });

            return view;
        }
    }
}

