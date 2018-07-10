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
        BluetoothSocket BTsocket = BTsocketHandler.getBTsocket();
            try{
                BTcomunication(BTsocket);
            }catch (IOException | JSONException e){
                Log.d(TAG, "CLOSED");
                localintent.putExtra(TAG, "CLOSED");
                LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(localintent);
            }
    }

    private void BTcomunication(BluetoothSocket BTSocket) throws IOException, JSONException {
        JSONObject mainObject;
        BufferedReader input;
        input = new BufferedReader(new InputStreamReader(BTSocket.getInputStream()));
        String msg;

        while ((msg = input.readLine()) != null){
            Log.d(TAG, "OK");

            mainObject = new JSONObject(msg);
            BTsocketHandler.setBTdata(mainObject);
            localintent.putExtra(TAG, "OK");
            LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(localintent);
        }
    }

}
