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

    private static final String TAG = "Addfragment";

    //context
    Context context;

    //layouts
    GridLayout subcontainer1;

    // widgets
    EditText etxt_equipment, etxt_serial, etxt_client, etxt_model;
    Button btn_add, btn_cancel;
    TextView s1_lbl, s1_value, s2_lbl, s2_value, s3_lbl, s3_value, s4_lbl, s4_value, s5_lbl,
    s5_value, s6_lbl, s6_value, s7_lbl, s7_value, s8_lbl, s8_value, s9_lbl, s9_value;
    ProgressBar progressBar;
    Spinner sp1, sp2, sp3, sp4;

    // Intent
    Intent RegisterTempIntent;

    //IntentFilter
    IntentFilter filter2;

    // Receivers
    BroadcastReceiver BTserviceReceiver2 = new BTserviceReceiver2();
    BroadcastReceiver RegisterTempReceiver = new RegisterTempReceiver();

    // DateFormat for timestamp
    SimpleDateFormat date_format = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
    SimpleDateFormat hour_format = new SimpleDateFormat("k:mm:ss", Locale.US);
 //   SimpleDateFormat timezone = new SimpleDateFormat("z", Locale.US);

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

        this.context = context;

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

        RegisterTempIntent = new Intent(this.context, RegisterTempService.class);
        filter2 = new IntentFilter("RegisterTemp");

        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_addAction();
            }
        });

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_cancelAction();
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
        LocalBroadcastManager.getInstance(this.context).registerReceiver(BTserviceReceiver2, filter);
    }

    @Override
    public void onPause() {
        LocalBroadcastManager.getInstance(this.context).unregisterReceiver(BTserviceReceiver2);
        super.onPause();
    }

    @Override
    public void onDestroy() {
        LocalBroadcastManager.getInstance(this.context).unregisterReceiver(BTserviceReceiver2);
        LocalBroadcastManager.getInstance(this.context).unregisterReceiver(RegisterTempReceiver);
        super.onDestroy();
    }

    private void btn_addAction() {

        // Date() captura la fecha y hora del momento
        Date start_timestamp = new Date();

        // Valores del sistema
        String fecha = date_format.format(start_timestamp);
        String hora = hour_format.format(start_timestamp);

        // Valores de los spinners
        String tipo_ensayo = sp1.getSelectedItem().toString();
        String tiempo_ensayo = sp2.getSelectedItem().toString();
        String tiempo_estabilizacion = sp3.getSelectedItem().toString();
        String tiempo_captura = sp4.getSelectedItem().toString();
        Integer tipo_ensayo_posicion = sp1.getSelectedItemPosition();

        // Valores de los editext
        String equipo_nombre = etxt_equipment.getText().toString();
        String equipo_modelo = etxt_model.getText().toString();
        String equipo_serial = etxt_serial.getText().toString();
        String equipo_cliente = etxt_client.getText().toString();


        // Valores que son enviados al servicio RegisterTempService
        RegisterTempIntent.putExtra("fecha", fecha);
        RegisterTempIntent.putExtra("hora", hora);
        RegisterTempIntent.putExtra("tipo_ensayo", tipo_ensayo);
        RegisterTempIntent.putExtra("tiempo_ensayo", tiempo_ensayo);
        RegisterTempIntent.putExtra("tiempo_estabilizacion", tiempo_estabilizacion);
        RegisterTempIntent.putExtra("tiempo_captura", tiempo_captura);
        RegisterTempIntent.putExtra("equipo_nombre", equipo_nombre);
        RegisterTempIntent.putExtra("equipo_modelo", equipo_modelo);
        RegisterTempIntent.putExtra("equipo_serial", equipo_serial);
        RegisterTempIntent.putExtra("equipo_cliente", equipo_cliente);
        RegisterTempIntent.putExtra("tipo_ensayo_posicion", tipo_ensayo_posicion.toString());

        btn_add.setEnabled(false);
        btn_cancel.setEnabled(true);

        sp1.setEnabled(false);
        sp2.setEnabled(false);
        sp3.setEnabled(false);
        sp4.setEnabled(false);

        etxt_equipment.setEnabled(false);
        etxt_model.setEnabled(false);
        etxt_serial.setEnabled(false);
        etxt_client.setEnabled(false);

        // Iniciamos el servicio de la clase RegisterTempService
        this.context.startService(RegisterTempIntent);

        // Iniciamos un broadcastmanager para recibir data de RegisterTempService
        LocalBroadcastManager.getInstance(this.context).registerReceiver(RegisterTempReceiver, filter2);
    }

    private void btn_cancelAction(){
        this.context.stopService(RegisterTempIntent);
        LocalBroadcastManager.getInstance(this.context).unregisterReceiver(RegisterTempReceiver);


        sp1.setEnabled(true);
        sp2.setEnabled(true);
        sp3.setEnabled(true);
        sp4.setEnabled(true);

        etxt_equipment.setEnabled(true);
        etxt_model.setEnabled(true);
        etxt_serial.setEnabled(true);
        etxt_client.setEnabled(true);

        btn_cancel.setEnabled(false);
        btn_add.setEnabled(true);

        progressBar.setProgress(0);

    }

    private void setAllViewById(View v){
        btn_add = v.findViewById(R.id.btn_add);
        btn_cancel = v.findViewById(R.id.btn_cancelar);

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
    private class BTserviceReceiver2 extends BroadcastReceiver {
        JSONObject BTdata;
        List<String> sensor_values = new ArrayList<>();

        @Override
        public void onReceive(Context context, Intent intent){
            Log.d(TAG, "Listening to BTservice");
            try {
                BTstatus = BTsocketHandler.getBluetoothStatus();
                BTdata = BTsocketHandler.getBTdata();
                sensor_values = getSensor_values(BTdata);

                s1_value.setText(sensor_values.get(0));
                s2_value.setText(sensor_values.get(1));
                s3_value.setText(sensor_values.get(2));
                s4_value.setText(sensor_values.get(3));
                s5_value.setText(sensor_values.get(4));
                s6_value.setText(sensor_values.get(5));
                s7_value.setText(sensor_values.get(6));
                s8_value.setText(sensor_values.get(7));
                s9_value.setText(sensor_values.get(8));

            }catch (JSONException e){
                Log.d("AddFragment", "JSONExpetion");
            }
        }

        private List<String> getSensor_values(JSONObject data) throws JSONException {
            List<String> values = new ArrayList<>();

            values.add(data.getString("S1"));
            values.add(data.getString("S2"));
            values.add(data.getString("S3"));
            values.add(data.getString("S4"));
            values.add(data.getString("S5"));
            values.add(data.getString("S6"));
            values.add(data.getString("S7"));
            values.add(data.getString("S8"));
            values.add(data.getString("S9"));

            return values;
        }
    }

    //BroadcastReceiver from RegisterTempService
    private class RegisterTempReceiver extends BroadcastReceiver {
        String receiver;
        Integer progressbar_current_value;

        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d(TAG, "Listening to RegisterTempService");
            if (!(receiver = intent.getStringExtra("progressbar_percentage")).isEmpty()){
                progressbar_current_value = Integer.parseInt(receiver);
                progressBar.setProgress(progressbar_current_value);
                Log.d(TAG, "Progressbar value:"+progressbar_current_value.toString());

                if (progressbar_current_value >= 100){
                    btn_add.setEnabled(true);
                    btn_cancel.setEnabled(false);

                    etxt_equipment.getText().clear();
                    etxt_model.getText().clear();
                    etxt_serial.getText().clear();
                    etxt_client.getText().clear();

                    sp1.setEnabled(true);
                    sp2.setEnabled(true);
                    sp3.setEnabled(true);
                    sp4.setEnabled(true);

                    etxt_equipment.setEnabled(true);
                    etxt_model.setEnabled(true);
                    etxt_serial.setEnabled(true);
                    etxt_client.setEnabled(true);

                    progressBar.setProgress(0);

                }

            }
        }
    }

}
