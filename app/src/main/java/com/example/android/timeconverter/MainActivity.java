package com.example.android.timeconverter;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Date;
import java.util.TimeZone;


public class MainActivity extends ActionBarActivity {

    int hour;
    int minute;
    int day;
    int month;
    int year;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void convertTime(View view) {
        boolean isDSTActive = false;
        boolean isMorning;

        long timeZoneId = getTimeZoneId();
        getTime();
        getDate();

        if (timeZoneId == 0) {
            hour += 16;
            isDSTActive = TimeZone.getTimeZone("GMT-8:00").inDaylightTime( new Date() );
        } else if (timeZoneId == 1) {
            hour += 15;
            isDSTActive = TimeZone.getTimeZone("GMT-7:00").inDaylightTime( new Date() );
        } else if (timeZoneId == 2) {
            isDSTActive = TimeZone.getTimeZone("GMT+8:00").inDaylightTime( new Date() );
        }

        if (hour > 23) {
            hour -= 24;
            day += 1;
        }

        if (hour > 12) {
            isMorning = false;
            hour -= 12;
        } else {
            isMorning = true;
        }

        if (isDSTActive == true) {
            hour += 1;
        }

        TextView convertedTimeText = (TextView)findViewById(R.id.converted_time);
        if (minute == 0) {
            convertedTimeText.setText("" + hour + ":" + minute + "0");
        } else {
            convertedTimeText.setText("" + hour + ":" + minute);
        }

        if (isMorning == true) {
            convertedTimeText.append(" am");
        } else {
            convertedTimeText.append(" pm");
        }

        TextView currentTimeZone = (TextView)findViewById(R.id.current_timezone);
        TimeZone tz = TimeZone.getDefault();
        currentTimeZone.setText("(currently " + tz.getID() + "time, " + tz.getDisplayName(false, TimeZone.SHORT) + ")");

        TextView convertedDateText = (TextView)findViewById(R.id.converted_date);
        convertedDateText.setText("" + day + "/" + month + "/" + year);
    }

    private long getTimeZoneId() {
        Spinner timeZoneSpinner = (Spinner)findViewById(R.id.timezones_spinner);
        return timeZoneSpinner.getSelectedItemId();
    }

    private void getDate() {
        DatePicker datePicker = (DatePicker)findViewById(R.id.datePicker);
        day = datePicker.getDayOfMonth();
        month = datePicker.getMonth() + 1; // returns 0-11
        year = datePicker.getYear();
    }

    private void getTime() {
        TimePicker timePicker = (TimePicker)findViewById(R.id.timePicker);
        hour = timePicker.getCurrentHour(); // Returns 0-23
        minute = timePicker.getCurrentMinute();
    }
}
