package com.prittysoft.jat;

import android.app.IntentService;
import android.content.Intent;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class RegisterTempService extends IntentService {
    private static final String TAG = "RegisterTempService";
    private static Intent localintent = new Intent("RegisterTemp");
    private static final Integer minute = 1000 * 60;
    SimpleDateFormat hour_format = new SimpleDateFormat("k:mm:ss", Locale.US);
    SimpleDateFormat timezone = new SimpleDateFormat("z", Locale.US);

    //class helper
    DatabaseHelper mDatabaseHelper;

    public RegisterTempService() {
        super("RegisterTempService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        Log.d(TAG, "Service Started!");

        if (intent != null){
            try {
                Data2DB(intent);
            }catch (JSONException e){
                Log.d(TAG, "JSONException");
            }
        }

        Log.d(TAG, "Out of Service");
    }

    private void Data2DB(Intent intent) throws JSONException {
        mDatabaseHelper = new DatabaseHelper(getApplicationContext());

        String value_date = intent.getStringExtra("value_date");
        String value_datetime = intent.getStringExtra("value_datetime");
        String value_endtime = null;
        String value_equipment = intent.getStringExtra("value_equipment");
        String value_serial = intent.getStringExtra("value_serial");
        String value_client = intent.getStringExtra("value_client");
        Integer value_range = Integer.parseInt(intent.getStringExtra("value_range"));
        String value_range_units = null;
        Integer value_period = Integer.parseInt(intent.getStringExtra("value_period"));
        String value_period_units = null;
        String value_type = intent.getStringExtra("value_type");

        String S1, value_progressbar;
        JSONObject mainObject;
        Integer seq_value = 0;
        Date timestamp;

        //Database Insertion for measurements table
        String[] values_table1 = {value_type,value_date, value_datetime, null, value_equipment, value_serial,
        value_client, Integer.toString(value_range), value_range_units, Integer.toString(value_period),
        value_period_units};

        if (mDatabaseHelper.addDataTable1(values_table1)){
            Log.d(TAG, "Succesfull Primary insert");

            for (int current_time = 1; current_time <= value_range; current_time += value_period) {
                SystemClock.sleep(minute);
                //Database Insertion for sensordata table
                mainObject = BTsocketHandler.getBTdata();
                S1 = mainObject.getString("S1");

                Log.d(TAG, "value from arduino: " + S1);

                seq_value = mDatabaseHelper.getAutoIncrementMeasurements();

                timestamp = new Date();
                String date_time = hour_format.format(timestamp);

                String[] values_table2 = {Integer.toString(seq_value), date_time, S1};

                if (mDatabaseHelper.addDataTableSensorCalibration(values_table2)){
                    Log.d(TAG, "Succesfull Secondary Insert");
                }
                else {
                    Log.d(TAG, "Unsuccesfull secondary insert");
                }


            /*Progress Bar send back to main UI*/
                Integer progressbar_percentage = (current_time * 100) / value_range;
                value_progressbar = Integer.toString(progressbar_percentage);

                localintent.putExtra("value_progressbar", value_progressbar);
                LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(localintent);
                Log.d(TAG, "progressbar: " + value_progressbar);
            }

            if (seq_value != 0){
                timestamp = new Date();
                value_endtime = hour_format.format(timestamp);
                mDatabaseHelper.updateEndTimeTable1(seq_value, value_endtime);
                Log.d(TAG, "Succesfull Update in main table");
            }

        }
        else {
            Log.d(TAG, "Unsuccesfull primary insert");
        }


    }

}
