package org.openhab.habdroid;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
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

import androidx.fragment.app.Fragment;

import static android.content.Context.ALARM_SERVICE;

public class AddScheduleActivityFragment extends Fragment {
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
    private static final String TAG = AddScheduleActivityFragment.class.getSimpleName();

    private int year, month, day, hour, minute;
    private long id;
    static final int DATE_DIALOG_ID = 999;
    static final int TIME_DIALOG_ID = 1111;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.activity_add_schedule, container, false);

        dbA = dbA.getsLoginDbInitiate(getActivity());
        titleRem = v.findViewById(R.id.titleRem);
        categoryRem = v.findViewById(R.id.categoryRem);
        saveRem = v.findViewById(R.id.saveRem);
        resetRem = v.findViewById(R.id.resetRem);
        btnTimeRem = v.findViewById(R.id.btnTimeRem);
        btnDateRem = v.findViewById(R.id.btnDateRem);
        lblTimeRem = v.findViewById(R.id.lblTimeRem);
        lblDateRem = v.findViewById(R.id.lblDateRem);
        linRem = v.findViewById(R.id.linRem);
        lisRem = v.findViewById(R.id.lisRem);
        radG = v.findViewById(R.id.rdoGroup);
        mon = v.findViewById(R.id.chkMon);
        tue = v.findViewById(R.id.chkTue);
        wed = v.findViewById(R.id.chkWed);
        thu = v.findViewById(R.id.chkThu);
        fri = v.findViewById(R.id.chkFri);
        sat = v.findViewById(R.id.chkSat);
        sun = v.findViewById(R.id.chkSun);

        setCurrentDateTime();
        loadSpinnerData();

        saveRem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if ((titleRem.getText().toString().equals(""))) {
                    Message.message(v.getContext(), "Please fill in the blank!");
                } else {
                    String cat = categoryRem.getSelectedItem().toString();
                    String dat = lblDateRem.getText().toString();
                    String tim = aTime.toString();
                    String tit = titleRem.getText().toString();
                    int selected_id = radG.getCheckedRadioButtonId();
                    radB = (RadioButton) v.findViewById(selected_id);
                    String rad = radB.getText().toString();


                    id = dbA.insertReminderData(cat, dat, tim, tit, rad);
                    Log.d(TAG, "btnSave :" + id);
                    if (id > 0) {
                        Message.message(v.getContext(), "Saved");
                    } else {
                        Message.message(v.getContext(), "Not Saved");
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

                    Intent intent = new Intent(getActivity(),
                            NotificationService.class);
                    intent.putExtra("msg", message);
                    intent.putExtra("item",cat);
                    intent.putExtras(b);

                    PendingIntent pendingIntent = PendingIntent.getService(getActivity(), keyid, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                    AlarmManager alarmManager = (AlarmManager) getActivity().getSystemService(ALARM_SERVICE);
                    alarmManager.set(AlarmManager.RTC, c.getTimeInMillis(), pendingIntent);
                    getActivity().finish();
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


        return v;
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
                return new DatePickerDialog(getContext(), datePickerListener, year, month, day);


            case TIME_DIALOG_ID:
                // set time picker as current time
                return new TimePickerDialog(getContext(), timePickerListener, hour, minute,
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
        List<String> lables = ItemsMiddleware.getInstance(getActivity().getApplicationContext()).getItemName();


        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(v.getContext(),
                android.R.layout.simple_spinner_item, lables);

        // Drop down layout style - list view with radio button
        dataAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        categoryRem.setAdapter(dataAdapter);
    }
}
