package com.auto.jarvis.libraryicognite.activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import com.auto.jarvis.libraryicognite.adapters.PagerFragmentAdapter;
import com.auto.jarvis.libraryicognite.estimote.BeaconID;
import com.auto.jarvis.libraryicognite.estimote.CheckOutProcess;
import com.auto.jarvis.libraryicognite.interfaces.ApiInterface;
import com.auto.jarvis.libraryicognite.models.input.InitBorrow;
import com.auto.jarvis.libraryicognite.models.output.InformationBookBorrowed;
import com.auto.jarvis.libraryicognite.models.output.RestService;
import com.auto.jarvis.libraryicognite.rest.ApiClient;
import com.auto.jarvis.libraryicognite.stores.SaveSharedPreference;
import com.estimote.sdk.Beacon;
import com.estimote.sdk.BeaconManager;
import com.estimote.sdk.Region;
import com.estimote.sdk.SystemRequirementsChecker;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InsideLibraryActivity extends AppCompatActivity {

    public static final String DEFAULT_UUID = "B9407F30-F5F8-466E-AFF9-25556B57FEED";
    public static final String DEFAULT_IDENTIFIER = "rid";
    private static final Region ALL_ESTIMOTE_BEACON_REGION = new Region(DEFAULT_IDENTIFIER,
            UUID.fromString(DEFAULT_UUID), null, null);

    private BeaconManager beaconManager;
    private BeaconID beaconID;
    ApiInterface apiService, apiCheckout;
    boolean isInit = false;
    @BindView(R.id.tv_location)
    TextView tvLocation;

    @BindView(R.id.drawer)
    DrawerLayout drawerLayout;
    @BindView(R.id.nvView)
    NavigationView navigationView;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inside_library);

        ButterKnife.bind(this);
        initView();

        Intent intentBroadcast = new Intent(InsideLibraryActivity.this, BeaconBroadcast.class);
        sendBroadcast(intentBroadcast);

        beaconManager = new BeaconManager(this);
        beaconManager.setRangingListener(new BeaconManager.RangingListener() {
            @Override
            public void onBeaconsDiscovered(final Region region, final List<Beacon> list) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        List<BeaconID> beaconIDs = new ArrayList<BeaconID>();
                        for (Beacon beacon : list) {
                            beaconIDs.add(BeaconID.fromEstimote(beacon));
                        }

                        startCheckout(region, beaconIDs, SaveSharedPreference.getUsername(InsideLibraryActivity.this));
                    }
                });
            }
        });
//        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (beaconManager == null) {
            beaconManager.disconnect();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (SystemRequirementsChecker.checkWithDefaultDialogs(this)) {
            startScanning();
        }
    }

    private void startScanning() {
        if (beaconManager != null) {
            beaconManager.connect(new BeaconManager.ServiceReadyCallback() {
                @Override
                public void onServiceReady() {
                    beaconManager.startRanging(ALL_ESTIMOTE_BEACON_REGION);
                }
            });
        }
    }


    private void startCheckout(final Region region, List<BeaconID> beaconIDs, String username) {
        if (beaconIDs.size() > 0) {
            apiService = ApiClient.getClient().create(ApiInterface.class);
            for (BeaconID beaconId : beaconIDs) {
                if (isInit == false && beaconId.getMinor() == 1) {
                    initCheckout(username);

                } else if (isInit == true && beaconId.getMinor() == 3) {
                    finishCheckout(username);
                }
            }
        }
    }

    private void initCheckout(String username) {
        InitBorrow initBorrow = new InitBorrow(username, "1");
        Call<RestService<InitBorrow>> callCheckoutInit = apiService.initBorrow(initBorrow);
        callCheckoutInit.enqueue(new Callback<RestService<InitBorrow>>() {
            @Override
            public void onResponse(Call<RestService<InitBorrow>> call, Response<RestService<InitBorrow>> response) {
                if (response.isSuccessful()) {
                    if (response.body().isSucceed()) {
                        tvLocation.setText("You are in Library");
//                        Toast.makeText(InsideLibraryActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        isInit = true;
                    }
                }
            }

            @Override
            public void onFailure(Call<RestService<InitBorrow>> call, Throwable t) {
                Toast.makeText(InsideLibraryActivity.this, "API FAIL WHEN INIT", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void finishCheckout(String username) {
        InitBorrow initBorrow = new InitBorrow(username, "1");
        apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<RestService<List<InformationBookBorrowed>>> callCheckoutInit = apiService.checkout(initBorrow);
        callCheckoutInit.enqueue(new Callback<RestService<List<InformationBookBorrowed>>>() {
            @Override
            public void onResponse(Call<RestService<List<InformationBookBorrowed>>> call, Response<RestService<List<InformationBookBorrowed>>> response) {
                if (response.isSuccessful()) {
                    if (response.body().isSucceed()) {
                        Intent borrowIntent = new Intent(InsideLibraryActivity.this, BorrowCartActivity.class);
                        ArrayList<InformationBookBorrowed> borroweds = (ArrayList<InformationBookBorrowed>) response.body().getData();
                        if (borroweds.size() == 0) {
                            tvLocation.setText("Thank for coming, see you again");
                        } else {
                            borrowIntent.putParcelableArrayListExtra("BORROW_LIST", borroweds);
                            startActivity(borrowIntent);
                        }
//                        Toast.makeText(InsideLibraryActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<RestService<List<InformationBookBorrowed>>> call, Throwable t) {
                Toast.makeText(InsideLibraryActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    private void initView() {

        // Toolbar
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);


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

    private void selectDrawerItem(MenuItem item) {
        switch (item.getItemId()){
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



    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        actionBarDrawerToggle.syncState();
    }
}
