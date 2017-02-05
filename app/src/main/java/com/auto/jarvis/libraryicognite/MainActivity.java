package com.auto.jarvis.libraryicognite;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.auto.jarvis.libraryicognite.activities.BarCodeActivity;
import com.auto.jarvis.libraryicognite.activities.GlobalVariable;
import com.auto.jarvis.libraryicognite.interfaces.ApiInterface;
import com.auto.jarvis.libraryicognite.models.input.User;
import com.auto.jarvis.libraryicognite.models.output.RestService;
import com.auto.jarvis.libraryicognite.rest.ApiClient;
import com.auto.jarvis.libraryicognite.stores.SaveSharedPreference;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements Callback<List<User>>{

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


        Call<List<User>> call = apiService.getAllUser();

        call.enqueue(this);


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
                                Intent intent = new Intent(MainActivity.this, BarCodeActivity.class);
                                intent.putExtra(USER_TAG, user);
                                startActivity(intent);
                            } else {
                                Toast.makeText(MainActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
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
//                Intent intent = new Intent(MainActivity.this, BarCodeActivity.class);
////                intent.putExtra(USER_TAG, user);
//                startActivity(intent);
//            }
//        });
    }

    @Override
    public void onResponse(Call<List<User>> call, Response<List<User>> response) {
        if (response.isSuccessful()) {
            List<User> result = response.body();
            Log.d("JWL", "List User: " + result.size());
        } else {
            Log.d("JWL", "Error: " + response.errorBody());
        }

    }

    @Override
    public void onFailure(Call<List<User>> call, Throwable t) {
        t.printStackTrace();
    }
}
