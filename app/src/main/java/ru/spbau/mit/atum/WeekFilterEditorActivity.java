package ru.spbau.mit.atum;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.TimePicker;

public class WeekFilterEditorActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_week_filter_editor);

        tvFirstMinute = (TextView) findViewById(R.id.first_minute);
        tvDuration = (TextView) findViewById(R.id.duration);
    }

    private int FIRST_MINUTE = 1;
    private int DURATION = 2;
    private int firstMinuteHour = 0;
    private int firstMinuteMinute = 0;
    private int durationHour = 0;
    private int durationMinute = 0;

    private TextView tvFirstMinute;
    private TextView tvDuration;

    public void onFirstMinuteClick(View view) {
        showDialog(FIRST_MINUTE);
    }

    public void onDutationClick(View view) {
        showDialog(DURATION);
    }

    protected Dialog onCreateDialog(int id) {
        if (id == FIRST_MINUTE) {
            TimePickerDialog tpd = new TimePickerDialog(this, myCallBackForFirstMinute, firstMinuteHour, firstMinuteMinute, true);
            return tpd;
        }
        if (id == DURATION) {
            TimePickerDialog tpd = new TimePickerDialog(this, myCallBackForDuration, durationHour, durationMinute, true);
            return tpd;
        }
        return super.onCreateDialog(id);
    }

    private TimePickerDialog.OnTimeSetListener myCallBackForFirstMinute = new TimePickerDialog.OnTimeSetListener() {
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            firstMinuteHour = hourOfDay;
            firstMinuteMinute = minute;
            tvFirstMinute.setText("First free minute: " + firstMinuteHour + " hours " + firstMinuteMinute + " minutes");
        }
    };

    private TimePickerDialog.OnTimeSetListener myCallBackForDuration = new TimePickerDialog.OnTimeSetListener() {
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            durationHour = hourOfDay;
            durationMinute = minute;
            tvDuration.setText("Expected duration: " + durationHour + " hours " + durationMinute + " minutes");
        }
    };

    public void onButtonOKClick(View view) {
        Intent intent = new Intent();
        int [] list = {1, 2, 3};
        TimeFilter timeFilter = new WeekFilter(firstMinuteHour * 60 + firstMinuteMinute,
                                               durationHour * 60 + durationMinute, new WeekMask(list), true);

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
