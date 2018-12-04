package org.openhab.habdroid.beta;

import android.os.Bundle;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import org.openhab.habdroid.AddScheduleActivityFragment;
import org.openhab.habdroid.DisplayScheduleActivityFragment;
import org.openhab.habdroid.ItemsMiddleware;
import org.openhab.habdroid.R;
import org.openhab.habdroid.ScheduleFragment;

public class Schedule extends AppCompatActivity {
    private static final String TAG = Schedule.class.getSimpleName();
    protected FragmentManager fragmentManager;
    protected Fragment fragment = null;
    protected Fragment addFragment = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        fragmentManager = getSupportFragmentManager();
        fragment = new DisplayScheduleActivityFragment();
        fragmentManager.beginTransaction().add(R.id.fragment_container, fragment).commit();


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                addFragment = new AddScheduleActivityFragment();
                fragmentManager.beginTransaction().replace(R.id.fragment_container,addFragment).addToBackStack(null).commit();

//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();

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





}

