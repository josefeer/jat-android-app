package com.prittysoft.jat;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class BluetoothSuccessFragment extends Fragment {

    //View
    View v;

    //bundles
    Bundle bundle;

    //bluetooth
    String address = null;

    //Intent
    Intent i;

    public BluetoothSuccessFragment() {
        // Required empty public constructor
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        bundle = this.getArguments();
        if (bundle != null){
            address = bundle.getString(DeviceListFragment.EXTRA_ADDRESS, address);
            Log.d("PASSING", "the address is: " + address);

        }

        BTasynk task = new BTasynk(this.getContext(), address, new BTasynk.BTAsyncResponse() {
            @Override
            public void processFinish(boolean ConnectionStatus) {
                if (ConnectionStatus){
                    Log.d("MAIN", "RECEIVED");
                    i = new Intent(getContext(), BTservice.class);
                    getContext().startService(i);
                }
                else{
                    Log.d("MAIN", "NOT RECEIVED");
                }
            }
        });

        task.execute();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_bluetooth_success, container,
                false);

        return v;
    }

}
