package ru.spbau.mit.atum;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class FilterEditorActivity extends AppCompatActivity {
    public static final String EXTRA_FILTER =
            "ru.spbau.mit.atum.FilterEditorActivity.EXTRA_FILTER";

    public static final String EXTRA_FILTER_POSITION =
            "ru.spbau.mit.atum.FilterEditorActivity.EXTRA_FILTER_POSITION";

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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            timeFilter = (TimeFilter) data.getSerializableExtra("filter");
        }
    }
}
