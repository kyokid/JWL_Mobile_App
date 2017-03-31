package com.auto.jarvis.libraryicognite.activities;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
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
import com.auto.jarvis.libraryicognite.Utils.RxUltils;
import com.auto.jarvis.libraryicognite.interfaces.ApiInterface;
import com.auto.jarvis.libraryicognite.models.input.User;
import com.auto.jarvis.libraryicognite.models.output.RestService;
import com.auto.jarvis.libraryicognite.rest.ApiClient;
import com.auto.jarvis.libraryicognite.stores.SaveSharedPreference;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class LoginActivity extends AppCompatActivity
        implements InternetConnectionReceiver.ConnectivityReceiverListener {


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

    Handler handler;

    ProgressDialog progressDialog;
    String message = "";


    public static final String USER_TAG = "USER_TAG";

    private boolean internetConnected = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        handler = new Handler();
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Login");
        progressDialog.setIndeterminate(false);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        initView();
        checkConnection();
//        checkInternetConnection();
    }

    private void initView() {

        apiService = ApiClient.getClient().create(ApiInterface.class);
        btnLogin.setOnClickListener(v -> {
            RxUltils.checkConnectToServer()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(isOnline -> {
                        if (!isOnline) {
                            Intent intent = new Intent(LoginActivity.this, NoInternetActivity.class);
                            startActivity(intent);
                        } else {
                            loginProcess();
                        }
                    });
        });

        btnRegister.setOnClickListener(view -> showDialogMaterial());
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


    private void loginProcess() {
        String userId = etUsername.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setMessage("Please wait...");
        User user = new User(userId, password);
        doLogin(user)
                .doOnSubscribe(() -> handler.post(() -> progressDialog.show()))
                .doOnError(new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        Toast.makeText(LoginActivity.this, "Something error", Toast.LENGTH_SHORT).show();
                    }
                })
                .subscribe(userRestService -> {
                    if (userRestService.getCode().equals("200")) {
                        User userResult = userRestService.getData();
                        SaveSharedPreference.setUsername(getApplicationContext(), userResult.getUsername());
                        SaveSharedPreference.setStatusUser(getApplicationContext(), Constant.LOGIN);
                        Intent intent = new Intent(LoginActivity.this, BarCodeActivity.class);
                        handler.post(() -> progressDialog.dismiss());
                        startActivity(intent);
                    } else {
                        progressDialog.dismiss();
                        try {
                            Thread.sleep(500);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        message = userRestService.getTextMessage();
                        MaterialDialog.Builder builder = new MaterialDialog.Builder(this)
                                .title("Login")
                                .content(message)
                                .positiveText("OK");
                        MaterialDialog dialog = builder.build();
                        dialog.show();
                    }
                });
    }

    private Observable<RestService<User>> doLogin(User user) {
        return apiService.loginUser(user)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

}
