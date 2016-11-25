package ru.spbau.mit.atum;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.TimePicker;

public class WeekFilterEditorActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_week_filter_editor);

        tvFirstMinute = (TextView) findViewById(R.id.first_minute);
        tvFirstMinute.setText("First minute: " + firstMinuteHour + " hours " + firstMinuteMinute + " minutes");

        tvDuration = (TextView) findViewById(R.id.duration);
        tvDuration.setText("Expected duration: " + durationHour + " hours " + durationMinute + " minutes");

        checkBoxList[0] = (CheckBox) findViewById(R.id.checkBoxMonday);
        checkBoxList[1] = (CheckBox) findViewById(R.id.checkBoxTuesday);
        checkBoxList[2] = (CheckBox) findViewById(R.id.checkBoxWednesday);
        checkBoxList[3] = (CheckBox) findViewById(R.id.checkBoxThursday);
        checkBoxList[4] = (CheckBox) findViewById(R.id.checkBoxFriday);
        checkBoxList[5] = (CheckBox) findViewById(R.id.checkBoxSaturday);
        checkBoxList[6] = (CheckBox) findViewById(R.id.checkBoxSunday);

        isFreeTime = (CheckBox) findViewById(R.id.isFreeTime);
    }

    private int FIRST_MINUTE = 1;
    private int DURATION = 2;
    private int firstMinuteHour = 0;
    private int firstMinuteMinute = 0;
    private int durationHour = 0;
    private int durationMinute = 0;

    private CheckBox isFreeTime;

    private TextView tvFirstMinute;
    private TextView tvDuration;

    private final int DAYS_IN_WEEK = 7;
    private CheckBox [] checkBoxList = new CheckBox[DAYS_IN_WEEK];

    public void onFirstMinuteClick(View view) {
        showDialog(FIRST_MINUTE);
    }

    public void onDutationClick(View view) {
        showDialog(DURATION);
    }

    protected Dialog onCreateDialog(int id) {
        if (id == FIRST_MINUTE) {
            TimePickerDialog tpd = new TimePickerDialog(this, CallBackForFirstMinute, firstMinuteHour, firstMinuteMinute, true);
            return tpd;
        }
        if (id == DURATION) {
            TimePickerDialog tpd = new TimePickerDialog(this, CallBackForDuration, durationHour, durationMinute, true);
            return tpd;
        }
        return super.onCreateDialog(id);
    }

    private TimePickerDialog.OnTimeSetListener CallBackForFirstMinute = new TimePickerDialog.OnTimeSetListener() {
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            firstMinuteHour = hourOfDay;
            firstMinuteMinute = minute;
            tvFirstMinute.setText("First minute: " + firstMinuteHour + " hours " + firstMinuteMinute + " minutes");
        }
    };

    private TimePickerDialog.OnTimeSetListener CallBackForDuration = new TimePickerDialog.OnTimeSetListener() {
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            durationHour = hourOfDay;
            durationMinute = minute;
            tvDuration.setText("Expected duration: " + durationHour + " hours " + durationMinute + " minutes");
        }
    };

    public void onButtonOKClick(View view) {
        Intent intent = new Intent();
        int resSize = 0;
        for (int i = 0; i < DAYS_IN_WEEK; i++) {
            if (checkBoxList[i].isChecked()) {
                resSize++;
            }
        }
        int [] resList = new int[resSize];
        int cur = 0;
        for (int i = 0; i < 7; i++) {
            if (checkBoxList[i].isChecked()) {
                resList[cur] = i;
                cur++;
            }
        }

        TimeFilter timeFilter = new WeekFilter(firstMinuteHour * 60 + firstMinuteMinute,
                                               durationHour * 60 + durationMinute,
                                               new WeekMask(resList), isFreeTime.isChecked());

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
