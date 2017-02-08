package ru.spbau.mit.atum.ui;

import android.support.annotation.Nullable;
import android.widget.EditText;
import android.widget.ListView;

import ru.spbau.mit.atum.model.AbstractFiltersHolder;
import ru.spbau.mit.atum.R;
import ru.spbau.mit.atum.model.UserDefinedTimeBlocker;

/**
 * Activity, в которой пользователь редактирует или создает объект типа UserDefinedTimeBlocker.
 */
public class TimeBlockerEditorActivity extends AbstractFiltersHolderEditorActivity {
    @Override
    protected void initializeLayout() {
        setContentView(R.layout.activity_time_blocker_editor);

        nameField = (EditText) findViewById(R.id.time_blocker_editor_name_field);
        descriptionField = (EditText) findViewById(R.id.time_blocker_editor_description_field);
        timeFilterListView = (ListView) findViewById(R.id.time_blocker_editor_filter_list);
    }

    @Override
    protected @Nullable
    AbstractFiltersHolder buildFiltersHolder() {
        return new UserDefinedTimeBlocker(nameField.getText().toString(),
                                          descriptionField.getText().toString(),
                                          timeFilters);
    }
}
