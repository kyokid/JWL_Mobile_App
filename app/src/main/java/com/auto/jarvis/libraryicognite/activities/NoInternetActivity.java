package com.auto.jarvis.libraryicognite.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.afollestad.materialdialogs.MaterialDialog;
import com.auto.jarvis.libraryicognite.R;
import com.auto.jarvis.libraryicognite.interfaces.ApiInterface;
import com.auto.jarvis.libraryicognite.rest.ApiClient;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NoInternetActivity extends AppCompatActivity {

    @BindView(R.id.btnRetry)
    Button btnRetry;

    @BindView(R.id.btnURL)
    Button btnUrl;

    Class classSource;
    RadioGroup radioGroup;
    EditText editText;
    RadioButton heroku, local;

    ApiInterface apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_no_internet);
        ButterKnife.bind(this);
        String caller = getIntent().getStringExtra("FROM");
        apiService = ApiClient.getClient().create(ApiInterface.class);
        Class source = getPreviousClass(caller);
        Log.d("CLASSNAME", source.getName());
        btnRetry.setOnClickListener(v -> {
            Intent intent = new Intent(NoInternetActivity.this, source);
            startActivity(intent);
        });
        btnUrl.setOnClickListener(v -> showDialogMaterial());
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


    private void showDialogMaterial() {
        MaterialDialog.Builder builder = new MaterialDialog.Builder(this)
                .title("set URL")
                .customView(R.layout.dialog_customview, true)
                .positiveText("OK")
                .onPositive((dialog, which) -> {
                    String urlServer = "";
                    int selectedId = radioGroup.getCheckedRadioButtonId();
                    if (selectedId == heroku.getId()) {
                        urlServer = "jwl-api-v0.herokuapp.com";

                    } else if (selectedId == local.getId()) {
                        urlServer = editText.getText().toString();
                    }
                    ApiClient.retrofit = null;
                    ApiClient.BASE_URL = "http://" + urlServer + "/";
                    apiService = ApiClient.getClient().create(ApiInterface.class);
                })
                .negativeText("Cancel");

        MaterialDialog dialog = builder.build();
        radioGroup = (RadioGroup) dialog.findViewById(R.id.rgUrl);
        editText = (EditText) dialog.findViewById(R.id.etCustomUrl);
        heroku = (RadioButton) dialog.findViewById(R.id.rdHeroku);
        local = (RadioButton) dialog.findViewById(R.id.rdCustomUrl);
        radioGroup.setOnCheckedChangeListener((radioGroup1, i) -> {
            if (i == R.id.rdHeroku) {
                editText.setVisibility(View.GONE);
                editText.setEnabled(false);
            } else if (i == R.id.rdCustomUrl) {
                editText.setVisibility(View.VISIBLE);
                editText.setEnabled(true);
                editText.requestFocus();
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                inputMethodManager.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);
            }
        });


        dialog.show();

    }
}
