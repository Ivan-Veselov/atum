package ru.spbau.mit.atum;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

public class FilterEditorActivity extends AppCompatActivity {

    TimeFilter timeFilter = null;

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
            timeFilter = (TimeFilter)data.getSerializableExtra("filter");
        }
    }
}
