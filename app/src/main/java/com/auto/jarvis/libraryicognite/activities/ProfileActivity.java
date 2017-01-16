package com.auto.jarvis.libraryicognite.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.auto.jarvis.libraryicognite.R;

import static android.content.Intent.FLAG_ACTIVITY_PREVIOUS_IS_TOP;

public class ProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
    }

    public static Intent getIntentNewTask(Context context) {
        Intent intent = new Intent(context, ProfileActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | FLAG_ACTIVITY_PREVIOUS_IS_TOP);
        return intent;
    }
}
