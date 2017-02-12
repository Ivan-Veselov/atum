package ru.spbau.mit.atum.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import ru.spbau.mit.atum.R;
import ru.spbau.mit.atum.model.TimeFilter;

/**
 * TODO: убрать, сама activity уже не используется.
 */
public class FilterEditorActivity extends AppCompatActivity {
    public static final String EXTRA_FILTER =
            "ru.spbau.mit.atum.ui.FilterEditorActivity.EXTRA_FILTER";

    public static final String EXTRA_FILTER_POSITION =
            "ru.spbau.mit.atum.ui.FilterEditorActivity.EXTRA_FILTER_POSITION";

    private TimeFilter timeFilter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter_editor);
    }

    public void onWeekFilterClick(View view) {
        Intent intent = new Intent(this, WeekFilterEditorActivity.class);
        startActivityForResult(intent, 1);
    }

    public void onIntervalFilterClick(View view) {
        Intent intent = new Intent(this, IntervalFilterEditorActivity.class);
        startActivityForResult(intent, 1);
    }

    public void onButtonOKClick(View view) {
        Intent intent = new Intent();
        if (timeFilter != null) {
            intent.putExtra(FilterEditorActivity.EXTRA_FILTER, timeFilter);
            setResult(RESULT_OK, intent);
        } else {
            setResult(RESULT_CANCELED);
        }
        finish();
    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_CANCELED);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            Intent intent = new Intent();
            intent.putExtra(FilterEditorActivity.EXTRA_FILTER, data.getParcelableExtra("filter"));
            setResult(RESULT_OK, intent);
            finish();
        }
    }
}
