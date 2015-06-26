package com.example.android.timeconverter;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
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
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        Spinner sourceTimeZoneSpinner = (Spinner)findViewById(R.id.from_timezones_spinner);
        sourceTimeZoneSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapterView, View view,
                                       int position, long id) {
                convertTime();
            }

            public void onNothingSelected(AdapterView<?> adapterView) {
                return;
            }
        });

        Spinner targetTimeZoneSpinner = (Spinner)findViewById(R.id.to_timezones_spinner);
        setDefaultTargetSpinnerSelection(targetTimeZoneSpinner);
        targetTimeZoneSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapterView, View view,
                                       int position, long id) {
                convertTime();
            }

            public void onNothingSelected(AdapterView<?> adapterView) {
                return;
            }
        });

        TimePicker timePicker = (TimePicker)findViewById(R.id.timePicker);
        timePicker.setCurrentMinute(0);
        timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {

            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                convertTime();
            }
        });

        DatePicker datePicker = (DatePicker)findViewById(R.id.datePicker);
        datePicker.init(datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth(),
                        new DatePicker.OnDateChangedListener() {

            @Override
            public void onDateChanged(DatePicker view, int year, int month, int day) {
                convertTime();
            }
        });


    }

    private void setDefaultTargetSpinnerSelection(Spinner targetTimeZoneSpinner) {
        TimeZone currentTimeZone = TimeZone.getDefault();

        if (currentTimeZone.getDisplayName(false, TimeZone.SHORT).equals("GMT-08:00")) {
            targetTimeZoneSpinner.setSelection(0);
        } else if (currentTimeZone.getDisplayName(false, TimeZone.SHORT).equals("GMT-07:00")) {
            targetTimeZoneSpinner.setSelection(1);
        } else {
            targetTimeZoneSpinner.setSelection(2);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_main, menu);
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

    public void convertTime() {
        Calendar date = getDateAndTime();

        int targetTimeOffset = getTargetTimeOffset();
        int sourceTimeOffset = getSourceTimeOffset(date);

        int adjustment = targetTimeOffset - sourceTimeOffset;
        date.add(Calendar.MILLISECOND, +adjustment);

        setTimeText(date);
        setCurrentTimeZoneText();
        setDateText(date);
    }

    private int getSourceTimeOffset(Calendar date) {
        Spinner timeZoneSpinner = (Spinner)findViewById(R.id.from_timezones_spinner);
        long spinnerSelectionId = timeZoneSpinner.getSelectedItemId();

        TimeZone sourceTimeZone = getTimeZone(spinnerSelectionId);

        return sourceTimeZone.getOffset(GregorianCalendar.AD,
                date.get(Calendar.YEAR),
                date.get(Calendar.MONTH),
                date.get(Calendar.DAY_OF_MONTH),
                date.get(Calendar.DAY_OF_WEEK),
                date.get(Calendar.MILLISECOND));
    }

    private int getTargetTimeOffset() {
        Spinner timeZoneSpinner = (Spinner)findViewById(R.id.to_timezones_spinner);
        long spinnerSelectionId = timeZoneSpinner.getSelectedItemId();

        Calendar today = Calendar.getInstance();

        TimeZone targetTimeZone = getTimeZone(spinnerSelectionId);

        return targetTimeZone.getOffset(GregorianCalendar.AD, today.get(Calendar.YEAR),
                today.get(Calendar.MONTH),
                today.get(Calendar.DAY_OF_MONTH),
                today.get(Calendar.DAY_OF_WEEK),
                today.get(Calendar.MILLISECOND));
    }

    private TimeZone getTimeZone(long spinnerSelectionId) {
        TimeZone timeZone;
        if (spinnerSelectionId == 0) {
            timeZone = TimeZone.getTimeZone("GMT-8:00");
        } else if (spinnerSelectionId == 1) {
            timeZone = TimeZone.getTimeZone("GMT-7:00");
        } else {
            timeZone = TimeZone.getTimeZone("GMT+8:00");
        }
        return timeZone;
    }

    private void setTimeText(Calendar date) {
        TextView convertedTimeText = (TextView)findViewById(R.id.converted_time);

        int hour = date.get(Calendar.HOUR);
        int minute = date.get(Calendar.MINUTE);

        if (hour == 0) {
            hour = 12;
        }

        if (minute == 0) {
            convertedTimeText.setText(hour + ":" + minute + "0");
        } else if (minute < 10) {
            convertedTimeText.setText(hour + ":0" + minute);
        } else {
            convertedTimeText.setText(hour + ":" + minute);
        }

        if (date.get(Calendar.AM_PM) == Calendar.AM) {
            convertedTimeText.append(" am");
        } else {
            convertedTimeText.append(" pm");
        }
    }

    private void setDateText(Calendar date) {
        TextView convertedDateText = (TextView)findViewById(R.id.converted_date);
        convertedDateText.setText("" + date.get(Calendar.DATE) + "/"
                                  + (date.get(Calendar.MONTH) + 1)
                                  + "/" + date.get(Calendar.YEAR));
    }

    private void setCurrentTimeZoneText() {
        TextView currentTimeZone = (TextView)findViewById(R.id.current_timezone);
        TimeZone tz = TimeZone.getDefault();
        currentTimeZone.setText("(you are in the " + tz.getID() + " timezone, " +
                                tz.getDisplayName(false, TimeZone.SHORT) + ")");
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