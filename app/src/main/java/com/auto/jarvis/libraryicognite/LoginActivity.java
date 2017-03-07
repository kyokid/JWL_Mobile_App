package com.auto.jarvis.libraryicognite;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.auto.jarvis.libraryicognite.Utils.Constant;
import com.auto.jarvis.libraryicognite.Utils.InternetConnectionReceiver;
import com.auto.jarvis.libraryicognite.Utils.NetworkUtils;
import com.auto.jarvis.libraryicognite.activities.BarCodeActivity;
import com.auto.jarvis.libraryicognite.activities.GlobalVariable;
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

import static com.auto.jarvis.libraryicognite.Utils.NetworkUtils.getConnectivityStatusString;

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
        GlobalVariable.getInstance().setConnectivityListener(this);
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
