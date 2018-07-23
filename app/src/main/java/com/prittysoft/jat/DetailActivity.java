package com.prittysoft.jat;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class DetailActivity extends AppCompatActivity {


    private static final String TAG = "DetailActivity";

    private TextView activity_detail_date, activity_detail_starttime, activity_detail_endtime,
    activitiy_detail_title, activity_detail_status, activity_detail_tipo, activity_detail_tensayo,
    activity_detail_testabilizacion, activity_detail_tcaptura, activity_detail_client,
    activity_detail_equipment, activity_detail_equipment_model, activity_detail_equipment_serial;

    private ListView datalist1;

    DatabaseHelper LocalSQlite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        /*
        detail_txt_id = findViewById(R.id.detail_txt_id);
        detail_txt_equipment = findViewById(R.id.detail_txt_equipment);
        detail_txt_client = findViewById(R.id.detail_txt_client);
        detail_txt_serial = findViewById(R.id.detail_txt_serial);
        detail_txt_range = findViewById(R.id.detail_txt_range);
        detail_txt_period = findViewById(R.id.detail_txt_period);
        detail_txt_type = findViewById(R.id.detail_txt_type);
        datalist1 = findViewById(R.id.data_list1);*/

        activitiy_detail_title = findViewById(R.id.activity_detail_title);
        activity_detail_status = findViewById(R.id.activity_detail_status);
        activity_detail_date = findViewById(R.id.activity_detail_date);
        activity_detail_starttime = findViewById(R.id.activity_detail_starttime);
        activity_detail_endtime = findViewById(R.id.activity_detail_endtime);
        activity_detail_tipo = findViewById(R.id.activity_detail_tipo);
        activity_detail_tensayo = findViewById(R.id.activity_detail_tensayo);
        activity_detail_testabilizacion = findViewById(R.id.activity_detail_testabilizacion);
        activity_detail_tcaptura = findViewById(R.id.activity_detail_tcaptura);
        activity_detail_client = findViewById(R.id.activity_detail_client);
        activity_detail_equipment = findViewById(R.id.activity_detail_equipment);
        activity_detail_equipment_model = findViewById(R.id.activity_detail_equipment_model);
        activity_detail_equipment_serial = findViewById(R.id.activity_detail_equipment_serial);

        setupToolbar();
        LocalSQlite = new DatabaseHelper(getApplicationContext());

        String id_main = getIntent().getStringExtra("id_main");
        updateBaseUI(id_main);
        /*
        populateListView1(id_main);*/

    }

    private void setupToolbar(){
        Toolbar mytoolbar = findViewById(R.id.toolbar_detail);
        setSupportActionBar(mytoolbar);
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    private void updateBaseUI(String id){
        ArrayList<String> valuesforUI;
        valuesforUI = LocalSQlite.getMainMeasurementDetails(id);
        String activity_title;

        if (!valuesforUI.isEmpty()){

            activity_title = "ENSAYO #" + valuesforUI.get(0);

            activitiy_detail_title.setText(activity_title);
            activity_detail_status.setText(valuesforUI.get(12));
            activity_detail_date.setText(valuesforUI.get(1));
            activity_detail_starttime.setText(valuesforUI.get(2));
            activity_detail_endtime.setText(valuesforUI.get(3));
            activity_detail_tipo.setText(valuesforUI.get(4));
            activity_detail_tensayo.setText(valuesforUI.get(5));
            activity_detail_testabilizacion.setText(valuesforUI.get(6));
            activity_detail_tcaptura.setText(valuesforUI.get(7));
            activity_detail_client.setText(valuesforUI.get(11));
            activity_detail_equipment.setText(valuesforUI.get(8));
            activity_detail_equipment_model.setText(valuesforUI.get(9));
            activity_detail_equipment_serial.setText(valuesforUI.get(10));

        }
        else {

            Log.d(TAG, "Query Empty");

        }

    }


    private void populateListView1(String id){
        Log.d(TAG, "displaying data in the listview");
        final List<SensorCalibration> listData = new ArrayList<>();
        Cursor data = LocalSQlite.getSensorCalibrationDataDetails(id);
        while (data.moveToNext()){
            SensorCalibration values = new SensorCalibration(data.getString(1),
                    data.getString(2), data.getString(3));
            listData.add(values);
        }

        final SensorCalibrationListAdapter adapter = new SensorCalibrationListAdapter(getApplicationContext(),
                R.layout.detail_adapter_listview, listData);
        datalist1.setAdapter(adapter);
    }
}
