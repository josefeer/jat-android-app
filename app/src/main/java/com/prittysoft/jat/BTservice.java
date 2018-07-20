package com.prittysoft.jat;

import android.app.IntentService;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class BTservice extends IntentService {
    private static String TAG = "BTservice";
    private static Intent localintent = new Intent("work");

    public BTservice() {
        super("BTservice");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        Log.d(TAG, "Service Started!");

        BluetoothSocket BTsocket = BTsocketHandler.getBTsocket();

        try{
            BTsocketHandler.setBluetoothStatus(true);
            BTcomunication(BTsocket);
        }catch (IOException | JSONException e){
            Log.d(TAG, e.toString());
            localintent.putExtra(TAG, "CLOSED");
            LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(localintent);
        }

        BTsocketHandler.setBluetoothStatus(false);
        Log.d(TAG, "Service Ended!");
    }

    private void BTcomunication(BluetoothSocket BTSocket) throws IOException, JSONException {
        JSONObject mainObject;
        BufferedReader input;
        String msg;
        input = new BufferedReader(new InputStreamReader(BTSocket.getInputStream()));

        while ((msg = input.readLine()) != null){
            msg = msg.replace("-127.00", "N/A");
            Log.d(TAG, msg);
            mainObject = new JSONObject(msg);
            BTsocketHandler.setBTdata(mainObject);
            localintent.putExtra(TAG, "OK");
            LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(localintent);
        }
    }

}
