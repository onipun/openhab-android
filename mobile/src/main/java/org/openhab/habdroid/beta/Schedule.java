package org.openhab.habdroid.beta;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import org.openhab.habdroid.AddScheduleActivityFragment;
import org.openhab.habdroid.DisplayScheduleActivityFragment;
import org.openhab.habdroid.ItemsMiddleware;
import org.openhab.habdroid.LoginDbInitiate;
import org.openhab.habdroid.R;
import org.openhab.habdroid.ScheduleFragment;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class Schedule extends AppCompatActivity {
    private static final String TAG = Schedule.class.getSimpleName();
    protected FragmentManager fragmentManager;
    protected Fragment fragment = null;
    protected Fragment addFragment = null;
    private LoginDbInitiate dbA;
    private Cursor c;
    private String[] swName;
    private String[] nameL = new String[100];
    private String[] dayL = new String[100];
    private ProgressDialog dialog;
    private int loopS = 0;
    private String currDay, currDate, currTime, yestDate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        dbA = dbA.getsLoginDbInitiate(getApplicationContext());
        loadSwitchName();
        setCurrentDateTime();


        fragmentManager = getSupportFragmentManager();
        fragment = new DisplayScheduleActivityFragment();
        fragmentManager.beginTransaction().add(R.id.fragment_container, fragment).commit();


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                addFragment = new AddScheduleActivityFragment();
                fragmentManager.beginTransaction().replace(R.id.fragment_container,addFragment).addToBackStack(null).commit();


                /*Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/

            }
        });

        FloatingActionButton fabSuggest = (FloatingActionButton) findViewById(R.id.fabSuggestion);
        fabSuggest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new MyAsyncTask().execute();
            }
        });




        ItemsMiddleware.getInstance(this.getApplicationContext()).fetchItems();

    }

    @Override
    public void onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount() > 0 ){
            getFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
        }
    }



    private Date yesterday() {
        final Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -1);
        return cal.getTime();
    }

    private String getYestDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        return dateFormat.format(yesterday());
    }

    private void setCurrentDateTime() {
        int day;
        Calendar c = Calendar.getInstance();
        day = c.get(Calendar.DAY_OF_WEEK);
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat timeFormat = new SimpleDateFormat("HHmm");

        currDate = dateFormat.format(new Date());
        currTime = timeFormat.format(new Date());
        yestDate = getYestDate();

        switch (day) {
            case 1:  currDay = "sunday";
                break;
            case 2:  currDay = "monday";
                break;
            case 3:  currDay = "tuesday";
                break;
            case 4:  currDay = "wednesday";
                break;
            case 5:  currDay = "thursday";
                break;
            case 6:  currDay = "friday";
                break;
            case 7:  currDay = "saturday";
                break;
            default: currDay = "invalid day";
                break;
        }
    }

    private String calculateDiffTime(String timeon, String timeoff){
        String total = "";
        String newHours;
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HHmm");
            Date date1 = simpleDateFormat.parse(timeon);
            Date date2 = simpleDateFormat.parse(timeoff);

            long difference = date2.getTime() - date1.getTime();
            int days = (int) (difference / (1000*60*60*24));
            int hours = (int) ((difference - (1000*60*60*24*days)) / (1000*60*60));
            int min = (int) (difference - (1000*60*60*24*days) - (1000*60*60*hours)) / (1000*60);
            //hours = (hours < 0 ? -hours : hours);

            if (hours == 0){
                newHours = "";
            }else{
                newHours = String.valueOf(hours);
            }


            if (min < 10){
                total = newHours+"0"+min;
            }else{
                total = newHours+""+min;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return total;
    }

    private class MyAsyncTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(Schedule.this);
            dialog.setTitle("Getting Information");
            dialog.setMessage("Please wait...");
            dialog.setCancelable(false);
            dialog.show();
            Log.d(TAG, "onPreExecute: running");
            
        }

        protected Void doInBackground(Void... args) {
            // do background work here
            runSuggestion(swName);
            Log.d(TAG, "doInBackground: runnning");
            return null;
        }

        protected void onPostExecute(Void result) {
            // do UI work here
            Log.d(TAG, "onPostExecute: running");
            if (dialog.isShowing()) {
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {
                        dialog.dismiss();
                        if(loopS == 0){
                            nothingDialog();
                            loopS = 0;
                            dayL = new String[100];
                            nameL = new String[100];
                        }else {
                            for (int i = 0; i < loopS; i++) {
                                setScheduleDialog(dayL[i], nameL[i]);
                            }
                            loopS = 0;
                            dayL = new String[100];
                            nameL = new String[100];
                        }
                    }
                }, 1000);
            }
            super.onPostExecute(result);
        }
    }

    private void loadSwitchName() {
        List<String> lables = dbA.getAllLabelsName();

        swName = new String[lables.size()];
        swName = lables.toArray(swName);
    }

    private void runSuggestion(final String name[]){
        Log.d(TAG, "runSuggestion: running");
        for (int i=0; i<name.length; i++) {
            getNightData(name[i]);
            getEveningData(name[i]);
            getDayData(name[i]);
            getMorningData(name[i]);
        }
    }

    private void getMorningData(final String name){
        Log.d(TAG, "getMorningData: running");
        int count = 0;
        int idA[] = new int[100];
        int totalAll = 0;

        c = dbA.getSwitchDataMorning(yestDate,name);
        String day = "Morning";
        try {
            if (c.moveToFirst()) {
                do {
                    int id = c.getInt(c.getColumnIndex("_id"));
                    String timeon = c.getString(c.getColumnIndex("timeOn"));
                    String timeoff = c.getString(c.getColumnIndex("timeOff"));

                    String total = calculateDiffTime(timeon, timeoff);

                    idA[count] = id;
                    totalAll += Integer.parseInt(total);
                    count++;
                } while (c.moveToNext());
            }
        }catch (SQLException e){
            e.printStackTrace();
        }

        if (totalAll > 200){
            //setScheduleDialog(day,name);
            nameL[loopS] = name;
            dayL[loopS] = day;
            loopS++;

        } else {
            //nothingDialog(day,name);
        }
    }

    private void getDayData(final String name){
        Log.d(TAG, "getDayData: running");
        int count = 0;
        int idA[] = new int[100];
        int totalAll = 0;
        String day = "Afternoon";
        c = dbA.getSwitchDataDay(yestDate,name);
        try {
            if (c.moveToFirst()) {
                do {
                    int id = c.getInt(c.getColumnIndex("_id"));
                    String timeon = c.getString(c.getColumnIndex("timeOn"));
                    String timeoff = c.getString(c.getColumnIndex("timeOff"));

                    String total = calculateDiffTime(timeon, timeoff);

                    idA[count] = id;
                    totalAll += Integer.parseInt(total);
                    count++;
                } while (c.moveToNext());
            }
        }catch (SQLException e){
            e.printStackTrace();
        }

        if (totalAll > 100){
            //setScheduleDialog(day,name);
            nameL[loopS] = name;
            dayL[loopS] = day;
            loopS++;
        } else {
            //nothingDialog(day,name);
        }
    }

    private void getEveningData(final String name){
        int count = 0;
        int idA[] = new int[100];
        int totalAll = 0;
        String day = "Evening";
        c = dbA.getSwitchDataEvening(yestDate,name);
        try {
            if (c.moveToFirst()) {
                do {
                    int id = c.getInt(c.getColumnIndex("_id"));
                    String timeon = c.getString(c.getColumnIndex("timeOn"));
                    String timeoff = c.getString(c.getColumnIndex("timeOff"));

                    String total = calculateDiffTime(timeon, timeoff);

                    idA[count] = id;
                    totalAll += Integer.parseInt(total);
                    count++;
                } while (c.moveToNext());
            }
        }catch (SQLException e){
            e.printStackTrace();
        }catch (Exception e){
            e.printStackTrace();
        }

        if (totalAll > 400){
            //setScheduleDialog(day,name);
            nameL[loopS] = name;
            dayL[loopS] = day;
            loopS++;
        } else {
            //nothingDialog(day,name);
        }
    }

    private void getNightData(final String name){
        int count = 0;
        int idA[] = new int[100];
        int totalAll = 0;
        String day = "Night";
        c = dbA.getSwitchDataNight(yestDate,name);
        try {
            if (c.moveToFirst()) {
                do {
                    int id = c.getInt(c.getColumnIndex("_id"));
                    String timeon = c.getString(c.getColumnIndex("timeOn"));
                    String timeoff = c.getString(c.getColumnIndex("timeOff"));

                    String total = calculateDiffTime(timeon, timeoff);

                    idA[count] = id;
                    totalAll += Integer.parseInt(total);
                    count++;
                } while (c.moveToNext());
            }
        }catch (SQLException e){
            e.printStackTrace();
        }

        if (totalAll > 400){
            //setScheduleDialog(day,name);
            nameL[loopS] = name;
            dayL[loopS] = day;
            loopS++;
        } else {
            //nothingDialog(day,name);
        }
    }

    private Dialog setScheduleDialog(String day, String name) {
        return new AlertDialog.Builder(Schedule.this)
                .setTitle("I got suggestion for you ! \nOn yesterday "+day+"...")
                .setMessage("I found you used switch "+name+" for too long. If you forget to switch off "+name+", I suggest you set a schedule to switch off "+name+". Do you want to set a schedule for switch "+name+"?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent intent = new Intent(Schedule.this, AddScheduleActivityFragment.class);
                        startActivity(intent);
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }

    private Dialog nothingDialog() {
        return new AlertDialog.Builder(Schedule.this)
                .setTitle("Opsss sorry...")
                .setMessage("No suggestion for today. See you tomorrow ! :)")
                .setCancelable(false)
                .setNegativeButton("Ok!", null)
                .show();
    }

}

