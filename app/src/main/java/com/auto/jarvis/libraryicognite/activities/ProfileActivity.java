package com.auto.jarvis.libraryicognite.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.widget.TextView;

import com.auto.jarvis.libraryicognite.R;
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

    private String fullName, email, address, placeOfWork, birthday, phoneNo;



    @BindView(R.id.toolbar)
    Toolbar toolbar;

    ApiInterface apiInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ButterKnife.bind(this);
        String userId = SaveSharedPreference.getUsername(ProfileActivity.this);
        initView(userId);

        getDataUser(userId);

    }

    private void getDataUser(String userId) {
        Call<RestService<Data>> getProfile = apiInterface.getProfile(userId);
        getProfile.enqueue(new Callback<RestService<Data>>() {
            @Override
            public void onResponse(Call<RestService<Data>> call, Response<RestService<Data>> response) {
                Profile profile = response.body().getData().getProfile();
                fullName = profile.getFullname();
                email = profile.getEmail();
                address = profile.getAddress();
                placeOfWork = profile.getPlaceOfWork();
                birthday = profile.getDateOfBirth();
                phoneNo = profile.getPhoneNo();

                if (!TextUtils.isEmpty(fullName) && !TextUtils.isEmpty(email) &&
                        !TextUtils.isEmpty(address) && !TextUtils.isEmpty(placeOfWork) &&
                        !TextUtils.isEmpty(birthday) && !TextUtils.isEmpty(phoneNo)) {
                    tvFullName.setText(fullName);
                    tvEmail.setText(email);
                    tvAddress.setText(address);
//                    tvPlaceOfWork.setText(placeOfWork);
//                    tvBirthday.setText(birthday);
                    tvPhoneNo.setText(phoneNo);
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
