package com.auto.jarvis.libraryicognite.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.auto.jarvis.libraryicognite.LoginActivity;
import com.auto.jarvis.libraryicognite.stores.SaveSharedPreference;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (SaveSharedPreference.getUsername(this).length() == 0) {
            Intent intent = new Intent(this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        } else {
            Intent barCodeIntent = new Intent(this, BarCodeActivity.class);
            barCodeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(barCodeIntent);
        }
    }
}
