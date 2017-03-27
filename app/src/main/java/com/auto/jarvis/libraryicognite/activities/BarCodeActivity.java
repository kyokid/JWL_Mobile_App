package com.auto.jarvis.libraryicognite.activities;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.auto.jarvis.libraryicognite.R;
import com.auto.jarvis.libraryicognite.Utils.Constant;
import com.auto.jarvis.libraryicognite.Utils.NotificationUtils;
import com.auto.jarvis.libraryicognite.fragments.QRCodePagerFragment;
import com.auto.jarvis.libraryicognite.fragments.SearchFragment;
import com.auto.jarvis.libraryicognite.interfaces.ApiInterface;
import com.auto.jarvis.libraryicognite.models.Book;
import com.auto.jarvis.libraryicognite.models.output.RestService;
import com.auto.jarvis.libraryicognite.rest.ApiClient;
import com.auto.jarvis.libraryicognite.stores.SaveSharedPreference;
import com.estimote.sdk.Beacon;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.R.attr.fragment;
import static android.content.Intent.FLAG_ACTIVITY_PREVIOUS_IS_TOP;
import static java.security.AccessController.getContext;

public class BarCodeActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.drawer)
    DrawerLayout drawerLayout;
    @BindView(R.id.nvView)
    NavigationView navigationView;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    String[] tabTitle;
    ApiInterface apiService;


    private BroadcastReceiver mRegistrationBroadcastReceiver;
    private final String BARCODE_FRAGMENT_TAG = "barcode_fragment";
    private final String SEARCH_FRAGMENT_TAG = "search_fragment";
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bar_code);
        ButterKnife.bind(this);
        userId = SaveSharedPreference.getUsername(getBaseContext());
        NotificationUtils.sendNewIdToServer(userId, FirebaseInstanceId.getInstance().getToken());
//            String userId = SaveSharedPreference.getUsername(BarCodeActivity.this);
//            Intent service = new Intent(BarCodeActivity.this, IntanceNotificationIDService.class);
//            this.startService(service);
        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                // checking for type intent filter
                if (intent.getAction().equals(Constant.REGISTRATION_COMPLETE)) {
                    Log.d("Registration token:" , intent.getStringExtra("token"));
                    NotificationUtils.sendNewIdToServer(userId, intent.getStringExtra("token"));
                } else if (intent.getAction().equals(Constant.PUSH_NOTIFICATION)) {
                    String message = intent.getStringExtra("message");
                    Log.d("Push notification:", message);
                    Intent intentLibrary = new Intent(getBaseContext(), LibraryActivity.class);
                    SaveSharedPreference.setStatusUser(getApplicationContext(), Constant.CHECK_IN);
                    int status = SaveSharedPreference.getStatusUser(getApplicationContext());
                    Log.d("BEACON", "STATUS AFTER SET: " + status);
                    startActivity(intentLibrary);
                }
            }
        };

        IntentFilter filter = new IntentFilter(Constant.PUSH_NOTIFICATION);
        registerReceiver(mRegistrationBroadcastReceiver, filter);

        FragmentManager fragmentManager = getSupportFragmentManager();
        QRCodePagerFragment fragment = QRCodePagerFragment.newInstance();
        fragmentManager.beginTransaction().replace(R.id.flBarcodeActivity, fragment).commit();

        initView(SaveSharedPreference.getUsername(getBaseContext()));
    }


    private void initView(String userId) {
        tabTitle = getResources().getStringArray(R.array.tab_title);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        View headerLayout = navigationView.inflateHeaderView(R.layout.drawer_header);
        TextView tvUsername = (TextView) headerLayout.findViewById(R.id.tvUsername);
        tvUsername.setText(userId);
        navigationView.setNavigationItemSelectedListener(item -> {
            selectDrawerItem(item);
            return true;
        });
    }

    private Bitmap fromStringToBitmap(String content) throws WriterException {
        QRCodeWriter writer = new QRCodeWriter();
        BitMatrix bitMatrix = writer.encode(content, BarcodeFormat.QR_CODE, 1000, 1000);
        int width = bitMatrix.getWidth();
        int height = bitMatrix.getHeight();
        Bitmap bmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                bmp.setPixel(x, y, bitMatrix.get(x, y) ? Color.BLACK : Color.WHITE);
            }
        }
        return bmp;
    }

    public static Intent getIntentNewTask(Context context) {
        Intent intent = new Intent(context, BarCodeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | FLAG_ACTIVITY_PREVIOUS_IS_TOP);
        return intent;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mRegistrationBroadcastReceiver);
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.open, R.string.close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
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

    @Override
    protected void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Constant.PUSH_NOTIFICATION));
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Constant.REGISTRATION_COMPLETE));
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
                if (f instanceof SearchFragment){
                    ((SearchFragment) f).search(query, userId);
                }else {
                    SearchFragment fragment = SearchFragment.newInstance(query);
                    fragmentManager.beginTransaction().replace(R.id.flBarcodeActivity, fragment, SEARCH_FRAGMENT_TAG).commit();
                    fragment.search(query, userId);
                }
                searchView.clearFocus();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                Fragment f = fragmentManager.findFragmentByTag(SEARCH_FRAGMENT_TAG);
                if (f instanceof SearchFragment){
                    ((SearchFragment) f).resetView();
                }
                return false;
            }
        });
        searchView.setOnCloseListener(() -> {
            Fragment f = getSupportFragmentManager().findFragmentByTag(SEARCH_FRAGMENT_TAG);
            if (f instanceof SearchFragment){
                FragmentManager fragmentManager = getSupportFragmentManager();
                QRCodePagerFragment fragment1 = QRCodePagerFragment.newInstance();
                fragmentManager.beginTransaction().replace(R.id.flBarcodeActivity, fragment1).commit();
            }
            return false;
        });
        return true;
    }
}
