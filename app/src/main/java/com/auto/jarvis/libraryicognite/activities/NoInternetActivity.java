package com.auto.jarvis.libraryicognite.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.auto.jarvis.libraryicognite.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NoInternetActivity extends AppCompatActivity {

    @BindView(R.id.btnRetry)
    Button btnRetry;

    Class classSource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_no_internet);
        ButterKnife.bind(this);
        String caller = getIntent().getStringExtra("FROM");
        Class source = getPreviousClass(caller);
        Log.d("CLASSNAME", source.getName());
        btnRetry.setOnClickListener(v -> {
            Intent intent = new Intent(NoInternetActivity.this, source);
            startActivity(intent);
        });
    }

    private Class getPreviousClass(String caller) {
        try {
            classSource = Class.forName(caller);
            if (classSource != null) {
                classSource.getName();
                Log.d("CLASSNAME", classSource.getName());
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return classSource;
    }
}
