package com.prittysoft.jat;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

public class DetailActivity extends AppCompatActivity {


    private static final String TAG = "DetailActivity";

    private TextView activity_detail_date, activity_detail_starttime, activity_detail_endtime,
    activitiy_detail_title, activity_detail_status, activity_detail_tipo, activity_detail_tensayo,
    activity_detail_testabilizacion, activity_detail_tcaptura, activity_detail_client,
    activity_detail_equipment, activity_detail_equipment_model, activity_detail_equipment_serial;

    private Button activity_detail_btn_s1, activity_detail_btn_s2, activity_detail_btn_s3,
            activity_detail_btn_s4, activity_detail_btn_s5, activity_detail_btn_s6,
            activity_detail_btn_s7, activity_detail_btn_s8, activity_detail_btn_s9;

    private String id_main;

    private DatabaseHelper LocalSQlite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        setAllViewsByID();
        setupToolbar();

        LocalSQlite = new DatabaseHelper(getApplicationContext());
        Intent intent = getIntent();
        id_main = intent.getStringExtra("id_main");

        updateBaseUI(id_main);

    }

    private void setAllViewsByID(){

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
        activity_detail_btn_s1 = findViewById(R.id.activity_detail_btn_s1);
        activity_detail_btn_s2 = findViewById(R.id.activity_detail_btn_s2);
        activity_detail_btn_s3 = findViewById(R.id.activity_detail_btn_s3);
        activity_detail_btn_s4 = findViewById(R.id.activity_detail_btn_s4);
        activity_detail_btn_s5 = findViewById(R.id.activity_detail_btn_s5);
        activity_detail_btn_s6 = findViewById(R.id.activity_detail_btn_s6);
        activity_detail_btn_s7 = findViewById(R.id.activity_detail_btn_s7);
        activity_detail_btn_s8 = findViewById(R.id.activity_detail_btn_s8);
        activity_detail_btn_s9 = findViewById(R.id.activity_detail_btn_s9);

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
        final String activity_title, sqltable;

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

            switch (valuesforUI.get(4)){
                case "Isotermo 1":
                    sqltable = "isotermo1";

                    break;

                case "Isotermo 2":
                    sqltable = "isotermo2";

                    activity_detail_btn_s5.setVisibility(View.GONE);
                    activity_detail_btn_s6.setVisibility(View.GONE);
                    activity_detail_btn_s7.setVisibility(View.GONE);
                    activity_detail_btn_s8.setVisibility(View.GONE);
                    activity_detail_btn_s9.setVisibility(View.GONE);
                    break;

                case "Calibración con Patrón":
                    sqltable = "calibracionpatron";

                    activity_detail_btn_s2.setVisibility(View.GONE);
                    activity_detail_btn_s3.setVisibility(View.GONE);
                    activity_detail_btn_s4.setVisibility(View.GONE);
                    activity_detail_btn_s5.setVisibility(View.GONE);
                    activity_detail_btn_s6.setVisibility(View.GONE);
                    activity_detail_btn_s7.setVisibility(View.GONE);
                    activity_detail_btn_s8.setVisibility(View.GONE);
                    activity_detail_btn_s9.setVisibility(View.GONE);

                    activity_detail_btn_s1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            Intent DetailSondaActivity = new Intent(getApplicationContext(),
                                    DetailSonda.class);
                            DetailSondaActivity.putExtra("NUMBER", "1");
                            DetailSondaActivity.putExtra("ID", id_main);
                            DetailSondaActivity.putExtra("SQLTABLE", sqltable);

                            startActivity(DetailSondaActivity);
                        }
                    });

                    break;

            }

        }
        else {

            Log.d(TAG, "Query Empty");

        }

    }

}
