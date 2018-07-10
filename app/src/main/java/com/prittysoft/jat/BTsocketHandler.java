package com.prittysoft.jat;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;

import org.json.JSONObject;

public class BTsocketHandler {

    private static BluetoothSocket BTsocket;
    private static BluetoothAdapter myBluetooth;
    private static BluetoothDevice dispositivo;
    private static JSONObject BTdata;

    public static synchronized BluetoothSocket getBTsocket(){
        return BTsocket;
    }

    public static synchronized BluetoothAdapter getMyBluetooth(){
        return myBluetooth;
    }

    public static synchronized BluetoothDevice getDispositivo(){
        return dispositivo;
    }

    public static synchronized JSONObject getBTdata(){
        return BTdata;
    }

    public static synchronized void setBTsocket(BluetoothSocket BTsocket){
        BTsocketHandler.BTsocket = BTsocket;
    }

    public static synchronized void setMyBluetooth(BluetoothAdapter myBluetooth){
        BTsocketHandler.myBluetooth = myBluetooth;
    }

    public static synchronized void setDispositivo(BluetoothDevice dispositivo){
        BTsocketHandler.dispositivo = dispositivo;
    }

    public static synchronized void setBTdata(JSONObject BTdata){
        BTsocketHandler.BTdata = BTdata;
    }

}
