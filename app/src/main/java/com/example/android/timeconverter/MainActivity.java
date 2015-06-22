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
        Spinner timeZoneSpinner = (Spinner)findViewById(R.id.timezones_spinner);
        //String text = timeZoneSpinner.getSelectedItem().toString();
        long timeZoneId = timeZoneSpinner.getSelectedItemId();

        TimePicker timePicker = (TimePicker)findViewById(R.id.timePicker);
        int hour = timePicker.getCurrentHour(); // Returns 0-23
        int minute = timePicker.getCurrentMinute();

        DatePicker datePicker = (DatePicker)findViewById(R.id.datePicker);
        int day = datePicker.getDayOfMonth();
        int month = datePicker.getMonth() + 1; // returns 0-11
        int year = datePicker.getYear();

        TextView convertedTimeText = (TextView)findViewById(R.id.converted_time);
        convertedTimeText.setText("" + hour + ":" + minute);

        TextView convertedDateText = (TextView)findViewById(R.id.converted_date);
        convertedDateText.setText("" + day + "/" + month + "/" + year);
    }
}
