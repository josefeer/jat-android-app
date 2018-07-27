package com.prittysoft.jat;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class DetailSonda extends AppCompatActivity {

    private TextView activity_detail_sonda_title, activity_detail_sonda_tpromedio,
            activity_detail_sonda_tmax, activity_detail_sonda_tmin;

    private DatabaseHelper mDatabaseHelper;
    private String SondaNumber, id_main, sqltable;
    private ListView mListView;
    private Double tpromedio = 0.00, tmax, tmin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_sonda);

        activity_detail_sonda_title = findViewById(R.id.toolbar_detail_sonda_title);
        mListView = findViewById(R.id.activity_detail_sonda_listview);
        activity_detail_sonda_tpromedio = findViewById(R.id.activity_detail_sonda_tpromedio);
        activity_detail_sonda_tmax = findViewById(R.id.activity_detail_sonda_tmax);
        activity_detail_sonda_tmin = findViewById(R.id.activity_detail_sonda_tmin);


        mDatabaseHelper = new DatabaseHelper(getApplicationContext());

        Intent intent = getIntent();
        SondaNumber = intent.getStringExtra("NUMBER");
        id_main = intent.getStringExtra("ID");
        sqltable = intent.getStringExtra("SQLTABLE");

        setupToolbar(SondaNumber);

        populateSondaListView();

    }

    private void setupToolbar(String n){
        Toolbar mytoolbar = findViewById(R.id.toolbar_detail_sonda);
        setSupportActionBar(mytoolbar);

        if (getSupportActionBar() != null){

            getSupportActionBar().setDisplayShowTitleEnabled(false);

            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        String newtitle = "Mediciones de la Sonda " + n;
        activity_detail_sonda_title.setText(newtitle);

    }

    private void populateSondaListView(){

        Integer i = 0, promdiv = 0;
        SondaMeasurements values;
        final List<SondaMeasurements> SondaList = new ArrayList<>();
        Cursor data = mDatabaseHelper.getSondaValues(id_main, sqltable, SondaNumber);
        Double currentemperature;
        String textprom, textmin, textmax;

        while (data.moveToNext()){

            i++;
            promdiv++;

            values = new SondaMeasurements(Integer.toString(i), data.getString(0),
                    data.getString(1));

            if (!values.getTemperature().equals("N/A")){

                currentemperature = Double.parseDouble(values.getTemperature());

                tpromedio = tpromedio + currentemperature;

                if (i == 1){
                    tmax = currentemperature;
                    tmin = currentemperature;
                }
                else {

                    if (currentemperature > tmax){
                        tmax = currentemperature;
                    }
                    else if (currentemperature < tmin){
                        tmin = currentemperature;
                    }

                }

            }
            else {
                promdiv--;
            }

            SondaList.add(values);

        }

        data.close();

        final SondaMeasurementsListAdapter adapter = new SondaMeasurementsListAdapter(
                getApplicationContext(),
                R.layout.sonda_adapter_listview,
                SondaList);

        mListView.setAdapter(adapter);

        if (tpromedio != 0){

            tpromedio = tpromedio / promdiv;
            textprom = Double.toString(tpromedio);
            activity_detail_sonda_tpromedio.setText(textprom);


        }

        if (tmax != null){

            textmax = Double.toString(tmax);
            activity_detail_sonda_tmax.setText(textmax);

        }

        if (tmin != null){

            textmin = Double.toString(tmin);
            activity_detail_sonda_tmin.setText(textmin);

        }

    }

}
