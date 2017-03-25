package com.auto.jarvis.libraryicognite.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.auto.jarvis.libraryicognite.MyApplication;
import com.auto.jarvis.libraryicognite.R;
import com.auto.jarvis.libraryicognite.Utils.Constant;
import com.auto.jarvis.libraryicognite.Utils.InternetConnectionReceiver;
import com.auto.jarvis.libraryicognite.Utils.NetworkUtils;
import com.auto.jarvis.libraryicognite.interfaces.ApiInterface;
import com.auto.jarvis.libraryicognite.models.input.User;
import com.auto.jarvis.libraryicognite.models.output.RestService;
import com.auto.jarvis.libraryicognite.rest.ApiClient;
import com.auto.jarvis.libraryicognite.stores.SaveSharedPreference;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity implements InternetConnectionReceiver.ConnectivityReceiverListener {


    @BindView(R.id.btnLogin)
    Button btnLogin;

    @BindView(R.id.btnRegister)
    Button btnRegister;
    @BindView(R.id.etUsername)
    EditText etUsername;

    @BindView(R.id.etPassword)
    EditText etPassword;

    @BindView(R.id.rlLayout)
    RelativeLayout relativeLayout;

    ApiInterface apiService;
    Snackbar snackbar;

    RadioGroup radioGroup;
    EditText editText;
    RadioButton heroku, local;


    public static final String USER_TAG = "USER_TAG";

    private boolean internetConnected = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initView();
        checkConnection();
//        checkInternetConnection();
    }

    private void initView() {

        apiService = ApiClient.getClient().create(ApiInterface.class);
        btnLogin.setOnClickListener(view -> {
            String username = etUsername.getText().toString().trim();
            String password = etPassword.getText().toString().trim();
            Log.d("URL", ApiClient.BASE_URL);
            User user = new User(username, password);
            Call<RestService<User>> callLogin = apiService.login(user);

            callLogin.enqueue(new Callback<RestService<User>>() {
                @Override
                public void onResponse(Call<RestService<User>> call, Response<RestService<User>> response) {
                    if (response.isSuccessful()) {
                        if (response.body().isSucceed()) {
                            User user = response.body().getData();
                            SaveSharedPreference.setUsername(getApplicationContext(), user.getUsername());
                            SaveSharedPreference.setStatusUser(getApplicationContext(), Constant.LOGIN);
                            Intent intent = new Intent(LoginActivity.this, BarCodeActivity.class);
                            intent.putExtra(USER_TAG, user);
                            startActivity(intent);
                        } else {
                            Toast.makeText(LoginActivity.this, response.body().getTextMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                @Override
                public void onFailure(Call<RestService<User>> call, Throwable t) {
                    t.printStackTrace();
                }
            });


        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogMaterial();

            }
        });
//        btnLogin.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(LoginActivity.this, BarCodeActivity.class);
//                intent.putExtra(USER_TAG, "SE61476");
//                startActivity(intent);
//            }
//        });
    }

    private void showDialogMaterial() {
        MaterialDialog.Builder builder = new MaterialDialog.Builder(this)
                .title("set URL")
                .customView(R.layout.dialog_customview, true)
                .positiveText("OK")
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        String urlServer = "";
                        int selectedId = radioGroup.getCheckedRadioButtonId();
                        if (selectedId == heroku.getId()) {
                            urlServer = "jwl-api-v0.herokuapp.com";

                        } else if (selectedId == local.getId()) {
                            urlServer = editText.getText().toString();
                        }
                        ApiClient.retrofit = null;
//                        ApiClient.changeApiBaseUrl(urlServer);
                        ApiClient.BASE_URL = "http://" + urlServer + "/";
                        apiService = ApiClient.getClient().create(ApiInterface.class);
//                        Intent i = getBaseContext().getPackageManager()
//                                .getLaunchIntentForPackage( getBaseContext().getPackageName() );
//                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                        startActivity(i);
                    }
                })
                .negativeText("Cancel");

        MaterialDialog dialog = builder.build();
        radioGroup = (RadioGroup) dialog.findViewById(R.id.rgUrl);
        editText = (EditText) dialog.findViewById(R.id.etCustomUrl);
        heroku = (RadioButton) dialog.findViewById(R.id.rdHeroku);
        local = (RadioButton) dialog.findViewById(R.id.rdCustomUrl);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
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
            }
        });


        dialog.show();

    }


//    private void checkInternetConnection() {
//        IntentFilter intentFilter = new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE");
//        broadcastReceiver = new BroadcastReceiver() {
//            @Override
//            public void onReceive(Context context, Intent intent) {
//                Snackbar snackbar = Snackbar.make(relativeLayout, R.string.no_internet, Snackbar.LENGTH_INDEFINITE);
//                if (InternetConnectionReceiver.checkInternet(context)) {
//                    snackbar.dismiss();
//                } else {
//                    snackbar.show();
////                    Toast.makeText(context, "No Internet Connection", Toast.LENGTH_SHORT).show();
//                }
//            }
//        };
//        registerReceiver(broadcastReceiver, intentFilter);
//    }

//    @Override
//    protected void onDestroy() {
//        if (broadcastReceiver != null) {
//            unregisterReceiver(broadcastReceiver);
//            broadcastReceiver = null;
//        }
//        super.onDestroy();
//    }

    @Override
    protected void onResume() {
        super.onResume();
        MyApplication.getInstance().setConnectivityListener(this);
    }

    private void checkConnection() {
        boolean isConnected = NetworkUtils.checkConnection(getApplicationContext());
        showSnack(isConnected);
    }

    private void showSnack(boolean isConnected) {
        String message;
        int color;
        if (isConnected) {
            message = "Good! Connected to Internet";
            color = Color.WHITE;
        } else {
            message = "Sorry! Not connected to internet";
            color = Color.RED;
        }

        snackbar = Snackbar
                .make(findViewById(R.id.fab), message, Snackbar.LENGTH_LONG);

        View sbView = snackbar.getView();
        TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(color);
        snackbar.show();
    }


    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        showSnack(isConnected);
    }
}
