package com.prittysoft.jat;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class AddFragment extends Fragment {

    //widgets
    EditText etxt_equipment, etxt_serial, etxt_client;
    Button btn_add;
    TextView s1_value;
    ProgressBar progressBar;
    Spinner type_spinner, range_spinner, period_spinner;

    //Database Class
    DatabaseHelper mDatabaseHelper;

    //Intent
    Intent RegisterTempIntent;

    //Receivers
    BroadcastReceiver mMessageReceiver = new mMessageReceiver();
    BroadcastReceiver RegisterTempReceiver = new RegisterTempReceiver();

    //DateFormat for timestamp
    SimpleDateFormat date_format = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
    SimpleDateFormat hour_format = new SimpleDateFormat("k:mm:ss", Locale.US);
    SimpleDateFormat timezone = new SimpleDateFormat("z", Locale.US);




    public AddFragment() {
        // Required empty public constructor
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_add, container, false);

        btn_add = v.findViewById(R.id.btn_add);
        etxt_equipment = v.findViewById(R.id.etxt_equipment);
        etxt_serial = v.findViewById(R.id.etxt_serial);
        etxt_client = v.findViewById(R.id.etxt_client);
        s1_value = v.findViewById(R.id.txt_s1_value);
        progressBar = v.findViewById(R.id.progressBar);
        type_spinner = v.findViewById(R.id.sp_test_type);
        range_spinner = v.findViewById(R.id.spinner_tiempodelensayo);
        period_spinner = v.findViewById(R.id.spinner_tiempodecaptura);


        //ArrayAdapters for spinners
        ArrayAdapter<CharSequence> type_adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.test_types, android.R.layout.simple_spinner_item);

        ArrayAdapter<CharSequence> range_adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.range_values, android.R.layout.simple_spinner_item);

        ArrayAdapter<CharSequence> period_adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.period_values, android.R.layout.simple_spinner_item);

        type_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        range_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        period_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        type_spinner.setAdapter(type_adapter);
        range_spinner.setAdapter(range_adapter);
        period_spinner.setAdapter(period_adapter);

        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_addAction();
            }
        });

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        IntentFilter filter = new IntentFilter("work");
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(mMessageReceiver, filter);
    }

    @Override
    public void onPause() {
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(mMessageReceiver);
        super.onPause();
    }

    @Override
    public void onDestroy() {
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(mMessageReceiver);
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(RegisterTempReceiver);
        super.onDestroy();
    }

    private void btn_addAction(){
        IntentFilter filter2 = new IntentFilter("RegisterTemp");
        Date start_timestamp = new Date();

        String value_range = range_spinner.getSelectedItem().toString();
        String value_period = period_spinner.getSelectedItem().toString();
        String value_date = date_format.format(start_timestamp);
        String value_equipment = etxt_equipment.getText().toString();
        String value_serial = etxt_serial.getText().toString();
        String value_client = etxt_client.getText().toString();
        String value_datetime = hour_format.format(start_timestamp);
        String value_type = type_spinner.getSelectedItem().toString();

        RegisterTempIntent = new Intent(getContext(), RegisterTempService.class);
        RegisterTempIntent.putExtra("value_range", value_range);
        RegisterTempIntent.putExtra("value_period", value_period);
        RegisterTempIntent.putExtra("value_date", value_date);
        RegisterTempIntent.putExtra("value_equipment", value_equipment);
        RegisterTempIntent.putExtra("value_serial", value_serial);
        RegisterTempIntent.putExtra("value_client", value_client);
        RegisterTempIntent.putExtra("value_datetime", value_datetime);
        RegisterTempIntent.putExtra("value_type", value_type);

        btn_add.setEnabled(false);

        getContext().startService(RegisterTempIntent);
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(RegisterTempReceiver, filter2);
    }

    //BrodcastReceiver from BTservice
    private class mMessageReceiver extends BroadcastReceiver {
        JSONObject BTdata;
        List<String> sensor_values = new ArrayList<>();
        @Override
        public void onReceive(Context context, Intent intent){
            //test = intent.getStringExtra("s1");
            Log.d("ADDFRAGMENT", "Receiver online");
            BTdata = BTsocketHandler.getBTdata();
            try {
                sensor_values = getSensor_values(BTdata);
                s1_value.setText(sensor_values.get(0));
            }catch (JSONException e){
                Log.d("AddFragment", "JSONExpetion");
            }
        }

        private List<String> getSensor_values(JSONObject data) throws JSONException {
            List<String> values = new ArrayList<>();
            values.add(data.getString("S1"));
            return values;
        }
    }

    //BroadcastReceiver from RegisterTempService
    private class RegisterTempReceiver extends BroadcastReceiver {
        String receiver0;
        Integer value_progressbar;
        @Override
        public void onReceive(Context context, Intent intent) {
            receiver0 = intent.getStringExtra("value_progressbar");
            if (receiver0 != null){
                value_progressbar = Integer.parseInt(receiver0);
                progressBar.setProgress(Integer.parseInt(receiver0));

                if (receiver0.equals("100")){
                    btn_add.setEnabled(true);

                }

            }
        }
    }

}
