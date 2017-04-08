package com.auto.jarvis.libraryicognite.activities;

import android.app.SearchManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ContextThemeWrapper;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.auto.jarvis.libraryicognite.R;
import com.auto.jarvis.libraryicognite.Utils.Constant;
import com.auto.jarvis.libraryicognite.Utils.NotificationUtils;
import com.auto.jarvis.libraryicognite.fragments.QRCodePagerFragment;
import com.auto.jarvis.libraryicognite.fragments.SearchFragment;
import com.auto.jarvis.libraryicognite.interfaces.ApiInterface;
import com.auto.jarvis.libraryicognite.models.output.RestService;
import com.auto.jarvis.libraryicognite.rest.ApiClient;
import com.auto.jarvis.libraryicognite.stores.SaveSharedPreference;
import com.google.firebase.iid.FirebaseInstanceId;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;
import static com.auto.jarvis.libraryicognite.R.id.tvUsername;

public class BarCodeActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.drawer)
    DrawerLayout drawerLayout;
    @BindView(R.id.nvView)
    NavigationView navigationView;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    ApiInterface apiService;


    private BroadcastReceiver mRegistrationBroadcastReceiver;
    private final String BARCODE_FRAGMENT_TAG = "barcode_fragment";
    private final String SEARCH_FRAGMENT_TAG = "search_fragment";
    private String userId;
    private boolean inLibrary;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bar_code);
        ButterKnife.bind(this);
        Log.d("LIFE", "Barcode CREATE");
        userId = SaveSharedPreference.getUsername(getBaseContext());

        if (actionBarDrawerToggle != null){
            actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                    R.string.open, R.string.close);
            drawerLayout.addDrawerListener(actionBarDrawerToggle);
            actionBarDrawerToggle.syncState();
        }
        View headerLayout = navigationView.inflateHeaderView(R.layout.drawer_header);
        TextView tvUsername = (TextView) headerLayout.findViewById(R.id.tvUsername);
        tvUsername.setText(userId);
        navigationView.setNavigationItemSelectedListener(item -> {
            selectDrawerItem(item);
            return true;
        });
        NotificationUtils.sendNewIdToServer(userId, FirebaseInstanceId.getInstance().getToken());
//            String userId = SaveSharedPreference.getUsername(BarCodeActivity.this);
//            Intent service = new Intent(BarCodeActivity.this, IntanceNotificationIDService.class);
//            this.startService(service);
        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                // checking for type intent filter
                if (intent.getAction().equals(Constant.REGISTRATION_COMPLETE)) {
                    Log.d("Registration token:", intent.getStringExtra("token"));
                    NotificationUtils.sendNewIdToServer(userId, intent.getStringExtra("token"));
                } else if (intent.getAction().equals(Constant.PUSH_NOTIFICATION)) {
                    String message = intent.getStringExtra("message");
                    if (message.equals("true")) {
                        Log.d("Current thread:", Thread.currentThread().getName());
                        Log.d("Push notification:", message);
                        Intent intentLibrary = new Intent(getBaseContext(), LibraryActivity.class);
                        SaveSharedPreference.setStatusUser(getApplicationContext(), Constant.CHECK_IN);
                        startActivity(intentLibrary);
                    } else {
                        new AlertDialog.Builder(new ContextThemeWrapper(BarCodeActivity.this, R.style.myDialog))
                                .setTitle("Check-in Fail")
                                .setMessage("Please restart your QR Code")
                                .setPositiveButton(android.R.string.yes, (dialog, which) -> dialog.dismiss())
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .show();
                    }
                }
            }
        };

        IntentFilter filter = new IntentFilter();
        filter.addAction(Constant.PUSH_NOTIFICATION);
        filter.addAction(Constant.REGISTRATION_COMPLETE);
        registerReceiver(mRegistrationBroadcastReceiver, filter);
    }


    private void initView(String userId) {
        Log.d("LIFE", "Barcode init view");
        FragmentManager fragmentManager = getSupportFragmentManager();
        QRCodePagerFragment fragment = QRCodePagerFragment.newInstance();
        fragmentManager.beginTransaction().add(R.id.flBarcodeActivity, fragment).commit();


        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle(R.string.home);


    }

    public static Intent getIntentNewTask(Context context) {
        Intent intent = new Intent(context, BarCodeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | FLAG_ACTIVITY_NEW_TASK);
        return intent;
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mRegistrationBroadcastReceiver);
    }

    @Override
    protected void onStart() {
        super.onStart();
        apiService = ApiClient.getClient().create(ApiInterface.class);
        checkStatusBorrower(userId)
                .doOnNext(booleanRestService -> {
                    if (booleanRestService.getData()) {
                        inLibrary = booleanRestService.getData();
                        Intent intentControl = new Intent(BarCodeActivity.this, LibraryActivity.class);
                        SaveSharedPreference.setStatusUser(getApplicationContext(), Constant.CHECK_IN);
                        intentControl.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intentControl);
                        finish();
                    } else {
                        initView(SaveSharedPreference.getUsername(getBaseContext()));
                    }
                })
                .subscribe(booleanRestService -> inLibrary = booleanRestService.getData());
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

    }

    private void selectDrawerItem(MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {
            case R.id.barCodePage:
                intent = new Intent(this, BarCodeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                break;
            case R.id.your_profile:
                intent = new Intent(this, ProfileActivity.class);
                startActivity(intent);
                break;
            case R.id.borrow_list:
                intent = new Intent(this, BorrowCartActivity.class);
                startActivity(intent);
                break;
            case R.id.borrowed_list:
                intent = new Intent(this, HistoryActivity.class);
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
    public void onBackPressed() {

        int count = getSupportFragmentManager().getBackStackEntryCount();
        if (count != 0) {
            Intent intent = new Intent(this, BarCodeActivity.class);
            startActivity(intent);
        }

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

    @Override
    protected void onResume() {
        super.onResume();
        Toast.makeText(this, "Onresume", Toast.LENGTH_SHORT).show();
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Constant.PUSH_NOTIFICATION));
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Constant.REGISTRATION_COMPLETE));
//        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
//                R.string.open, R.string.close);
//        drawerLayout.addDrawerListener(actionBarDrawerToggle);
//        if (actionBarDrawerToggle != null)
//            actionBarDrawerToggle.syncState();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.action_search));
        SearchManager searchManager = (SearchManager) getSystemService(SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                Fragment f = fragmentManager.findFragmentByTag(SEARCH_FRAGMENT_TAG);
                if (f instanceof SearchFragment) {
                    ((SearchFragment) f).search(query, userId);
                } else {
                    SearchFragment fragment = SearchFragment.newInstance(query);
                    fragmentManager.beginTransaction().replace(R.id.flBarcodeActivity, fragment, SEARCH_FRAGMENT_TAG).addToBackStack("search").commit();
                    fragment.search(query, userId);
                }
                searchView.clearFocus();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                Fragment f = fragmentManager.findFragmentByTag(SEARCH_FRAGMENT_TAG);
                if (f instanceof SearchFragment) {
                    ((SearchFragment) f).resetView();
                }
                return false;
            }
        });
        searchView.setOnCloseListener(() -> {
            Fragment f = getSupportFragmentManager().findFragmentByTag(SEARCH_FRAGMENT_TAG);
            if (f instanceof SearchFragment) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                QRCodePagerFragment fragment1 = QRCodePagerFragment.newInstance();
                fragmentManager.beginTransaction().replace(R.id.flBarcodeActivity, fragment1).commit();
            }
            return false;
        });
        return true;
    }

    private Observable<RestService<Boolean>> checkStatusBorrower(String userId) {
        return apiService.userStatus(userId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
