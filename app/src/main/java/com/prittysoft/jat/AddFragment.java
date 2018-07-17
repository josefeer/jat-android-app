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
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
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

    //layouts
    GridLayout subcontainer1;

    // widgets
    EditText etxt_equipment, etxt_serial, etxt_client, etxt_model;
    Button btn_add;
    TextView s1_lbl, s1_value, s2_lbl, s2_value, s3_lbl, s3_value, s4_lbl, s4_value, s5_lbl,
    s5_value, s6_lbl, s6_value, s7_lbl, s7_value, s8_lbl, s8_value, s9_lbl, s9_value;
    ProgressBar progressBar;
    Spinner sp1, sp2, sp3, sp4;

    // Intent
    Intent RegisterTempIntent;

    // Receivers
    BroadcastReceiver mMessageReceiver = new mMessageReceiver();
    BroadcastReceiver RegisterTempReceiver = new RegisterTempReceiver();

    // DateFormat for timestamp
    SimpleDateFormat date_format = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
    SimpleDateFormat hour_format = new SimpleDateFormat("k:mm:ss", Locale.US);
    SimpleDateFormat timezone = new SimpleDateFormat("z", Locale.US);

    // ArrayAdapters
    ArrayAdapter<CharSequence> SpinnerAdapter1, SpinnerAdapter2, SpinnerAdapter3, SpinnerAdapter4;

    //Booleans
    boolean BTstatus;


    public AddFragment() {
        // Required empty public constructor
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        SpinnerAdapter1 = ArrayAdapter.createFromResource(context,
                R.array.valorestiposensayos, android.R.layout.simple_spinner_item);

        SpinnerAdapter2 = ArrayAdapter.createFromResource(context,
                R.array.valoresensayos, android.R.layout.simple_spinner_item);

        SpinnerAdapter3 = ArrayAdapter.createFromResource(context,
                R.array.valoresestabilizacion, android.R.layout.simple_spinner_item);

        SpinnerAdapter4 = ArrayAdapter.createFromResource(context,
                R.array.valoresdecaptura, android.R.layout.simple_spinner_item);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_add, container, false);

        setAllViewById(v);

        SpinnerAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        SpinnerAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        SpinnerAdapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        SpinnerAdapter4.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        sp1.setAdapter(SpinnerAdapter1);
        sp2.setAdapter(SpinnerAdapter2);
        sp3.setAdapter(SpinnerAdapter3);
        sp4.setAdapter(SpinnerAdapter4);

        etxt_equipment.addTextChangedListener(verify_data);
        etxt_model.addTextChangedListener(verify_data);
        etxt_serial.addTextChangedListener(verify_data);
        etxt_client.addTextChangedListener(verify_data);

        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_addAction();
            }
        });

        sp1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0:
                        isIsotermo1();
                        break;
                    case 1:
                        isIsotermo2();
                        break;
                    case 2:
                        isCalibracionPatron();
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

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

    private void btn_addAction() {
        IntentFilter filter2 = new IntentFilter("RegisterTemp");
        Date start_timestamp = new Date();

        String value_range = sp2.getSelectedItem().toString();
        String value_period = sp3.getSelectedItem().toString();
        String value_date = date_format.format(start_timestamp);
        String value_equipment = etxt_equipment.getText().toString();
        String value_serial = etxt_serial.getText().toString();
        String value_client = etxt_client.getText().toString();
        String value_datetime = hour_format.format(start_timestamp);
        String value_type = sp1.getSelectedItem().toString();

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

    private void setAllViewById(View v){
        btn_add = v.findViewById(R.id.btn_add);

        subcontainer1 = v.findViewById(R.id.subcontainer1);

        etxt_equipment = v.findViewById(R.id.etxt_equipment);
        etxt_model = v.findViewById(R.id.etxt_modelo);
        etxt_serial = v.findViewById(R.id.etxt_serial);
        etxt_client = v.findViewById(R.id.etxt_client);

        s1_lbl = v.findViewById(R.id.addfragment_lbl_s1);
        s1_value = v.findViewById(R.id.addfragment_lbl_s1_value);
        s2_lbl = v.findViewById(R.id.addfragment_lbl_s2);
        s2_value = v.findViewById(R.id.addfragment_lbl_s2_value);
        s3_lbl = v.findViewById(R.id.addfragment_lbl_s3);
        s3_value = v.findViewById(R.id.addfragment_lbl_s3_value);
        s4_lbl = v.findViewById(R.id.addfragment_lbl_s4);
        s4_value = v.findViewById(R.id.addfragment_lbl_s4_value);
        s5_lbl = v.findViewById(R.id.addfragment_lbl_s5);
        s5_value = v.findViewById(R.id.addfragment_lbl_s5_value);
        s6_lbl = v.findViewById(R.id.addfragment_lbl_s6);
        s6_value = v.findViewById(R.id.addfragment_lbl_s6_value);
        s7_lbl = v.findViewById(R.id.addfragment_lbl_s7);
        s7_value = v.findViewById(R.id.addfragment_lbl_s7_value);
        s8_lbl = v.findViewById(R.id.addfragment_lbl_s8);
        s8_value = v.findViewById(R.id.addfragment_lbl_s8_value);
        s9_lbl = v.findViewById(R.id.addfragment_lbl_s9);
        s9_value = v.findViewById(R.id.addfragment_lbl_s9_value);

        progressBar = v.findViewById(R.id.progressBar);

        sp1 = v.findViewById(R.id.addfragment_sp1);
        sp2 = v.findViewById(R.id.addfragment_sp2);
        sp3 = v.findViewById(R.id.addfragment_sp3);
        sp4 = v.findViewById(R.id.addfragment_sp4);
    }

    private void isIsotermo1(){
        s2_lbl.setVisibility(View.VISIBLE);
        s2_value.setVisibility(View.VISIBLE);
        s3_lbl.setVisibility(View.VISIBLE);
        s3_value.setVisibility(View.VISIBLE);
        s4_lbl.setVisibility(View.VISIBLE);
        s4_value.setVisibility(View.VISIBLE);
        s5_lbl.setVisibility(View.VISIBLE);
        s5_value.setVisibility(View.VISIBLE);
        s6_lbl.setVisibility(View.VISIBLE);
        s6_value.setVisibility(View.VISIBLE);
        s7_lbl.setVisibility(View.VISIBLE);
        s7_value.setVisibility(View.VISIBLE);
        s8_lbl.setVisibility(View.VISIBLE);
        s8_value.setVisibility(View.VISIBLE);
        s9_lbl.setVisibility(View.VISIBLE);
        s9_value.setVisibility(View.VISIBLE);

    }

    private void isIsotermo2(){
        s2_lbl.setVisibility(View.VISIBLE);
        s2_value.setVisibility(View.VISIBLE);
        s3_lbl.setVisibility(View.VISIBLE);
        s3_value.setVisibility(View.VISIBLE);
        s4_lbl.setVisibility(View.VISIBLE);
        s4_value.setVisibility(View.VISIBLE);
        s5_lbl.setVisibility(View.GONE);
        s5_value.setVisibility(View.GONE);
        s6_lbl.setVisibility(View.GONE);
        s6_value.setVisibility(View.GONE);
        s7_lbl.setVisibility(View.GONE);
        s7_value.setVisibility(View.GONE);
        s8_lbl.setVisibility(View.GONE);
        s8_value.setVisibility(View.GONE);
        s9_lbl.setVisibility(View.GONE);
        s9_value.setVisibility(View.GONE);

    }

    private void isCalibracionPatron(){
        s2_lbl.setVisibility(View.GONE);
        s2_value.setVisibility(View.GONE);
        s3_lbl.setVisibility(View.GONE);
        s3_value.setVisibility(View.GONE);
        s4_lbl.setVisibility(View.GONE);
        s4_value.setVisibility(View.GONE);
        s5_lbl.setVisibility(View.GONE);
        s5_value.setVisibility(View.GONE);
        s6_lbl.setVisibility(View.GONE);
        s6_value.setVisibility(View.GONE);
        s7_lbl.setVisibility(View.GONE);
        s7_value.setVisibility(View.GONE);
        s8_lbl.setVisibility(View.GONE);
        s8_value.setVisibility(View.GONE);
        s9_lbl.setVisibility(View.GONE);
        s9_value.setVisibility(View.GONE);
    }

    private TextWatcher verify_data = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            String equipment = etxt_equipment.getText().toString().trim();
            String model = etxt_model.getText().toString().trim();
            String serial = etxt_serial.getText().toString().trim();
            String client = etxt_client.getText().toString().trim();

            btn_add.setEnabled(!equipment.isEmpty() && !model.isEmpty() && !serial.isEmpty() &&
            !client.isEmpty() && BTstatus);
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    //BrodcastReceiver from BTservice
    private class mMessageReceiver extends BroadcastReceiver {
        JSONObject BTdata;
        List<String> sensor_values = new ArrayList<>();

        @Override
        public void onReceive(Context context, Intent intent){
            BTstatus = BTsocketHandler.getBluetoothStatus();
            Log.d("ADDFRAGMENT", "Receiver online");
            /*
            BTdata = BTsocketHandler.getBTdata();
            try {
                sensor_values = getSensor_values(BTdata);
                s1_value.setText(sensor_values.get(0));
            }catch (JSONException e){
                Log.d("AddFragment", "JSONExpetion");
            }*/
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
