package ru.spbau.mit.atum;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class FilterEditorActivity extends AppCompatActivity {

    TimeFilter timeFilter = null;
    int ans = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter_editor);
    }

    public void onWeekFilterClick(View view) {
        Intent intent = new Intent(this, WeekFilterEditorActivity.class);
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        ans = data.getIntExtra("filter", 0);
        if (ans == 1) {
            Toast.makeText(getBaseContext(), "It's week filter!!!", Toast.LENGTH_SHORT).show();
        }
    }
}
