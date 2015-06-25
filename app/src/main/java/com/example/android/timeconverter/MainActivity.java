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

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;


public class MainActivity extends ActionBarActivity {
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

        TimeZone targetTimeZone;
        long timeZoneId = getTimeZoneId();
        Calendar date = getDateAndTime();

        TimeZone currentTimeZone = TimeZone.getDefault();
        int currentTimeOffset = currentTimeZone.getOffset(GregorianCalendar.AD, date.get(Calendar.YEAR),
                date.get(Calendar.MONTH),
                date.get(Calendar.DAY_OF_MONTH),
                date.get(Calendar.DAY_OF_WEEK),
                date.get(Calendar.MILLISECOND));

        if (timeZoneId == 0) {
            targetTimeZone = TimeZone.getTimeZone("GMT-8:00");
        } else if (timeZoneId == 1) {
            targetTimeZone = TimeZone.getTimeZone("GMT-7:00");
        } else {
            targetTimeZone = TimeZone.getTimeZone("GMT+8:00");
        }

        int targetTimeOffset = targetTimeZone.getOffset(GregorianCalendar.AD,
                date.get(Calendar.YEAR),
                date.get(Calendar.MONTH),
                date.get(Calendar.DAY_OF_MONTH),
                date.get(Calendar.DAY_OF_WEEK),
                date.get(Calendar.MILLISECOND));

        int adjustment = currentTimeOffset - targetTimeOffset;
        date.add(Calendar.MILLISECOND, +adjustment);

        setTimeText(date);
        setCurrentTimeZoneText();
        setDateText(date);
    }

    private void setTimeText(Calendar date) {
        TextView convertedTimeText = (TextView)findViewById(R.id.converted_time);
        if (date.get(Calendar.MINUTE) == 0) {
            convertedTimeText.setText("" + date.get(Calendar.HOUR) + ":" + date.get(Calendar.MINUTE) + "0");
        } else if (date.get(Calendar.MINUTE) < 10) {
            convertedTimeText.setText("" + date.get(Calendar.HOUR) + ":0" + date.get(Calendar.MINUTE));
        } else {
            convertedTimeText.setText("" + date.get(Calendar.HOUR) + ":" + date.get(Calendar.MINUTE));
        }

        if (date.get(Calendar.HOUR) == 0) {
            convertedTimeText.setText("12:" + date.get(Calendar.MINUTE) + "0");
        }

        if (date.get(Calendar.AM_PM) == Calendar.AM) {
            convertedTimeText.append(" am");
        } else {
            convertedTimeText.append(" pm");
        }
    }

    private void setDateText(Calendar date) {
        TextView convertedDateText = (TextView)findViewById(R.id.converted_date);
        convertedDateText.setText("" + date.get(Calendar.DATE) + "/" + (date.get(Calendar.MONTH) + 1)
                                  + "/" + date.get(Calendar.YEAR));
    }

    private void setCurrentTimeZoneText() {
        TextView currentTimeZone = (TextView)findViewById(R.id.current_timezone);
        TimeZone tz = TimeZone.getDefault();
        currentTimeZone.setText("(currently " + tz.getID() + " timezone, " +
                                tz.getDisplayName(false, TimeZone.SHORT) + ")");
    }

    private long getTimeZoneId() {
        Spinner timeZoneSpinner = (Spinner)findViewById(R.id.timezones_spinner);
        return timeZoneSpinner.getSelectedItemId();
    }

    private Calendar getDateAndTime() {
        DatePicker datePicker = (DatePicker)findViewById(R.id.datePicker);
        int day = datePicker.getDayOfMonth();
        int month = datePicker.getMonth(); // returns 0-11
        int year = datePicker.getYear();

        TimePicker timePicker = (TimePicker)findViewById(R.id.timePicker);
        int hour = timePicker.getCurrentHour(); // Returns 0-23
        int minute = timePicker.getCurrentMinute();

        Calendar date = new GregorianCalendar();
        date.set(year, month, day, hour, minute);

        return date;
    }
}