package org.openhab.habdroid;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;


import java.util.Calendar;
import java.util.List;

public class AddRemeber extends AppCompatActivity {

    private LoginDbInitiate dbA;
    private EditText titleRem;
    private Spinner categoryRem;
    private TextView lblDateRem, lblTimeRem;
    private Button saveRem, resetRem;
    private ImageButton btnDateRem, btnTimeRem;
    private LinearLayout linRem;
    private ListView lisRem;
    private StringBuilder aTime;
    private RadioGroup radG;
    private RadioButton radB;
    private CheckBox mon, tue, wed, thu, fri, sat, sun;
    private View v;
    private final Calendar c = Calendar.getInstance();
    private static final String TAG = AddRemeber.class.getSimpleName();

    private int year, month, day, hour, minute;
    private long id;
    static final int DATE_DIALOG_ID = 999;
    static final int TIME_DIALOG_ID = 1111;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_remeber);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        dbA = dbA.getsLoginDbInitiate(getApplicationContext());
        titleRem = findViewById(R.id.titleRem);
        categoryRem = findViewById(R.id.categoryRem);
        saveRem = findViewById(R.id.saveRem);
        resetRem = findViewById(R.id.resetRem);
        btnTimeRem = findViewById(R.id.btnTimeRem);
        btnDateRem = findViewById(R.id.btnDateRem);
        lblTimeRem = findViewById(R.id.lblTimeRem);
        lblDateRem = findViewById(R.id.lblDateRem);
        linRem = findViewById(R.id.linRem);
        lisRem = findViewById(R.id.lisRem);
        radG = findViewById(R.id.rdoGroup);
        mon = findViewById(R.id.chkMon);
        tue = findViewById(R.id.chkTue);
        wed = findViewById(R.id.chkWed);
        thu = findViewById(R.id.chkThu);
        fri = findViewById(R.id.chkFri);
        sat = findViewById(R.id.chkSat);
        sun = findViewById(R.id.chkSun);

        setCurrentDateTime();
        loadSpinnerData();

        saveRem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if ((titleRem.getText().toString().equals("")) || (radG.getCheckedRadioButtonId() == -1)) {
                    Message.message(AddRemeber.this, "Please fill in the title and check the set switch !");
                } else if ((titleRem.getText().toString().equals("")) && (radG.getCheckedRadioButtonId() == -1)){
                    Message.message(AddRemeber.this, "Please fill in the title and check the set switch !");
                }else {
                    String cat = categoryRem.getSelectedItem().toString();
                    String dat = lblDateRem.getText().toString();
                    String tim = aTime.toString();
                    String tit = titleRem.getText().toString();
                    int selected_id = radG.getCheckedRadioButtonId();
                    radB = (RadioButton) findViewById(selected_id);
                    String rad = radB.getText().toString();


                    id = dbA.insertReminderData(cat, dat, tim, tit, rad);
                    Log.d(TAG, "btnSave :" + id);
                    if (id > 0) {
                        Message.message(AddRemeber.this, "Saved");
                    } else {
                        Message.message(AddRemeber.this, "Not Saved");
                    }

                    String message = "Switch " + cat + " has been turn " + rad;

                    Calendar c = Calendar.getInstance();
                    c.set(Calendar.MONTH, month);
                    c.set(Calendar.DAY_OF_MONTH, day);
                    c.set(Calendar.YEAR, year);
                    c.set(Calendar.HOUR_OF_DAY, hour);
                    c.set(Calendar.MINUTE, minute);

                    Bundle b = new Bundle();
                    String longString = Long.toString(id);
                    int keyid = Integer.parseInt(longString);
                    b.putInt("keyid", keyid);

                    Intent intent = new Intent(AddRemeber.this,
                            NotificationService.class);
                    intent.putExtra("msg", message);
                    intent.putExtra("item",cat);
                    intent.putExtras(b);

                    PendingIntent pendingIntent = PendingIntent.getService(AddRemeber.this, keyid, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                    AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
                    alarmManager.set(AlarmManager.RTC, c.getTimeInMillis(), pendingIntent);
                    finish();
                }
            }
        });

        resetRem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                titleRem.setText("");
                categoryRem.setSelection(0);
                setCurrentDateTime();
                radG.clearCheck();
                mon.setChecked(false);
                tue.setChecked(false);
                wed.setChecked(false);
                thu.setChecked(false);
                fri.setChecked(false);
                sat.setChecked(false);
                sun.setChecked(false);
            }
        });

        btnDateRem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onCreateDialog(DATE_DIALOG_ID).show();
            }
        });

        btnTimeRem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onCreateDialog(TIME_DIALOG_ID).show();
            }
        });


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem mi) {
        switch (mi.getItemId()) {
            case android.R.id.home:
//                onBackPressed();

                break;

            default:
                return super.onOptionsItemSelected(mi);
        }
        return super.onOptionsItemSelected(mi);
    }


    public void setCurrentDateTime() {
        Calendar c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);
        hour = c.get(Calendar.HOUR_OF_DAY);
        minute = c.get(Calendar.MINUTE);

        lblDateRem.setText(new StringBuilder().append(day).append("/")
                .append(month + 1).append("/").append(year));

        updateTime(hour, minute);

        aTime = new StringBuilder().append(hour).append(':')
                .append(minute).append(" ");
    }


    protected Dialog onCreateDialog(int id) {

        switch (id) {
            case DATE_DIALOG_ID:
                return new DatePickerDialog(AddRemeber.this, datePickerListener, year, month, day);


            case TIME_DIALOG_ID:
                // set time picker as current time
                return new TimePickerDialog(AddRemeber.this, timePickerListener, hour, minute,
                        false);
        }
        Log.d(TAG, "onCreateDialog: failed to show Date and Time dialog");
        return null;
    }


    private DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker dp, int selectedYear, int selectedMonth,
                              int selectedDay) {
            year = selectedYear;
            month = selectedMonth;
            day = selectedDay;
            Log.d(TAG, "onDateSet: " + month);

            lblDateRem.setText(new StringBuilder().append(day).append("/")
                    .append(month + 1).append("/").append(year));
        }
    };


    public TimePickerDialog.OnTimeSetListener timePickerListener = new TimePickerDialog.OnTimeSetListener() {


        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minutes) {
            hour = hourOfDay;
            minute = minutes;

            updateTime(hour, minute);

            aTime = new StringBuilder().append(hour).append(':')
                    .append(minute).append(" ");
        }
    };


    // Used to convert 24hr format to 12hr format with AM/PM values
    private void updateTime(int hours, int mins) {

        String timeSet = "";
        if (hours > 12) {
            hours -= 12;
            timeSet = "PM";
        } else if (hours == 0) {
            hours += 12;
            timeSet = "AM";
        } else if (hours == 12)
            timeSet = "PM";
        else
            timeSet = "AM";


        String minutes = "";
        if (mins < 10)
            minutes = "0" + mins;
        else
            minutes = String.valueOf(mins);

        // Append in a StringBuilder
        String aTime = new StringBuilder().append(hours).append(':')
                .append(minutes).append(" ").append(timeSet).toString();

        lblTimeRem.setText(aTime);
    }


    private void loadSpinnerData() {
        // Spinner Drop down elements
        List<String> lables = ItemsMiddleware.getInstance(getApplicationContext()).getItemName();


        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getApplicationContext(),
                android.R.layout.simple_spinner_item, lables);

        // Drop down layout style - list view with radio button
        dataAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        categoryRem.setAdapter(dataAdapter);
    }

}
