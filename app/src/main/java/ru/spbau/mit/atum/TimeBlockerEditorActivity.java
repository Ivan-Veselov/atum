package ru.spbau.mit.atum;

import android.content.Intent;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;

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

    public void onClickApplyButton(View view) {
        UserDefinedTimeBlocker resultingTimeBlocker =
                new UserDefinedTimeBlocker(nameField.getText().toString(),
                                           descriptionField.getText().toString(),
                                           timeFilters);

        Intent result = new Intent();
        result.putExtra(EXTRA_FILTER_HOLDER, resultingTimeBlocker);
        setResult(RESULT_OK, result);
        finish();
    }
}
