package com.auto.jarvis.libraryicognite;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.auto.jarvis.libraryicognite.models.output.InformationBookBorrowed;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BorrowCartActivity extends AppCompatActivity {


    @BindView(R.id.ivBorrowList)
    ImageView ivBorrowList;
    @BindView(R.id.ivHistory)
    ImageView ivHistory;
    @BindView(R.id.ivOther)
    ImageView ivOther;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_borrow_cart);
        ButterKnife.bind(this);

        initView();

    }

    public static Intent getIntentNewTask(Context context) {
        Intent intent = new Intent(context, BorrowCartActivity.class);
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        return intent;
    }

    private void initView() {
        getSupportActionBar().setTitle("BORROWED LIST");

        initContent();
        initFooter();
    }

    private void initFooter() {
        ivBorrowList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                replaceContent(new BorrowListFragment());
            }
        });
        ivHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                replaceContent(new HistoryFragment());
            }
        });
        ivOther.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                replaceContent(new HistoryFragment());
            }
        });
    }

    private void initContent() {
        replaceContent(new BorrowListFragment());
    }

    private void replaceContent(Fragment fragment) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.content, fragment).addToBackStack(null);
        ft.commit();
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
