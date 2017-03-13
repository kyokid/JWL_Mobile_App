package com.auto.jarvis.libraryicognite.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.PowerManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.auto.jarvis.libraryicognite.MyApplication;
import com.auto.jarvis.libraryicognite.R;
import com.auto.jarvis.libraryicognite.Utils.Constant;
import com.auto.jarvis.libraryicognite.Utils.NetworkUtils;
import com.auto.jarvis.libraryicognite.Utils.NotificationUtils;
import com.auto.jarvis.libraryicognite.interfaces.ApiInterface;
import com.auto.jarvis.libraryicognite.models.input.InitBorrow;
import com.auto.jarvis.libraryicognite.models.output.InformationBookBorrowed;
import com.auto.jarvis.libraryicognite.models.output.RestService;
import com.auto.jarvis.libraryicognite.rest.ApiClient;
import com.auto.jarvis.libraryicognite.stores.SaveSharedPreference;
import com.estimote.sdk.connection.DeviceConnectionProvider;
import com.estimote.sdk.connection.scanner.ConfigurableDevice;
import com.estimote.sdk.connection.scanner.ConfigurableDevicesScanner;
import com.estimote.sdk.connection.scanner.DeviceType;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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

    private static final String TAG = "MainActivity";

    ConfigurableDevicesScanner deviceScanner;
    DeviceConnectionProvider connectionProvider;
    ConfigurableDevice device;
    private int status = Constant.CHECK_IN;

    private String username;
    private boolean inLibrary = false;
    Bundle intentFlag;


    ApiInterface apiService;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inside_library);

        ButterKnife.bind(this);
        intentFlag = getIntent().getExtras();
        if (intentFlag != null) {
            inLibrary = intentFlag.getBoolean("IN_LIBRARY");
        }

        initView();

//        checkStatusBorrower(username);
//        startSearching();
        if (NetworkUtils.checkBluetoothConnection(LibraryActivity.this)) {
            startScan();
        }
    }

    private void checkStatusBorrower(String username) {
        apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<RestService<Boolean>> result = apiService.inLibrary(username);
        result.enqueue(new Callback<RestService<Boolean>>() {
            @Override
            public void onResponse(Call<RestService<Boolean>> call, Response<RestService<Boolean>> response) {
                if (response.isSuccessful()) {
                    inLibrary = response.body().getData();
                }
            }

            @Override
            public void onFailure(Call<RestService<Boolean>> call, Throwable t) {

            }
        });
    }

    private void startScan() {
        if (status == Constant.CHECK_IN && inLibrary) {
            deviceScanner.setScanPeriodMillis(5000);
            deviceScanner.scanForDevices(new ConfigurableDevicesScanner.ScannerCallback() {
                @Override
                public void onDevicesFound(List<ConfigurableDevicesScanner.ScanResultItem> list) {
                    Log.d("BEACON", "Number of beacon: " + list.size());
                    Log.d("BEACON", "status: " + status);

                    for (ConfigurableDevicesScanner.ScanResultItem item : list) {
                        String macAddress = item.device.macAddress.toStandardString();
                        if (status == Constant.CHECK_IN && inLibrary && macAddress.equals(Constant.IBEACON_INIT_CHECKOUT_ADDRESS)) {
                            Log.d("BEACON", "init check out");
                            initCheckout(username);

                        } else if (status == Constant.INIT_CHECKOUT && macAddress.equals(Constant.IBEACON_CHECKOUT_COMPLETE_ADDRESS)) {
                            Log.d("BEACON", "Finish");
                            finishCheckout(username);

                        }
                    }
                }
            });
        }
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        startScan();
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
//        tvUsername.setText(username);


        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                selectDrawerItem(item);
                return true;
            }
        });
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.open, R.string.close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
    }


    private void selectDrawerItem(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.barCodePage:
                startActivity(BarCodeActivity.getIntentNewTask(this));
                break;
            case R.id.your_profile:
                startActivity(ProfileActivity.getIntentNewTask(this));
                break;
            case R.id.borrow_list:
                startActivity(BorrowCartActivity.getIntentNewTask(this));
                break;
            case R.id.sign_out:
                SaveSharedPreference.clearAll(this);
                Intent loginIntent = new Intent(this, LoginActivity.class);
                startActivity(loginIntent);
        }
        drawerLayout.closeDrawers();
    }

//    private void startSearching() {
//        MyApplication app = (MyApplication) getApplication();
//        if (!SystemRequirementsChecker.checkWithDefaultDialogs(LibraryActivity.this)) {
//            Log.e(TAG, "Can't scan for beacons, some pre-conditions were not met");
//            Log.e(TAG, "Read more about what's required at: http://estimote.github.io/Android-SDK/JavaDocs/com/estimote/sdk/SystemRequirementsChecker.html");
//            Log.e(TAG, "If this is fixable, you should see a popup on the app's screen right now, asking to enable what's necessary");
//        } else if (!app.isBeaconNotificationsEnabled()) {
//            Log.d(TAG, "Enabling beacon notifications");
//            app.enableBeaconNotifications();
//        }
//    }

    @Override
    protected void onDestroy() {
        connectionProvider.destroy();
        deviceScanner.stopScanning();
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
//        deviceScanner.stopScanning();
    }


    @Override
    protected void onResume() {
        super.onResume();
    }





    private void initCheckout(String username) {
        InitBorrow initBorrow = new InitBorrow(username, "1");
        Call<RestService<InitBorrow>> callCheckoutInit = apiService.initBorrow(initBorrow);
        callCheckoutInit.enqueue(new Callback<RestService<InitBorrow>>() {
            @Override
            public void onResponse(Call<RestService<InitBorrow>> call, Response<RestService<InitBorrow>> response) {
                String message = response.body().getTextMessage();
                if (response.isSuccessful()) {
                    if (response.body().isSucceed()) {
                        Log.d("BEACON", "message = " + message);
                        tvLocation.setText(message);
                        status = Constant.INIT_CHECKOUT;
                    } else {
                        tvLocation.setText(message);
                    }
                } else {
                    tvLocation.setText(message);
                }
            }

            @Override
            public void onFailure(Call<RestService<InitBorrow>> call, Throwable t) {
                call.cancel();
                Toast.makeText(LibraryActivity.this, "API FAIL WHEN INIT", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void finishCheckout(String username) {
        deviceScanner.stopScanning();
        status = Constant.LOGIN;
        InitBorrow initBorrow = new InitBorrow(username, "1");
        apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<RestService<List<InformationBookBorrowed>>> callCheckoutInit = apiService.checkout(initBorrow);
        callCheckoutInit.enqueue(new Callback<RestService<List<InformationBookBorrowed>>>() {
            @Override
            public void onResponse(Call<RestService<List<InformationBookBorrowed>>> call, Response<RestService<List<InformationBookBorrowed>>> response) {
                if (response.isSuccessful()) {
                    String message = response.body().getTextMessage();
                    if (response.body().isSucceed()) {
                        Intent borrowIntent = new Intent(LibraryActivity.this, BorrowCartActivity.class);
                        ArrayList<InformationBookBorrowed> recentList = (ArrayList<InformationBookBorrowed>) response.body().getData();
                        if (recentList == null || recentList.size() == 0) {
                            Log.d("BEACON", "message = " + message);
                            tvLocation.setText(message);
                        } else {
                            Log.d("BEACON", "message = " + message);
                            NotificationUtils.showNotification(getApplicationContext(), message, recentList);

                            PowerManager powerManager = (PowerManager) getSystemService(POWER_SERVICE);
                            PowerManager.WakeLock wl = powerManager.newWakeLock(PowerManager.ACQUIRE_CAUSES_WAKEUP, "checkout");
                            wl.acquire(5000);
                            wl.release();

                            borrowIntent.putExtra("RESULT", true);
                            borrowIntent.putParcelableArrayListExtra("RECENT_LIST", recentList);
//                            borrowIntent.setFlags(1);
                            if (!MyApplication.isBackgroundMode()) {
                                startActivity(borrowIntent);
                            }
                        }
//                        Toast.makeText(InsideLibraryActivity.this, response.body().getTextMessage(), Toast.LENGTH_SHORT).show();
                    } else {
                        tvLocation.setText(message);
                    }
                }
            }

            @Override
            public void onFailure(Call<RestService<List<InformationBookBorrowed>>> call, Throwable t) {
                Toast.makeText(LibraryActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }


}
