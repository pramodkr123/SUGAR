package com.example.peter.sugar;

import android.app.DialogFragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Xml;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import org.xmlpull.v1.XmlSerializer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class ClosingTimeDisplayActivity extends AppCompatActivity {

    final String[] WEEKDAYS = {
            "Monday", "Tuesday", "Wednesday", "Thursday",
            "Friday", "Staurday", "Sunday"
    };

    private TimeObject[] closingTimes;
    private TextView[] timeViews;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_closing_time_display);

        timeViews = new TextView[]{ (TextView) findViewById(R.id.monday_closing_time_weekday),
                                    (TextView) findViewById(R.id.tuesday_closing_time_weekday),
                                    (TextView) findViewById(R.id.wednesday_closing_time_weekday),
                                    (TextView) findViewById(R.id.thursday_closing_time_weekday),
                                    (TextView) findViewById(R.id.friday_closing_time_weekday),
                                    (TextView) findViewById(R.id.saturday_closing_time_weekday),
                                    (TextView) findViewById(R.id.sunday_closing_time_weekday)};

        Log.d(MainActivity.LOG_TAG, "Before opening SharedPreferences");
        closingTimes = new TimeObject[7];
        SharedPreferences savedTimes = getPreferences(Context.MODE_PRIVATE);
        for(int i = 0; i < WEEKDAYS.length; i++) {
            String str = savedTimes.getString(WEEKDAYS[i], "-");
            if(str.equals("-")) {
                closingTimes[i] = null;
            } else {
                closingTimes[i] = new TimeObject(str);
            }
        }


        for(int i = 0; i < timeViews.length; i++) {
            timeViews[i].setText(toDayString(i, closingTimes[i] == null ? "" : closingTimes[i].toString()));
            final int index = i;
            timeViews[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Bundle args = new Bundle();
                    if(closingTimes[index] != null) {
                        args.putInt(MainActivity.EXTRA_HOUR_OF_DAY, closingTimes[index].getHour());
                        args.putInt(MainActivity.EXTRA_MINUTE, closingTimes[index].getMinute());
                    }
                    args.putInt(MainActivity.EXTRA_INDEX, index);

                    DialogFragment newFragment = new ClosingTimePickerFragment();
                    newFragment.setArguments(args);
                    newFragment.show(getFragmentManager(), "closing" + index);
                }
            });
        }
    }

    public void setClosingTime(int index, TimeObject time) {
        closingTimes[index] = time;
    }

    public TextView[] getWeekDayViews() { return timeViews; }

    public String toDayString(int index, String timeString) {
        switch(index) {
            case 0:
                return getString(R.string.monday, timeString);
            case 1:
                return getString(R.string.tuesday, timeString);
            case 2:
                return getString(R.string.wednesday, timeString);
            case 3:
                return getString(R.string.thursday, timeString);
            case 4:
                return getString(R.string.friday, timeString);
            case 5:
                return getString(R.string.saturday, timeString);
            case 6:
                return getString(R.string.sunday, timeString);
            default:
                return "";
        }
    }

    /* mondayButton = find...
     * ..
     * sundayButton = find...
     *
     * Button[] allDays = ...
     *
     * for(int i = 0; i < 7; i++) {
     *  siehe oben (69-83)
     * }
     */

    /* <button
     *  android:text="@string/mondayPlusTime"
     *  />
     *
     *  strings.xml:
     *  <string name="mondayPlusTime">Montag\n%s</string>
     *
     *  Activity:
     *  getString(R.string.mondayPlusTime, timeObject.toString());
     */

}
