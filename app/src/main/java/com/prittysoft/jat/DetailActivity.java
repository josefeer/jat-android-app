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
    TextView detail_txt_id, detail_txt_start, detail_txt_date, detail_txt_end, detail_txt_equipment,
    detail_txt_client, detail_txt_serial, detail_txt_range, detail_txt_period, detail_txt_type;
    ListView datalist1;

    DatabaseHelper LocalSQlite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        detail_txt_id = findViewById(R.id.detail_txt_id);
        detail_txt_start = findViewById(R.id.detail_txt_starttime);
        detail_txt_date = findViewById(R.id.detail_txt_date);
        detail_txt_end = findViewById(R.id.detail_txt_endtime);
        detail_txt_equipment = findViewById(R.id.detail_txt_equipment);
        detail_txt_client = findViewById(R.id.detail_txt_client);
        detail_txt_serial = findViewById(R.id.detail_txt_serial);
        detail_txt_range = findViewById(R.id.detail_txt_range);
        detail_txt_period = findViewById(R.id.detail_txt_period);
        detail_txt_type = findViewById(R.id.detail_txt_type);
        datalist1 = findViewById(R.id.data_list1);

        setupToolbar();
        LocalSQlite = new DatabaseHelper(getApplicationContext());

        String main_id = getIntent().getStringExtra("main_id");
        updateUI(main_id);
        populateListView1(main_id);

    }

    private void updateUI(String id){
        ArrayList<String> valuesforUI;
        valuesforUI = LocalSQlite.getMainMeasurementDetails(id);
        if (valuesforUI.isEmpty()){
            Log.d(TAG, "Query Empty");
        }
        else {
            //up to 9 values in valuesforUI
            detail_txt_id.setText(valuesforUI.get(0));
            detail_txt_type.setText(valuesforUI.get(1));
            detail_txt_date.setText(valuesforUI.get(2));
            detail_txt_start.setText(valuesforUI.get(3));
            detail_txt_end.setText(valuesforUI.get(4));
            detail_txt_equipment.setText(valuesforUI.get(5));
            detail_txt_serial.setText(valuesforUI.get(6));
            detail_txt_client.setText(valuesforUI.get(7));
            detail_txt_range.setText(valuesforUI.get(8));
            detail_txt_period.setText(valuesforUI.get(9));
        }

    }

    private void setupToolbar(){
        Toolbar mytoolbar = findViewById(R.id.toolbar_detail);
        setSupportActionBar(mytoolbar);
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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
