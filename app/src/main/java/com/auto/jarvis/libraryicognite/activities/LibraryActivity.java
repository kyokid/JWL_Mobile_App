package com.auto.jarvis.libraryicognite.activities;

import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.auto.jarvis.libraryicognite.R;
import com.auto.jarvis.libraryicognite.Utils.NetworkUtils;
import com.auto.jarvis.libraryicognite.interfaces.ApiInterface;
import com.auto.jarvis.libraryicognite.rest.ApiClient;
import com.auto.jarvis.libraryicognite.service.BackGroundService;
import com.auto.jarvis.libraryicognite.stores.SaveSharedPreference;
import com.estimote.sdk.connection.DeviceConnectionProvider;
import com.estimote.sdk.connection.scanner.ConfigurableDevicesScanner;
import com.estimote.sdk.connection.scanner.DeviceType;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by HaVH on 2/26/17.
 */

public class LibraryActivity extends AppCompatActivity {
    @BindView(R.id.tv_location)
    TextView tvLocation;

    @BindView(R.id.drawer)
    DrawerLayout drawerLayout;
    @BindView(R.id.nvView)
    NavigationView navigationView;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    ConfigurableDevicesScanner deviceScanner;
    DeviceConnectionProvider connectionProvider;

    String username;

    ApiInterface apiService;

    Intent serviceIntent;

    private static LibraryActivity ins;

    private BroadcastReceiver bluetoothReceiver;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inside_library);
        ButterKnife.bind(this);
        ins = this;

        initView();

        serviceIntent = new Intent(this, BackGroundService.class);

        boolean onBluetooth = !NetworkUtils.checkBluetoothConnection(LibraryActivity.this);
        if (onBluetooth && serviceIntent != null) {
            startService(serviceIntent);
        } else {
            tvLocation.setText(R.string.turn_on_bluetooth);
        }

        bluetoothReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                final String action = intent.getAction();
                if (action.equals(BluetoothAdapter.ACTION_STATE_CHANGED)) {
                    final int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR);
                    switch (state) {
                        case BluetoothAdapter.STATE_TURNING_OFF:
                            //todo dialog for restart app
                            break;

                        case BluetoothAdapter.STATE_TURNING_ON:
                            tvLocation.setText(R.string.turn_on_bluetooth_already);
                            startService(serviceIntent);
                            break;
                    }

                }
            }
        };

        IntentFilter filter1 = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
        registerReceiver(bluetoothReceiver, filter1);
    }

    public static LibraryActivity getInstance() {
        return ins;
    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        actionBarDrawerToggle.syncState();
    }

    private void initView() {
        username = SaveSharedPreference.getUsername(LibraryActivity.this);

        connectionProvider = new DeviceConnectionProvider(this);
        deviceScanner = new ConfigurableDevicesScanner(getApplicationContext());
        deviceScanner.setOwnDevicesFiltering(true);
        deviceScanner.setDeviceTypes(DeviceType.PROXIMITY_BEACON);
        apiService = ApiClient.getClient().create(ApiInterface.class);

        // Toolbar
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        View headerLayout = navigationView.inflateHeaderView(R.layout.drawer_header);

        TextView tvUsername = (TextView) headerLayout.findViewById(R.id.tvUsername);
        tvUsername.setText(username);


        navigationView.setNavigationItemSelectedListener(item -> {
            selectDrawerItem(item);
            return true;
        });
        navigationView.setItemIconTintList(null);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.open, R.string.close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
    }


    private void selectDrawerItem(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.barCodePage:
                Intent intenta = new Intent(this, HomeControllerActivity.class);
                intenta.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intenta);
                break;
            case R.id.your_profile:
                startActivity(ProfileActivity.getIntentNewTask(this));
                break;
            case R.id.borrow_list:
                startActivity(BorrowCartActivity.getIntentNewTask(this));
                break;
            case R.id.borrowed_list:
                Intent intent = new Intent(this, HistoryActivity.class);
                startActivity(intent);
                break;
            case R.id.sign_out:
                SaveSharedPreference.clearAll(this);
                Intent loginIntent = new Intent(this, LoginActivity.class);
                startActivity(loginIntent);
                break;
            case R.id.policy:
                Intent ruleIntent = new Intent(this, RuleActivity.class);
                startActivity(ruleIntent);
                break;
        }
        drawerLayout.closeDrawers();
    }

    @Override
    protected void onDestroy() {
        if (serviceIntent != null) {
            stopService(serviceIntent);
        }
        unregisterReceiver(bluetoothReceiver);
        super.onDestroy();
    }


    public void updateTextView(final String t) {
        LibraryActivity.this.runOnUiThread(() -> tvLocation.setText(t));
    }




}
