package com.prittysoft.jat;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    private MenuItem bluetooth_icon;
    private Button main_button;

    private RecentsFragment fragment1 = new RecentsFragment();
    private AddFragment fragment2 = new AddFragment();
    private RegisterFragment fragment3 = new RegisterFragment();

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_recents:
                    FragmentTransaction fragmentTransaction1 = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction1.replace(R.id.base_frame, fragment1);
                    fragmentTransaction1.commit();
                    return true;
                case R.id.navigation_add:
                    FragmentTransaction fragmentTransaction2 = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction2.replace(R.id.base_frame, fragment2);
                    fragmentTransaction2.commit();
                    return true;
                case R.id.navigation_registers:
                    FragmentTransaction fragmentTransaction3 = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction3.replace(R.id.base_frame, fragment3);
                    fragmentTransaction3.commit();
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

        RecentsFragment fragment1 = new RecentsFragment();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.base_frame, fragment1);
        fragmentTransaction.commit();

        IntentFilter filter = new IntentFilter("work");
        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver, filter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver);
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

    protected BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {

        String BTservice;

        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d("MainActivity", "listening");
            BTservice = intent.getStringExtra("BTservice");
            Log.d("intent", BTservice);
            if (BTservice.equals("CLOSED") || BTservice == null){
                bluetooth_icon.setIcon(R.drawable.toolbar_bluetooth_disabled);
                AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
                alertDialog.setTitle("Ops!");
                alertDialog.setMessage("Device Desconnected");
                alertDialog.show();

            }
            else{
                bluetooth_icon.setIcon(R.drawable.toolbar_bluetooth_enabled);
            }

        }

    };
}
