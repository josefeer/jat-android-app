package com.prittysoft.jat;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Set;

public class DeviceListFragment extends Fragment {

    View v;

    //widgets
    Button btn_btserach;
    ListView device_list;

    //bluetooth
    private BluetoothAdapter myBluetooth = null;
    public static String EXTRA_ADDRESS = "device_address";

    //bundle for next fragment
    Bundle bundle = new Bundle();


    public DeviceListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_device_list, container, false);
        btn_btserach = v.findViewById(R.id.btn_btsearch);
        device_list = v.findViewById(R.id.device_list);

        //if device has bluetooth?
        myBluetooth = BluetoothAdapter.getDefaultAdapter();

        if (myBluetooth == null){
            //Show message that the device doesn't have  bluetooth adapter
            Toast.makeText(getContext(), "Bluetooth Device not Available", Toast.LENGTH_LONG).show();
        }
        else if (!myBluetooth.isEnabled()){
            //Ask user to turn on bluetooth
            Intent turnBTon = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(turnBTon, 1);
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
            Toast.makeText(getContext(), "No paired devices found!", Toast.LENGTH_LONG).show();
        }

        final ArrayAdapter adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, list);
        device_list.setAdapter(adapter);
        device_list.setOnItemClickListener(myListClickListener); //Method called when the device from the list is clicked

    }

    private AdapterView.OnItemClickListener myListClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            //Get the device MAC address, last 17 chars in the view
            String info = ((TextView) view).getText().toString();
            String address = info.substring(info.length() - 17);
            bundle.putString(EXTRA_ADDRESS, address);

            BluetoothSuccessFragment fragment = new BluetoothSuccessFragment();
            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
            fragment.setArguments(bundle);
            fragmentTransaction.replace(R.id.base_frame_b, fragment);
            fragmentTransaction.commit();

        }
    };

}
