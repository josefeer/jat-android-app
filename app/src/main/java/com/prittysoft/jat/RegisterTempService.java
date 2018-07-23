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

    private static boolean ServiceStatus;
    private static final String TAG = "RegisterTempService";
    private static final Integer minute = 1000 * 60;
    private static Intent localintent = new Intent("RegisterTemp");
    SimpleDateFormat hour_format = new SimpleDateFormat("k:mm:ss", Locale.US);

    // SimpleDateFormat timezone = new SimpleDateFormat("z", Locale.US);

    // Class helper
    DatabaseHelper mDatabaseHelper;

    // Variables
    String fecha, hora, tipo_ensayo, tiempo_ensayo, tiempo_estabilizacion, tiempo_captura,
            equipo_nombre, equipo_modelo, equipo_serial, equipo_cliente;

    String tipo_ensayo_posicion;

    public RegisterTempService() {
        super("RegisterTempService");
    }

    @Override
    public void onDestroy() {
        ServiceStatus = false;
        Log.d(TAG, "Serviced Destroyed");
        super.onDestroy();
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

        ServiceStatus = true;
        Log.d(TAG, "Service Started!");

        if (intent != null) {

            getValuesFromAddfragment(intent);
            Data2DB();

        }

        Log.d(TAG, "Service Finished");

    }

    private void getValuesFromAddfragment(Intent intent) {

        fecha = intent.getStringExtra("fecha");
        hora = intent.getStringExtra("hora");
        tipo_ensayo = intent.getStringExtra("tipo_ensayo");
        tiempo_ensayo = intent.getStringExtra("tiempo_ensayo");
        tiempo_estabilizacion = intent.getStringExtra("tiempo_estabilizacion");
        tiempo_captura = intent.getStringExtra("tiempo_captura");
        equipo_nombre = intent.getStringExtra("equipo_nombre");
        equipo_modelo = intent.getStringExtra("equipo_modelo");
        equipo_serial = intent.getStringExtra("equipo_serial");
        equipo_cliente = intent.getStringExtra("equipo_cliente");
        tipo_ensayo_posicion = intent.getStringExtra("tipo_ensayo_posicion");

    }

    private void Data2DB() {

        mDatabaseHelper = new DatabaseHelper(getApplicationContext());

        Integer t_estabilizacion = Integer.parseInt(tiempo_estabilizacion);
        Integer t_ensayo_posicion = Integer.parseInt(tipo_ensayo_posicion);
        Integer current_id;

        //Database Insertion for measurements table
        String[] Table1Values = {
                fecha,
                hora,
                null,
                tipo_ensayo,
                tiempo_ensayo,
                tiempo_estabilizacion,
                tiempo_captura,
                equipo_nombre,
                equipo_modelo,
                equipo_serial,
                equipo_cliente,
                "INCOMPLETO"
        };


        if (mDatabaseHelper.addDataTable1(Table1Values)) {

            Log.d(TAG, "Succesfull Table 1 INSERT");

            // Pausamos el proceso por el tiempo de estabilizacion si es diferente de 0
            if (!tiempo_estabilizacion.equals("0")) {
                Log.d(TAG, "Tiempo de estabilizacion es: " + tiempo_estabilizacion);
                SystemClock.sleep(t_estabilizacion);
            }

            // Verificamos que no se ha cancelado el servicio
            if (!ServiceStatus) {
                stopSelf();
                Log.d(TAG, "Service Interrupted");
                return;
            }

            current_id = mDatabaseHelper.getAutoIncrementMeasurements();

            try {

                switch (t_ensayo_posicion) {
                    case 0:
                        InsertIsotermos1(current_id);
                        break;
                    case 1:
                        InsertIsotermos2(current_id);
                        break;
                    case 2:
                        InsertCalPatron(current_id);
                        break;
                }

            } catch (JSONException e) {

                Log.d(TAG, "JSONException" + e.toString());

            }

            if (ServiceStatus) {

                // Actualizamos el valor endtime que indica cuando se finalizo el proceso
                mDatabaseHelper.updateTable1Complete(current_id, hour_format.format(new Date()));

            } else {

                // Actualizamos el valor endtime que indica cuando se cancelo el proceso
                mDatabaseHelper.updateTable1Canceled(current_id, hour_format.format(new Date()));

            }

        } else {

            Log.d(TAG, "Unsuccesfull Table1 INSERT");

        }


    }

    private void InsertIsotermos1(Integer queryID) throws JSONException {

        String current_timestamp;

        Integer t_ensayo = Integer.parseInt(tiempo_ensayo);
        Integer t_captura = Integer.parseInt(tiempo_captura);
        Integer progressbar_percentage, current_time;

        JSONObject JSONValues;

        String S1, S2, S3, S4, S5, S6, S7, S8, S9;

        for (current_time = 1; current_time <= t_ensayo; current_time += t_captura) {

            SystemClock.sleep(minute * t_captura);

            // Verificamos que no se ha cancelado el servicio
            if (!ServiceStatus) {
                stopSelf();
                Log.d(TAG, "Service Interrupted");
                return;
            }

            current_timestamp = hour_format.format(new Date());

            JSONValues = BTsocketHandler.getBTdata();
            S1 = JSONValues.getString("S1");
            S2 = JSONValues.getString("S2");
            S3 = JSONValues.getString("S3");
            S4 = JSONValues.getString("S4");
            S5 = JSONValues.getString("S5");
            S6 = JSONValues.getString("S6");
            S7 = JSONValues.getString("S7");
            S8 = JSONValues.getString("S8");
            S9 = JSONValues.getString("S9");

            String[] table3_values = {
                    queryID.toString(),
                    current_timestamp,
                    S1,
                    S2,
                    S3,
                    S4,
                    S5,
                    S6,
                    S7,
                    S8,
                    S9
            };

            mDatabaseHelper.addDataTable3(table3_values);

            // Verificamos que no se ha cancelado el servicio
            if (!ServiceStatus) {

                stopSelf();
                Log.d(TAG, "Service Interrupted");
                return;

            } else {

                progressbar_percentage = (current_time * 100) / t_ensayo;

                // Enviamos información del proceso al progressbar del Addfragment
                localintent.putExtra("progressbar_percentage", progressbar_percentage.toString());
                LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(localintent);

            }

        }


    }

    private void InsertIsotermos2(Integer queryID) throws JSONException {

        String current_timestamp;

        Integer t_ensayo = Integer.parseInt(tiempo_ensayo);
        Integer t_captura = Integer.parseInt(tiempo_captura);
        Integer progressbar_percentage, current_time;

        JSONObject JSONValues;

        String S1, S2, S3, S4;

        for (current_time = 1; current_time <= t_ensayo; current_time += t_captura) {

            SystemClock.sleep(minute * t_captura);

            // Verificamos que no se ha cancelado el servicio
            if (!ServiceStatus) {
                stopSelf();
                Log.d(TAG, "Service Interrupted");
                return;
            }

            current_timestamp = hour_format.format(new Date());

            JSONValues = BTsocketHandler.getBTdata();
            S1 = JSONValues.getString("S1");
            S2 = JSONValues.getString("S2");
            S3 = JSONValues.getString("S3");
            S4 = JSONValues.getString("S4");

            String[] table2_values = {
                    queryID.toString(),
                    current_timestamp,
                    S1,
                    S2,
                    S3,
                    S4
            };

            mDatabaseHelper.addDataTable2(table2_values);

            // Verificamos que no se ha cancelado el servicio
            if (!ServiceStatus) {

                stopSelf();
                Log.d(TAG, "Service Interrupted");
                return;

            } else {

                progressbar_percentage = (current_time * 100) / t_ensayo;

                // Enviamos información del proceso al progressbar del Addfragment
                localintent.putExtra("progressbar_percentage", progressbar_percentage.toString());
                LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(localintent);

            }

        }


    }

    private void InsertCalPatron(Integer queryID) throws JSONException {

        String current_timestamp;

        Integer t_ensayo = Integer.parseInt(tiempo_ensayo);
        Integer t_captura = Integer.parseInt(tiempo_captura);
        Integer progressbar_percentage, current_time;

        JSONObject JSONValues;

        String S1;

        for (current_time = 1; current_time <= t_ensayo; current_time += t_captura) {

            SystemClock.sleep(minute * t_captura);

            // Verificamos que no se ha cancelado el servicio
            if (!ServiceStatus) {
                stopSelf();
                Log.d(TAG, "Service Interrupted");
                return;
            }

            current_timestamp = hour_format.format(new Date());

            JSONValues = BTsocketHandler.getBTdata();
            S1 = JSONValues.getString("S1");

            String[] table4_values = {
                    queryID.toString(),
                    current_timestamp,
                    S1,
            };

            mDatabaseHelper.addDataTable4(table4_values);

            // Verificamos que no se ha cancelado el servicio
            if (!ServiceStatus) {

                stopSelf();
                Log.d(TAG, "Service Interrupted");
                return;

            } else {

                progressbar_percentage = (current_time * 100) / t_ensayo;

                // Enviamos información del proceso al progressbar del Addfragment
                localintent.putExtra("progressbar_percentage", progressbar_percentage.toString());
                LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(localintent);

            }

        }

    }

}
