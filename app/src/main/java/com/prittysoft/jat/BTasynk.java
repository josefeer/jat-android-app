package com.prittysoft.jat;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.widget.Toast;


import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.UUID;


public class BTasynk extends AsyncTask<Void, Void, Void> {

    private WeakReference<Context> weakContext;
    private ProgressDialog progress;
    private String address = null;

    //Bluetooth
    private BluetoothSocket BTSocket = null;
    private boolean isBTConnected = false;
    private static final UUID myUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    private boolean ConnectSuccess = true;

    private BTAsyncResponse delegate = null;

    public BTasynk(Context context, String address, BTAsyncResponse delegate) {
        weakContext = new WeakReference<>(context);
        this.address = address;
        this.delegate = delegate;
    }

    public interface BTAsyncResponse {
        void processFinish(boolean ConnectionSuccess);
    }

    @Override
    protected void onPreExecute() {
        progress = ProgressDialog.show(weakContext.get(), "Connecting...", "Please wait!!!");
    }

    @Override
    protected Void doInBackground(Void... devices) {
        BluetoothAdapter myBluetooth;
        try {
            if (BTSocket == null || !isBTConnected) {
                myBluetooth = BluetoothAdapter.getDefaultAdapter();
                BTsocketHandler.setMyBluetooth(myBluetooth);

                BluetoothDevice dispositivo = myBluetooth.getRemoteDevice(address);
                BTsocketHandler.setDispositivo(dispositivo);

                BTSocket = dispositivo.createInsecureRfcommSocketToServiceRecord(myUUID);
                BTsocketHandler.setBTsocket(BTSocket);

                BluetoothAdapter.getDefaultAdapter().cancelDiscovery();

                BTSocket.connect();
            }
        } catch (IOException e) {
            ConnectSuccess = false;
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void result) {
        super.onPostExecute(result);
        if (!ConnectSuccess) {
            msg("Connection Failed. Is it a SPP Bluetooth? Try again.");
            //getActivity().getSupportFragmentManager().popBackStackImmediate();
            //weakActivity.get().getFragmentManager().popBackStack();
            FragmentActivity fragmentActivity = (FragmentActivity) weakContext.get();
            fragmentActivity.getSupportFragmentManager().popBackStackImmediate();
        } else {
            msg("Connected.");
            isBTConnected = true;
            BTsocketHandler.setBTsocket(BTSocket);
        }
        progress.dismiss();
        delegate.processFinish(ConnectSuccess);
    }

    private void msg(String s) { //fast way to call Toast
        Toast.makeText(weakContext.get(), s, Toast.LENGTH_LONG).show();
    }

}
