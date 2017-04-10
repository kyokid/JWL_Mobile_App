package com.auto.jarvis.libraryicognite.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.auto.jarvis.libraryicognite.Utils.Constant;
import com.auto.jarvis.libraryicognite.Utils.NotificationUtils;
import com.auto.jarvis.libraryicognite.Utils.RxUltils;
import com.auto.jarvis.libraryicognite.fragments.BorrowListFragment;
import com.auto.jarvis.libraryicognite.R;
import com.auto.jarvis.libraryicognite.fragments.RecentBooksFragment;
import com.auto.jarvis.libraryicognite.fragments.SearchFragment;
import com.auto.jarvis.libraryicognite.stores.SaveSharedPreference;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class BorrowCartActivity extends AppCompatActivity {

    @BindView(R.id.drawer)
    DrawerLayout drawerLayout;
    @BindView(R.id.nvView)
    NavigationView navigationView;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.ivBorrowedList)
    ImageButton ivBorrowedList;

    @BindView(R.id.ivRecentBooks)
    ImageButton ivRecentBooks;

    @BindView(R.id.tvNewBooks)
    TextView tvNewBooks;

    Fragment borrowListFragment = new BorrowListFragment();
    Fragment recentListFragment = new RecentBooksFragment();

    boolean flag = false;
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    private String mUserId;
    private final String BORROWING_BOOK_FRAGMENT = "BorrowListFragment";
    private boolean isNew = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_borrow_cart);
        ButterKnife.bind(this);
        Log.d("LIFE", "CART create");
        mUserId = SaveSharedPreference.getUsername(getBaseContext());

        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                // checking for type intent filter
                if (intent.getAction().equals(Constant.REFRESH_LIST)) {
                    System.out.println("da bat dc event refresh");
                    isNew = true;
                    invalidateOptionsMenu();

                }
            }
        };
        RxUltils.checkConnectToServer()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(isOnline -> {
                    if (!isOnline) {
                        Intent intent = new Intent(BorrowCartActivity.this, NoInternetActivity.class);
                        intent.putExtra("FROM", this.getClass().getCanonicalName());
                        startActivity(intent);
                        finish();
                    } else {
                        initView();
                        initFooter();
                    }
                });
        IntentFilter filter = new IntentFilter();
        filter.addAction(Constant.REFRESH_LIST);
        registerReceiver(mRegistrationBroadcastReceiver, filter);
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.open, R.string.close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        if (actionBarDrawerToggle != null)
        actionBarDrawerToggle.syncState();
    }

    @Override
    protected void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Constant.REFRESH_LIST));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mRegistrationBroadcastReceiver);
    }

    private void initFooter() {
        if (!flag) {
//            ivRecentBooks.setEnabled(false);
            tvNewBooks.setEnabled(false);
        } else {
            ivRecentBooks.setEnabled(true);
            tvNewBooks.setEnabled(true);
            tvNewBooks.setVisibility(View.VISIBLE);
        }

        ivBorrowedList.setOnClickListener(view -> {
            showFragment(borrowListFragment);
            hideFragment(recentListFragment);
        });

        ivRecentBooks.setOnClickListener(view -> {
            showFragment(recentListFragment);
            hideFragment(borrowListFragment);
            tvNewBooks.setVisibility(View.INVISIBLE);
        });
        tvNewBooks.setOnClickListener(view -> {
            showFragment(recentListFragment);
            hideFragment(borrowListFragment);
        });
    }

    public static Intent getIntentNewTask(Context context) {
        Intent intent = new Intent(context, BorrowCartActivity.class);
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        return intent;
    }

    private void initView() {
        // Toolbar
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);


        View headerLayout = navigationView.inflateHeaderView(R.layout.drawer_header);

        String userId = SaveSharedPreference.getUsername(getBaseContext());
        TextView tvUsername = (TextView) headerLayout.findViewById(R.id.tvUsername);
        tvUsername.setText(userId);


        navigationView.setNavigationItemSelectedListener(item -> {
            selectDrawerItem(item);
            return true;
        });
        navigationView.setItemIconTintList(null);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.open, R.string.close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);


        getSupportActionBar().setTitle("BORROWED LIST");

        Bundle intent = getIntent().getExtras();
        if (intent != null) {
            flag = intent.getBoolean("RESULT");
        }

        initContent(flag);
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

    private void initContent(boolean flag) {
        replaceContent(borrowListFragment, false, "BorrowListFragment");
        replaceContent(recentListFragment, false, "RecentListFragment");
        if (flag) {
            showFragment(recentListFragment);
            hideFragment(borrowListFragment);
        } else {
            hideFragment(recentListFragment);
            showFragment(borrowListFragment);
        }
    }

    private void replaceContent(Fragment fragment, boolean exist, String tag) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        if (exist) {
            ft.show(fragment);
        } else {
            ft.add(R.id.content, fragment, tag);
        }
        ft.commit();
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawers();
        } else {
            super.onBackPressed();
        }
    }


    private void showFragment(Fragment fragment) {
        if (fragment instanceof BorrowListFragment) {
            getSupportActionBar().setTitle(R.string.borrowing_book);
            getSupportFragmentManager().beginTransaction().show(fragment).commit();
        } else {
            getSupportActionBar().setTitle(R.string.recent_borrow_book);
            getSupportFragmentManager().beginTransaction().show(fragment).commit();
        }

    }

    private void hideFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().hide(fragment).commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.borrow_books_menu, menu);
        menu.getItem(0).setVisible(isNew);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.newFresh){
            showFragment(borrowListFragment);
            isNew = false;
            item.setVisible(isNew);
            FragmentManager fragmentManager = getSupportFragmentManager();
            Fragment f = fragmentManager.findFragmentByTag(BORROWING_BOOK_FRAGMENT);
            if (f instanceof BorrowListFragment){
                ((BorrowListFragment) f).refreshList();
            }
        }
        return super.onOptionsItemSelected(item);
    }
    public void setIsNewFlag(boolean isNew){
        this.isNew = isNew;
        invalidateOptionsMenu();
    }
}
