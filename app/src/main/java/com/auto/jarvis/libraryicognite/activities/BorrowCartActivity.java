package com.auto.jarvis.libraryicognite.activities;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
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
import android.widget.TextView;

import com.auto.jarvis.libraryicognite.fragments.BorrowListFragment;
import com.auto.jarvis.libraryicognite.R;
import com.auto.jarvis.libraryicognite.fragments.RecentBooksFragment;
import com.auto.jarvis.libraryicognite.stores.SaveSharedPreference;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BorrowCartActivity extends AppCompatActivity {

    @BindView(R.id.drawer)
    DrawerLayout drawerLayout;
    @BindView(R.id.nvView)
    NavigationView navigationView;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.ivBorrowedList)
    ImageView ivBorrowedList;

    @BindView(R.id.ivRecentBooks)
    ImageView ivRecentBooks;

    @BindView(R.id.tvNewBooks)
    TextView tvNewBooks;

    Fragment borrowListFragment = new BorrowListFragment();
    Fragment recentListFragment = new RecentBooksFragment();

    boolean flag = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_borrow_cart);
        ButterKnife.bind(this);
        Log.d("LIFE", "CART create");

        initView();
        initFooter();


    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        actionBarDrawerToggle.syncState();
    }

    private void initFooter() {
        if (!flag) {
            ivRecentBooks.setEnabled(false);
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
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
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
            case R.id.sign_out:
                SaveSharedPreference.clearAll(this);
                Intent loginIntent = new Intent(this, LoginActivity.class);
                startActivity(loginIntent);
        }
        drawerLayout.closeDrawers();
    }

    private void initContent(boolean flag) {
        replaceContent(borrowListFragment, false, "BorrowListFragment");
        replaceContent(recentListFragment, false, "BorrowListFragment");
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
            getSupportActionBar().setTitle("BORROWING BOOKS");
        } else {
            getSupportActionBar().setTitle("RECENT BORROWED BOOKS");
        }
        getSupportFragmentManager().beginTransaction().show(fragment).commit();
    }

    private void hideFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().hide(fragment).commit();
    }



}
