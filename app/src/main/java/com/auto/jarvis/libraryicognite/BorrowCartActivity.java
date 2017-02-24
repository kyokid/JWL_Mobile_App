package com.auto.jarvis.libraryicognite;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BorrowCartActivity extends AppCompatActivity {

    @BindView(R.id.ivBorrowedList)
    ImageView ivBorrowedList;

    @BindView(R.id.ivRecentBooks)
    ImageView ivRecentBooks;

    Fragment borrowListFragment = new BorrowListFragment();
    Fragment recentListFragment = new RecentBooksFragment();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_borrow_cart);
        ButterKnife.bind(this);

        initView();
        initFooter();


    }

    private void initFooter() {
        ivBorrowedList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showFragment(borrowListFragment);
                hideFragment(recentListFragment);
            }
        });

        ivRecentBooks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showFragment(recentListFragment);
                hideFragment(borrowListFragment);
            }
        });
    }

    public static Intent getIntentNewTask(Context context) {
        Intent intent = new Intent(context, BorrowCartActivity.class);
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        return intent;
    }

    private void initView() {
        getSupportActionBar().setTitle("BORROWED LIST");

        initContent();
    }

    private void initContent() {
        replaceContent(borrowListFragment, false, "BorrowListFragment");
        replaceContent(recentListFragment, false, "BorrowListFragment");
        hideFragment(recentListFragment);
        showFragment(borrowListFragment);
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
        finish();
    }

    private Fragment findPreviousFragment(String tag) {
        Fragment fragment = getSupportFragmentManager().findFragmentByTag(tag);
//        if (fragment != null) {
//            return fragment;
//        }
        return fragment;
    }

    private void showFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().show(fragment).commit();
    }

    private void hideFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().hide(fragment).commit();
    }
}
