package com.auto.jarvis.libraryicognite.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.auto.jarvis.libraryicognite.R;
import com.auto.jarvis.libraryicognite.Utils.RxUltils;
import com.auto.jarvis.libraryicognite.interfaces.ApiInterface;
import com.auto.jarvis.libraryicognite.models.output.Data;
import com.auto.jarvis.libraryicognite.models.output.Profile;
import com.auto.jarvis.libraryicognite.models.output.RestService;
import com.auto.jarvis.libraryicognite.models.output.UserProfile;
import com.auto.jarvis.libraryicognite.models.output.UserProfileInfo;
import com.auto.jarvis.libraryicognite.rest.ApiClient;
import com.auto.jarvis.libraryicognite.stores.SaveSharedPreference;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static android.content.Intent.FLAG_ACTIVITY_PREVIOUS_IS_TOP;

public class ProfileActivity extends AppCompatActivity {

    @BindView(R.id.tvFullName)
    TextView tvFullName;

    @BindView(R.id.tvEmail)
    TextView tvEmail;

    @BindView(R.id.tvAddress)
    TextView tvAddress;

//    @BindView(R.id.tvPlaceOfWork)
//    TextView tvPlaceOfWork;
//
//    @BindView(R.id.tvBirthday)
//    TextView tvBirthday;

    @BindView(R.id.tvPhoneNo)
    TextView tvPhoneNo;

    @BindView(R.id.tvUsableBalance)
    TextView tvUsableBalance;

    @BindView(R.id.tvTotalBalance)
    TextView tvTotalBalance;

    private String fullName, email, address, placeOfWork, birthday, phoneNo;
    private int totalBalance, usableBalance;



    @BindView(R.id.toolbar)
    Toolbar toolbar;

    ApiInterface apiInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ButterKnife.bind(this);
        String userId = SaveSharedPreference.getUsername(ProfileActivity.this);
        RxUltils.checkConnectToServer()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(isOnline -> {
                    if (!isOnline) {
                        Intent intent = new Intent(ProfileActivity.this, NoInternetActivity.class);
                        intent.putExtra("FROM", this.getClass().getCanonicalName());
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();
                    } else {
                        initView(userId);
                        getDataUser(userId);
                    }
                });
    }

    private void getDataUser(String userId) {
        Call<RestService<Data>> getProfile = apiInterface.getProfile(userId);
        getProfile.enqueue(new Callback<RestService<Data>>() {
            @Override
            public void onResponse(Call<RestService<Data>> call, Response<RestService<Data>> response) {
                if (response.body().getData().getProfile() != null){
                    Profile profile = response.body().getData().getProfile();
                    fullName = profile.getFullname();
                    email = profile.getEmail();
                    address = profile.getAddress();
                    placeOfWork = profile.getPlaceOfWork();
                    birthday = profile.getDateOfBirth();
                    phoneNo = profile.getPhoneNo();
                    usableBalance = response.body().getData().getUsableBalance();
                    totalBalance = response.body().getData().getTotalBalance();
                    if (!TextUtils.isEmpty(fullName) && !TextUtils.isEmpty(email) &&
                            !TextUtils.isEmpty(address) && !TextUtils.isEmpty(placeOfWork) &&
                            !TextUtils.isEmpty(birthday) && !TextUtils.isEmpty(phoneNo)) {
                        tvFullName.setText(fullName);
                        tvEmail.setText(email);
                        tvAddress.setText(address);
//                    tvPlaceOfWork.setText(placeOfWork);
//                    tvBirthday.setText(birthday);
                        tvPhoneNo.setText(phoneNo);
                        tvUsableBalance.setText(usableBalance + "");
                        tvTotalBalance.setText(totalBalance + "");
                    }
                }else {
                    Toast.makeText(getBaseContext(), "Vui lòng thêm profile vào DB", Toast.LENGTH_SHORT).show();
                }


            }

            @Override
            public void onFailure(Call<RestService<Data>> call, Throwable t) {

            }
        });


    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void initView(String userId) {
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(userId);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        apiInterface = ApiClient.getClient().create(ApiInterface.class);
    }

    public static Intent getIntentNewTask(Context context) {
        Intent intent = new Intent(context, ProfileActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | FLAG_ACTIVITY_PREVIOUS_IS_TOP);
        return intent;
    }



}
