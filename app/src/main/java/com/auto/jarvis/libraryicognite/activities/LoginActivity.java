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
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
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

public class LoginActivity extends AppCompatActivity {


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

    RadioGroup radioGroup;
    EditText editText;
    RadioButton heroku, local;

    Handler handler;

    ProgressDialog progressDialog;
    String message = "";


    public static final String USER_TAG = "USER_TAG";


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
    }

    private void initView() {

        apiService = ApiClient.getClient().create(ApiInterface.class);
        btnLogin.setText(R.string.login);
        btnLogin.setOnClickListener(v -> {
            loginClick();
        });

        etPassword.setOnEditorActionListener((TextView.OnEditorActionListener) (v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                loginClick();
            }
            return false;
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


    private void loginProcess() {
        String userId = etUsername.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setMessage("Please wait...");
        User user = new User(userId, password);
        doLogin(user)
                .doOnSubscribe(() -> handler.post(() -> progressDialog.show()))
                .doOnError(throwable -> Toast.makeText(LoginActivity.this, "Something error", Toast.LENGTH_SHORT).show())
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

    private void loginClick() {
        RxUltils.checkConnectToServer()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(isOnline -> {
                    if (!isOnline) {
                        Intent intent = new Intent(LoginActivity.this, NoInternetActivity.class);
                        intent.putExtra("FROM", this.getClass().getCanonicalName());
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    } else {
                        loginProcess();
                    }
                });
    }

}
