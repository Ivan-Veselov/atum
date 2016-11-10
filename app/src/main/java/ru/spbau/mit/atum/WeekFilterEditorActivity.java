package ru.spbau.mit.atum;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class WeekFilterEditorActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_week_filter_editor);
    }

    public void onButtonOKClick(View view) {
        Intent intent = new Intent();
        int [] list = {1, 2, 3};
        TimeFilter timeFilter = new WeekFilter(1, 1, new WeekMask(list), true);

        intent.putExtra("filter", timeFilter);

        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_CANCELED, null);
        finish();
    }
}
