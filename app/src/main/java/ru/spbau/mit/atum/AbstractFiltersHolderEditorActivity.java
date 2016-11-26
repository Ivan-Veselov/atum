package ru.spbau.mit.atum;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Activity, в которой пользователь создает или редактирует одного из наследников
 * AbstractFiltersHolder.
 * Чтобы activity открыла объект на редактирование ей нужно в качестве Extra значения с ключом
 * EXTRA_FILTER_HOLDER передать этот самый объект. В противном случае пользователь будет создавать
 * новый объект с нуля.
 * Результат своей работы (инициализированный объект) activity возвращает как Extra значение с
 * ключом EXTRA_FILTER_HOLDER.
 */
public abstract class AbstractFiltersHolderEditorActivity extends AppCompatActivity {
    public static final String EXTRA_FILTER_HOLDER =
            "ru.spbau.mit.atum.AbstractFiltersHolderEditorActivity.EXTRA_FILTER_HOLDER";

    private static final String STATE_TIME_FILTERS = "TIME_FILTERS";

    protected EditText nameField;

    protected EditText descriptionField;

    protected ListView timeFilterListView;

    protected ArrayList<TimeFilter> timeFilters;

    private ArrayAdapter<TimeFilter> timeFilterListViewAdapter;

    /**
     * Классы наследники должны перегрузить этот метод. В нем они должны инициализировать элементы
     * layout.
     */
    protected abstract void initializeLayout();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initializeLayout();

        AbstractFiltersHolder holderToEdit =
                (AbstractFiltersHolder) getIntent().getSerializableExtra(EXTRA_FILTER_HOLDER);

        if (savedInstanceState != null) {
            timeFilters =
                    (ArrayList<TimeFilter>) savedInstanceState.getSerializable(STATE_TIME_FILTERS);
        } else if (holderToEdit == null) {
            timeFilters = new ArrayList<>();
        } else {
            timeFilters = new ArrayList<>(holderToEdit.getFilterList());

            nameField.setText(holderToEdit.getName());
            descriptionField.setText(holderToEdit.getDescription());
        }

        timeFilterListViewAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_2,
                android.R.id.text1,
                timeFilters);

        timeFilterListViewAdapter =
                new ArrayAdapter<TimeFilter>(this,
                        android.R.layout.simple_list_item_2,
                        android.R.id.text1,
                        timeFilters) {
                    @Override
                    public @NonNull
                    View getView(int position,
                                 View convertView,
                                 @NonNull ViewGroup parent) {
                        View view = super.getView(position, convertView, parent);
                        TextView text1 = (TextView) view.findViewById(android.R.id.text1);
                        TextView text2 = (TextView) view.findViewById(android.R.id.text2);

                        TimeFilter filter = timeFilters.get(position);
                        text1.setText(filter.getDescription());
                        text2.setText(
                                filter.getTypeDescription(AbstractFiltersHolderEditorActivity.this));
                        return view;
                    }
                };

        timeFilterListView.setAdapter(timeFilterListViewAdapter);
        registerForContextMenu(timeFilterListView);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.filter_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info =
                (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()) {
            case R.id.filter_menu_edit_item:
                return true;
            case R.id.filter_menu_delete_item:
                timeFilters.remove(info.position);
                timeFilterListViewAdapter.notifyDataSetChanged();

                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_CANCELED);
        finish();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(STATE_TIME_FILTERS, timeFilters);
    }
}

