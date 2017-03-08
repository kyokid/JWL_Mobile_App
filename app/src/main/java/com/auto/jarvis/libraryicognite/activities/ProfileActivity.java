package com.auto.jarvis.libraryicognite.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.auto.jarvis.libraryicognite.R;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.content.Intent.FLAG_ACTIVITY_PREVIOUS_IS_TOP;

public class ProfileActivity extends AppCompatActivity {

//    @BindView(R.id.tvUserFullName)
//    TextView tvUserFullName;
//
//    @BindView(R.id.tvUserId)
//    TextView tvUserId;
//
//    @BindView(R.id.tvPassword)
//    TextView tvPassword;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
    }

    public static Intent getIntentNewTask(Context context) {
        Intent intent = new Intent(context, ProfileActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | FLAG_ACTIVITY_PREVIOUS_IS_TOP);
        return intent;
    }
}
