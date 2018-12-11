package org.openhab.habdroid;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
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

import android.os.Bundle;

public class EditScheduleActivity extends AppCompatActivity {


    private LoginDbInitiate dbA;
    private Cursor c;
    private EditText titleRem;
    private Spinner categoryRem;
    private TextView lblDateRem, lblTimeRem;
    private Button updateRem, deleteRem;
    private ImageButton btnDateRem, btnTimeRem;
    private StringBuilder aTime;
    private RadioGroup radG;
    private RadioButton radB, radB1, radB2;
    private String status;

    private int rowId, year, month, day, hour, minute;
    static final int DATE_DIALOG_ID = 999;
    static final int TIME_DIALOG_ID = 1111;
    private static final String TAG = EditScheduleActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_schedule);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar1);
        setSupportActionBar(toolbar);

        titleRem = (EditText) findViewById(R.id.titleRem);
        categoryRem = (Spinner) findViewById(R.id.categoryRem);
        updateRem = (Button) findViewById(R.id.updateRem);
        deleteRem = (Button) findViewById(R.id.deleteRem);
        btnDateRem = (ImageButton) findViewById(R.id.btnDateRem);
        btnTimeRem = (ImageButton) findViewById(R.id.btnTimeRem);
        lblDateRem = (TextView) findViewById(R.id.lblDateRem);
        lblTimeRem = (TextView) findViewById(R.id.lblTimeRem);
        radG = (RadioGroup) findViewById(R.id.rdoGroup1);
        radB1 = (RadioButton) findViewById(R.id.rdoON1);
        radB2 = (RadioButton) findViewById(R.id.rdoOFF1);



        Bundle b = getIntent().getExtras();
        rowId = b.getInt("keyid");
        dbA = dbA.getsLoginDbInitiate(getApplicationContext());
        loadSpinnerData();
        loadScheduleData(rowId);

        updateRem.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                confirmationUpdate();
            }
        });

        deleteRem.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                confirmationDelete();
            }
        });

        btnDateRem.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(DATE_DIALOG_ID);
            }
        });

        btnTimeRem.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(TIME_DIALOG_ID);
            }
        });


        getCurrentDateTime();
    }


    private void loadScheduleData(int rowId) {
        Log.d(TAG, "loadScheduleData: entered");
        c = dbA.getAllReminderData(rowId);
        if (c.moveToFirst()) {
            do {
                categoryRem.setSelection(((ArrayAdapter<String>)categoryRem.getAdapter()).getPosition(c.getString(1)));
                lblDateRem.setText(c.getString(2));
                lblTimeRem.setText(c.getString(3));
                titleRem.setText(c.getString(4));
                status = c.getString(5);

            } while (c.moveToNext());
        }

        if (status.equals("ON")){
            radB1.setChecked(true);

        }else{
            radB2.setChecked(true);
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem mi)
    {
        switch (mi.getItemId())
        {
            case android.R.id.home:
                onBackPressed();
                break;

            default:
                return super.onOptionsItemSelected(mi);
        }
        return super.onOptionsItemSelected(mi);
    }


    @SuppressLint("SimpleDateFormat")
    public void getCurrentDateTime() {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            SimpleDateFormat stf = new SimpleDateFormat("KK:mm");
            Date d = sdf.parse(lblDateRem.getText().toString());
            Date t = stf.parse(lblTimeRem.getText().toString());

            year = d.getYear();
            month = d.getMonth();
            day = d.getDate();
            hour = t.getHours();
            minute = t.getMinutes();

            updateTime(hour,minute);

            aTime = new StringBuilder().append(hour).append(':')
                    .append(minute);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }


    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DATE_DIALOG_ID:
                return new DatePickerDialog(this, datePickerListener, (year + 1900), month, day);

            case TIME_DIALOG_ID:
                // set time picker as current time
                return new TimePickerDialog(this, timePickerListener, hour, minute,
                        false);
        }
        return null;
    }


    private DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker v, int selectedYear, int selectedMonth,
                              int selectedDay) {
            year = selectedYear;
            month = selectedMonth;
            day = selectedDay;

            lblDateRem.setText(new StringBuilder().append(day).append("/")
                    .append(month + 1).append("/").append(year));
        }
    };


    private TimePickerDialog.OnTimeSetListener timePickerListener = new TimePickerDialog.OnTimeSetListener() {


        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minutes) {
            hour   = hourOfDay;
            minute = minutes;

            updateTime(hour,minute);

            aTime = new StringBuilder().append(hour).append(':')
                    .append(minute);
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
        List<String> lables = ItemsMiddleware.getInstance(EditScheduleActivity.this).getItemName();


        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(EditScheduleActivity.this,
                android.R.layout.simple_spinner_item, lables);

        // Drop down layout style - list view with radio button
        dataAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        categoryRem.setAdapter(dataAdapter);
    }


    public Dialog confirmationUpdate() {
        return new AlertDialog.Builder(this)
                .setMessage("Are you sure want to update this transaction?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if ((titleRem.getText().toString().equals(""))) {
                            Message.message(EditScheduleActivity.this, "Please fill in the title and check the set switch !");
                        }else {
                            int selected_id = radG.getCheckedRadioButtonId();
                            radB = (RadioButton) findViewById(selected_id);
                            dbA.updateReminderData(rowId, categoryRem.getSelectedItem().toString(), lblDateRem.getText().toString(), aTime.toString(), titleRem.getText().toString(), radB.getText().toString());

                            String message = "Switch " + categoryRem.getSelectedItem().toString() + " has been turn " + radB.getText().toString();

                            Calendar c = Calendar.getInstance();
                            c.set(Calendar.MONTH, month);
                            c.set(Calendar.DAY_OF_MONTH, day);
                            c.set(Calendar.YEAR, (year + 1900));
                            c.set(Calendar.HOUR_OF_DAY, hour);
                            c.set(Calendar.MINUTE, minute);

                            Bundle b = new Bundle();
                            b.putInt("keyid", rowId);

                            Intent intent = new Intent(EditScheduleActivity.this,
                                    NotificationService.class);
                            intent.putExtra("msg", message);
                            intent.putExtra("item", categoryRem.getSelectedItem().toString());
                            intent.putExtras(b);

                            PendingIntent pendingIntent = PendingIntent.getService(EditScheduleActivity.this, rowId, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                            AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                            alarmManager.cancel(pendingIntent);
                            alarmManager.set(AlarmManager.RTC, c.getTimeInMillis(), pendingIntent);
                            finish();
                        }
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }


    public Dialog confirmationDelete() {
        return new AlertDialog.Builder(this)
                .setMessage("Are you sure want to delete this transaction?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent intent = new Intent(EditScheduleActivity.this,
                                NotificationService.class);
                        PendingIntent pendingIntent = PendingIntent.getService(EditScheduleActivity.this, rowId, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                        AlarmManager alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
                        alarmManager.cancel(pendingIntent);

                        dbA.deleteReminderData(rowId);
                        setResult(RESULT_OK);
                        finish();
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }
}
