package com.auto.jarvis.libraryicognite.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.auto.jarvis.libraryicognite.BorrowCartActivity;
import com.auto.jarvis.libraryicognite.LoginActivity;
import com.auto.jarvis.libraryicognite.R;
import com.auto.jarvis.libraryicognite.Utils.Constant;
import com.auto.jarvis.libraryicognite.Utils.NotificationUtils;
import com.auto.jarvis.libraryicognite.adapters.PagerFragmentAdapter;
import com.auto.jarvis.libraryicognite.interfaces.ApiInterface;
import com.auto.jarvis.libraryicognite.stores.SaveSharedPreference;
import com.estimote.sdk.Beacon;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BarCodeActivity extends AppCompatActivity {

//    public static final String DEFAULT_UUID = "B9407F30-F5F8-466E-AFF9-25556B57FEED";
//    public static final String DEFAULT_IDENTIFIER = "rid";
//    private static final Region ALL_ESTIMOTE_BEACON_REGION  = new Region(DEFAULT_IDENTIFIER,
//            UUID.fromString(DEFAULT_UUID), null, null);
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.viewPager)
    ViewPager viewPager;
    @BindView(R.id.tablayout)
    TabLayout tabLayout;
    @BindView(R.id.drawer)
    DrawerLayout drawerLayout;
    @BindView(R.id.nvView)
    NavigationView navigationView;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    String[] tabTitle;
    private String userId;
    ApiInterface apiService;
    private BroadcastReceiver mRegistrationBroadcastReceiver;

//    private BeaconManager beaconManager;
    private List<Beacon> beacons = new ArrayList<>();
//    private CheckOutProcess checkout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bar_code);
        ButterKnife.bind(this);
        String userId = SaveSharedPreference.getUsername(BarCodeActivity.this);
        if (SaveSharedPreference.getUsername(BarCodeActivity.this).length() == 0) {
            Intent intent = new Intent(BarCodeActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        } else {
//            Toast.makeText(getBaseContext(), getResources().getString(R.string.google_api_key), Toast.LENGTH_SHORT).show();
            Log.d("API key = ", FirebaseInstanceId.getInstance().getToken());
            NotificationUtils.sendNewIdToServer(FirebaseInstanceId.getInstance().getToken());
//            String userId = SaveSharedPreference.getUsername(BarCodeActivity.this);
//            Intent service = new Intent(BarCodeActivity.this, IntanceNotificationIDService.class);
//            this.startService(service);
            mRegistrationBroadcastReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    // checking for type intent filter
                    if (intent.getAction().equals(Constant.REGISTRATION_COMPLETE)) {
                        Log.d("Registration token:" , intent.getStringExtra("token"));
                        Toast.makeText(context, "New token is generated!", Toast.LENGTH_SHORT).show();
                        NotificationUtils.sendNewIdToServer(intent.getStringExtra("token"));
                    } else if (intent.getAction().equals(Constant.PUSH_NOTIFICATION)) {
                        String message = intent.getStringExtra("message");
                        Log.d("Push notification:", message);
                        Toast.makeText(context, "Push notification: " + message, Toast.LENGTH_LONG).show();
                        Intent intentLibrary = new Intent(BarCodeActivity.this, InsideLibraryActivity.class);
                        startActivity(intentLibrary);

                    }
                }
            };
            IntentFilter filter = new IntentFilter(Constant.PUSH_NOTIFICATION);
            registerReceiver(mRegistrationBroadcastReceiver, filter);
            initView(userId);
        }
//        checkout = new CheckOutProcess();
//        beaconManager = new BeaconManager(this);
//        beaconManager.setRangingListener(new BeaconManager.RangingListener() {
//            @Override
//            public void onBeaconsDiscovered(Region region, final List<Beacon> list) {
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        List<BeaconID> beaconIDs = new ArrayList<BeaconID>();
//                        for (Beacon beacon: list) {
//                            beaconIDs.add(BeaconID.fromEstimote(beacon));
//                        }
//                        checkout.startCheckout(beaconIDs, username);
//                    }
//                });
//            }
//        });



//        String macAddress =
//        Call<List<InitBorrow>> call = apiService.initBorrow()

    }

    private void initView(String userId) {

        tabTitle = getResources().getStringArray(R.array.tab_title);
        // Toolbar
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        viewPager.setAdapter(new PagerFragmentAdapter(getSupportFragmentManager(), userId));
        tabLayout.setupWithViewPager(viewPager);
        for (int i = 0; i < tabLayout.getTabCount(); i++) {
//            tabLayout.getTabAt(i).setIcon(icons[i]);
            tabLayout.getTabAt(i).setText(tabTitle[i].toString());

        }

        View headerLayout = navigationView.inflateHeaderView(R.layout.drawer_header);

        TextView tvUsername = (TextView) headerLayout.findViewById(R.id.tvUsername);
//        tvUsername.setText(user.getUsername());


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

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        actionBarDrawerToggle.syncState();
    }

    private void selectDrawerItem(MenuItem item) {
        switch (item.getItemId()){
            case R.id.your_profile:
                startActivity(ProfileActivity.getIntentNewTask(this));
                break;
            case R.id.borrow_list:
                startActivity(BorrowCartActivity.getIntentNewTask(this));
                break;
        }
        drawerLayout.closeDrawers();
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawers();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        if (beaconManager == null) {
//            beaconManager.disconnect();
//        }
//    }

    @Override
    protected void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Constant.PUSH_NOTIFICATION));
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Constant.REGISTRATION_COMPLETE));
//        if (SystemRequirementsChecker.checkWithDefaultDialogs(this)) {
//            startScanning();
//        }
    }
//
//    private void startScanning() {
//        if ( beaconManager != null) {
//            beaconManager.connect(new BeaconManager.ServiceReadyCallback() {
//                @Override
//                public void onServiceReady() {
//                    beaconManager.startRanging(ALL_ESTIMOTE_BEACON_REGION);
//                }
//            });
//        }
//    }


}
