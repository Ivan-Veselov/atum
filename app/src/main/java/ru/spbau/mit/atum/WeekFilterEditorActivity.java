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
        tvFirstMinute.setText("First free minute: " + firstMinuteHour + " hours " + firstMinuteMinute + " minutes");

        tvDuration = (TextView) findViewById(R.id.duration);
        tvDuration.setText("Expected duration: " + durationHour + " hours " + durationMinute + " minutes");

        checkBoxMonday = (CheckBox) findViewById(R.id.checkBoxMonday);
        checkBoxTuesday = (CheckBox) findViewById(R.id.checkBoxTuesday);
        checkBoxWednesday = (CheckBox) findViewById(R.id.checkBoxWednesday);
        checkBoxThursday = (CheckBox) findViewById(R.id.checkBoxThursday);
        checkBoxFriday = (CheckBox) findViewById(R.id.checkBoxFriday);
        checkBoxSaturday = (CheckBox) findViewById(R.id.checkBoxSaturday);
        checkBoxSunday = (CheckBox) findViewById(R.id.checkBoxSunday);
    }

    private int FIRST_MINUTE = 1;
    private int DURATION = 2;
    private int firstMinuteHour = 0;
    private int firstMinuteMinute = 0;
    private int durationHour = 0;
    private int durationMinute = 0;

    private TextView tvFirstMinute;
    private TextView tvDuration;

    private CheckBox checkBoxMonday;
    private CheckBox checkBoxTuesday;
    private CheckBox checkBoxWednesday;
    private CheckBox checkBoxThursday;
    private CheckBox checkBoxFriday;
    private CheckBox checkBoxSaturday;
    private CheckBox checkBoxSunday;


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
            tvFirstMinute.setText("First free minute: " + firstMinuteHour + " hours " + firstMinuteMinute + " minutes");
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
        int [] list = new int[7];
        for (int i = 0; i < 7; i++) {
            list[i] = 0;
        }
        int resSize = 0;
        if (checkBoxMonday.isChecked()) {
            list[0] = 1;
            resSize++;
        }
        if (checkBoxTuesday.isChecked()) {
            list[1] = 1;
            resSize++;
        }
        if (checkBoxWednesday.isChecked()) {
            list[2] = 1;
            resSize++;
        }
        if (checkBoxThursday.isChecked()) {
            list[3] = 1;
            resSize++;
        }
        if (checkBoxFriday.isChecked()) {
            list[4] = 1;
            resSize++;
        }
        if (checkBoxSaturday.isChecked()) {
            list[5] = 1;
            resSize++;
        }
        if (checkBoxSunday.isChecked()) {
            list[6] = 1;
            resSize++;
        }
        int [] resList = new int[resSize];
        int cur = 0;
        for (int i = 0; i < 7; i++) {
            if (list[i] == 1) {
                resList[cur] = i;
                cur++;
            }
        }

        TimeFilter timeFilter = new WeekFilter(firstMinuteHour * 60 + firstMinuteMinute,
                                               durationHour * 60 + durationMinute, new WeekMask(resList), true);

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
