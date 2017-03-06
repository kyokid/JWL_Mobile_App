package com.auto.jarvis.libraryicognite;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.auto.jarvis.libraryicognite.Utils.Constant;
import com.auto.jarvis.libraryicognite.Utils.InternetConnectionReceiver;
import com.auto.jarvis.libraryicognite.activities.BarCodeActivity;
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

public class LoginActivity extends AppCompatActivity {

    private BroadcastReceiver broadcastReceiver;

    @BindView(R.id.btnLogin)
    Button btnLogin;

    @BindView(R.id.btnRegister)
    Button btnRegister;
    @BindView(R.id.etUsername)
    EditText etUsername;

    @BindView(R.id.etPassword)
    EditText etPassword;

    ApiInterface apiService;

    public static final String USER_TAG = "USER_TAG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initView();
        checkInternetConnection();
    }

    private void initView() {

        apiService = ApiClient.getClient().create(ApiInterface.class);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = etUsername.getText().toString().trim();
                String password = etPassword.getText().toString().trim();
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


    private void checkInternetConnection() {
        IntentFilter intentFilter = new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE");
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (InternetConnectionReceiver.checkInternet(context)) {
                    return;
                } else {
                    Toast.makeText(context, "No Internet Connection", Toast.LENGTH_SHORT).show();
                }
            }
        };
        registerReceiver(broadcastReceiver, intentFilter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(broadcastReceiver);
    }
}
