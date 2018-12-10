package org.openhab.habdroid;

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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import androidx.cardview.widget.CardView;
import androidx.cursoradapter.widget.SimpleCursorAdapter;
import androidx.fragment.app.Fragment;
import database.LoginDbSchema.LoginDbSchema;

import static database.LoginDbSchema.LoginDbSchema.Cols.ID_REMINDER;

public class DisplayScheduleActivityFragment extends Fragment {

    private Fragment fragment;
    private LoginDbInitiate dbA;
    private View v;
    private Cursor c;
    private ListView listRem;
    private Button add;
    private static final String TAG = DisplayScheduleActivityFragment.class.getSimpleName();


    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        fragment = new AddScheduleActivityFragment();
        dbA = dbA.getsLoginDbInitiate(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.activity_display_schedule, container, false);

        setDisplay();
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();

        setDisplay();
    }

    public void setDisplay(){

        listRem =  v.findViewById(R.id.listRem);



        String[] from = {LoginDbSchema.Cols.TITLE_REMINDER};
        int[] to = {R.id.txtEditCat};
        c = dbA.getReminderData();
        @SuppressWarnings("deprecation")
        SimpleCursorAdapter sca = new SimpleCursorAdapter(getContext(),
                R.layout.row_reminder_category, c, from, to);



        listRem.setAdapter(sca);
        listRem.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {

                Bundle b = new Bundle();
                c = (Cursor) arg0.getItemAtPosition(arg2);
                int keyid = c.getInt(c
                        .getColumnIndex(ID_REMINDER));
                b.putInt("keyid", keyid);
                Log.d(TAG, "onItemClick: DisplayScheduleActivityFragment " + keyid );

                try {
                    Intent i = new Intent(getActivity(),EditScheduleActivity.class);
                    i.putExtras(b);
                    getActivity().startActivityForResult(i,1);

                }catch (Exception e){
                    Log.d(TAG, "onItemClick: failed on opening Edit class");
                    e.printStackTrace();

                }

            }
        });

    }
}

