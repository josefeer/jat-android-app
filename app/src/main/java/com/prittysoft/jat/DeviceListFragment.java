package com.prittysoft.jat;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.util.Log;

import java.util.ArrayList;
import java.util.Set;

public class DeviceListFragment extends Fragment {

    View v;

    //widgets
    Button btn_btserach;
    ListView device_list;

    private static final String TAG = "DeviceListFragment";

    //bluetooth
    private BluetoothAdapter myBluetooth = null;
    public static String EXTRA_ADDRESS = "device_address";

    Context context;

    Intent i;


    public DeviceListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_device_list, container, false);
        btn_btserach = v.findViewById(R.id.btn_btsearch);
        device_list = v.findViewById(R.id.device_list);

        //if device has bluetooth?
        myBluetooth = BluetoothAdapter.getDefaultAdapter();

        if (myBluetooth == null){
            //Show message that the device doesn't have  bluetooth adapter
            Toast.makeText(getContext(), "Dispositivo SIN bluetooth", Toast.LENGTH_LONG).show();
        }
        else if (!myBluetooth.isEnabled()){
            //Ask user to turn on bluetooth
            Intent turnBTon = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(turnBTon, 1);
        }
        else if(myBluetooth.isEnabled()){
            pairedDevicesList();
        }

        btn_btserach.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pairedDevicesList();
            }
        });
        return v;
    }


    private void pairedDevicesList(){
        Set<BluetoothDevice> pairedDevices;
        pairedDevices = myBluetooth.getBondedDevices();
        ArrayList<String> list = new ArrayList<>();

        if (pairedDevices.size() > 0){
            for (BluetoothDevice bt : pairedDevices){
                //Get the device's name  and the address
                list.add(bt.getName() + "\n" + bt.getAddress());
            }
        }
        else{
            Toast.makeText(getContext(), "NO se encontraron dispositivos!", Toast.LENGTH_LONG).show();
        }

        final ArrayAdapter adapter = new ArrayAdapter<>(this.context, android.R.layout.simple_list_item_1, list);
        device_list.setAdapter(adapter);
        device_list.setOnItemClickListener(myListClickListener); //Method called when the device from the list is clicked

    }

    private AdapterView.OnItemClickListener myListClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            //Get the device MAC address, last 17 chars in the view
            String info = ((TextView) view).getText().toString();
            String address = info.substring(info.length() - 17);

            BTasynk task = new BTasynk(context, address, new BTasynk.BTAsyncResponse() {
                @Override
                public void processFinish(boolean ConnectionStatus) {
                    if (ConnectionStatus){
                        Log.d(TAG, "AsynkTask Ended: Initiating BTservice");
                        i = new Intent(context, BTservice.class);
                        context.startService(i);
                        ((Activity) context).finish();
                    }
                    else{
                        Log.d(TAG, "AsynkTask Ended: Connection Failed!");
                    }
                }
            });

            task.execute();


        }
    };

}
