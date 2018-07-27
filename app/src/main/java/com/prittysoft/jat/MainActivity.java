package com.prittysoft.jat;

import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private MenuItem bluetooth_icon;

    RecentsFragment fragment1 = new RecentsFragment();
    final AddFragment fragment2 = new AddFragment();
    final RegisterFragment fragment3 = new RegisterFragment();

    final FragmentManager fm = getSupportFragmentManager();

    Fragment active = fragment1;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            switch (item.getItemId()) {

                case R.id.navigation_recents:
                    fm.beginTransaction().remove(fragment1).commit();
                    fragment1 = new RecentsFragment();
                    fm.beginTransaction().hide(active).commit();
                    fm.beginTransaction().add(R.id.base_frame, fragment1, "1").commit();

                    active = fragment1;
                    return true;

                case R.id.navigation_add:
                    fm.beginTransaction().hide(active).show(fragment2).commit();
                    active = fragment2;
                    return true;

                case R.id.navigation_registers:
                    fm.beginTransaction().hide(active).show(fragment3).commit();
                    active = fragment3;
                    return true;

            }

            return false;

        }

    };

    private void setupToolbar(){
        Toolbar mytoolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mytoolbar);
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
    }

    private void setupBottomNav(){
        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

            setupToolbar();
            setupBottomNav();

            BottomNavigationView navigation = findViewById(R.id.navigation);
            navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

            fm.beginTransaction().add(R.id.base_frame, fragment3, "3").hide(fragment3).commit();
            fm.beginTransaction().add(R.id.base_frame, fragment2, "2").hide(fragment2).commit();
            fm.beginTransaction().add(R.id.base_frame, fragment1, "1").commit();

            IntentFilter filter = new IntentFilter("work");
            LocalBroadcastManager.getInstance(this).registerReceiver(BTserviceReceiver, filter);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(BTserviceReceiver);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        bluetooth_icon = menu.findItem(R.id.toolbar_bluetooth);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){

            case R.id.toolbar_bluetooth:
                Intent BTActivity = new Intent(this, BluetoothActivity.class);
                startActivity(BTActivity);
                return true;

        }

        return super.onOptionsItemSelected(item);
    }

    protected BroadcastReceiver BTserviceReceiver = new BroadcastReceiver() {

        String BTservice;

        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d(TAG, "Listening to BTservice");
            BTservice = intent.getStringExtra("BTservice");
            if (BTservice.equals("CLOSED") || BTservice == null){
                try {
                    BluetoothSocket socket = BTsocketHandler.getBTsocket();
                    socket.close();
                }catch (IOException e){
                    Log.d(TAG, e.toString());
                }
                bluetooth_icon.setIcon(R.drawable.toolbar_bluetooth_disabled);
                AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
                alertDialog.setTitle("OOPS!");
                alertDialog.setMessage("Dispositivo Desconectado!");
                alertDialog.show();

            }
            else{
                bluetooth_icon.setIcon(R.drawable.toolbar_bluetooth_enabled);
            }

        }

    };

}
