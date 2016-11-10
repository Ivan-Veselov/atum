package ru.spbau.mit.atum;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class WeekFilterEditorActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_week_filter_editor);
    }

    public void onButtonOKClick(View view) {
        Intent intent = new Intent();
        intent.putExtra("filter", 1);

        setResult(RESULT_OK, intent);
        finish();
    }
}
