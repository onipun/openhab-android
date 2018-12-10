package org.openhab.habdroid;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import androidx.fragment.app.Fragment;


public class ScheduleFragment extends Fragment{
    private static final String TAG = ScheduleFragment.class.getSimpleName();
    private LoginDbInitiate dbA;
    private Fragment fragment;
    private Switch led1, led2, fan1;
    private Button btnSchedule;
    private Cursor c;


/*
    this class only used for initial integration
 */
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        fragment= new DisplayScheduleActivityFragment();
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View v = inflater.inflate(R.layout.fragment_rule, container,false);

        dbA = dbA.getsLoginDbInitiate(getContext());

        led1 = (Switch) v.findViewById(R.id.switch1);
        led2 = (Switch) v.findViewById(R.id.switch2);
        fan1 = (Switch) v.findViewById(R.id.switch3);
        btnSchedule = (Button) v.findViewById(R.id.btnSchedule);


        btnSchedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, fragment)
                        .addToBackStack(null).commit();
            }
        });

        led1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    dbA.updateSwitchData(1,"ON");
                }else{
                    dbA.updateSwitchData(1,"OFF");
                }
            }
        });

        led2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    dbA.updateSwitchData(2,"ON");
                }else{
                    dbA.updateSwitchData(2,"OFF");
                }
            }
        });

        fan1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    dbA.updateSwitchData(3,"ON");
                }else{
                    dbA.updateSwitchData(3,"OFF");
                }
            }
        });

        return v;
    }

//    private void loadLED1Data() {
//        String status = "";
//        c = dbA.getAllSwitchData(1);
//        if (c.moveToFirst()) {
//            do {
//                led1.setText(c.getString(1));
//                status = c.getString(2);
//            } while (c.moveToNext());
//        }
//        if (status.equals("ON")){
//            led1.setChecked(true);
//
//        }else{
//            led1.setChecked(false);
//        }
//    }

//    private void loadLED2Data() {
//        String status = "";
//        c = dbA.getAllSwitchData(2);
//        if (c.moveToFirst()) {
//            do {
//                led2.setText(c.getString(1));
//                status = c.getString(2);
//            } while (c.moveToNext());
//        }
//        if (status.equals("ON")){
//            led2.setChecked(true);
//
//        }else{
//            led2.setChecked(false);
//        }
//    }

//    private void loadFAN1Data() {
//        String status = "";
//        c = dbA.getAllSwitchData(3);
//        if (c.moveToFirst()) {
//            do {
//                fan1.setText(c.getString(1));
//                status = c.getString(2);
//            } while (c.moveToNext());
//        }
//        if (status.equals("ON")){
//            fan1.setChecked(true);
//
//        }else{
//            fan1.setChecked(false);
//        }
//    }

}
