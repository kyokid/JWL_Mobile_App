package com.auto.jarvis.libraryicognite.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.auto.jarvis.libraryicognite.R;
import com.auto.jarvis.libraryicognite.Utils.Constant;
import com.auto.jarvis.libraryicognite.interfaces.ApiInterface;
import com.auto.jarvis.libraryicognite.models.output.RestService;
import com.auto.jarvis.libraryicognite.rest.ApiClient;
import com.auto.jarvis.libraryicognite.stores.SaveSharedPreference;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class HomeActivity extends AppCompatActivity {
    ApiInterface apiService;
    boolean inLibrary = false;

    @BindView(R.id.imgLoading)
    ImageView imgLoading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_activity);

        apiService = ApiClient.getClient().create(ApiInterface.class);

        controllerActivities();


//        String userId = SaveSharedPreference.getUsername(this);
//        if (userId.length() == 0 || userId.isEmpty() || userId.equals("")) {
//            Intent intent = new Intent(this, LoginActivity.class);
//            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
//            startActivity(intent);
//        } else {
//            apiService = ApiClient.getClient().create(ApiInterface.class);
//            Call<RestService<Boolean>> result = apiService.inLibrary(userId);
//            result.enqueue(new Callback<RestService<Boolean>>() {
//                @Override
//                public void onResponse(Call<RestService<Boolean>> call, Response<RestService<Boolean>> response) {
//                    if (response.isSuccessful()) {
//                        inLibrary = response.body().getData();
//                        if (inLibrary) {
//                            Intent intentLibrary = new Intent(HomeActivity.this, LibraryActivity.class);
//                            intentLibrary.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
//                            intentLibrary.putExtra("IN_LIBRARY", true);
//                            SaveSharedPreference.setStatusUser(getApplicationContext(), Constant.CHECK_IN);
//                            startActivity(intentLibrary);
//                        } else if (!inLibrary) {
//                            Intent barCodeIntent = new Intent(HomeActivity.this, BarCodeActivity.class);
//                            barCodeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
//                            startActivity(barCodeIntent);
//                        }
//                    }
//                }
//
//
//                @Override
//                public void onFailure(Call<RestService<Boolean>> call, Throwable t) {
//                    Intent barCodeIntent = new Intent(HomeActivity.this, BarCodeActivity.class);
//                    barCodeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
//                    startActivity(barCodeIntent);
//                }
//            });
//            Log.d("LIBRARY", " " + inLibrary);
////            if (!inLibrary) {
////                Intent barCodeIntent = new Intent(this, BarCodeActivity.class);
////                barCodeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
////                startActivity(barCodeIntent);
////            }
//        }
    }

    private void controllerActivities() {
        Observable.timer(3, TimeUnit.SECONDS)
                .subscribe(new Subscriber<Long>() {
                    @Override
                    public void onCompleted() {
                        if (isUnsubscribed()) {
                            unsubscribe();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onNext(Long aLong) {
                        String userId = SaveSharedPreference.getUsername(HomeActivity.this);
                        if (userId.length() == 0 || userId.isEmpty() || userId.equals("")) {
                            Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            unsubscribe();
                        } else {
                            Observable<RestService<Boolean>> statusUser = apiService.userStatus(userId);
                            statusUser.subscribeOn(Schedulers.newThread())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(new Subscriber<RestService<Boolean>>() {
                                        @Override
                                        public void onCompleted() {
                                            if (isUnsubscribed()) {
                                                unsubscribe();
                                            }
                                        }

                                        @Override
                                        public void onError(Throwable e) {
                                        }

                                        @Override
                                        public void onNext(RestService<Boolean> booleanRestService) {
                                            inLibrary = booleanRestService.getData();
                                            Intent intentControl = null;
                                            if (inLibrary) {
                                                intentControl = new Intent(HomeActivity.this, LibraryActivity.class);
                                                intentControl.putExtra("IN_LIBRARY", true);
                                                SaveSharedPreference.setStatusUser(getApplicationContext(), Constant.CHECK_IN);
                                            } else {
                                                intentControl = new Intent(HomeActivity.this, BarCodeActivity.class);
                                            }
                                            intentControl.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                            startActivity(intentControl);
                                        }
                                    });

                        }
                    }
                });
    }

//        if (userId.length() == 0 || userId.isEmpty() || userId.equals("")) {
//            Intent intent = new Intent(this, LoginActivity.class);
//            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
//            startActivity(intent);
//        } else {
//            Observable<RestService<Boolean>> statusUser = apiService.userStatus(userId);
//            statusUser.subscribeOn(Schedulers.newThread())
//                    .observeOn(AndroidSchedulers.mainThread())
//                    .subscribe(new Subscriber<RestService<Boolean>>() {
//                        @Override
//                        public void onCompleted() {
//
//                        }
//
//                        @Override
//                        public void onError(Throwable e) {
//
//                        }
//
//                        @Override
//                        public void onNext(RestService<Boolean> booleanRestService) {
//                            inLibrary = booleanRestService.getData();
//                            if (inLibrary) {
//                                Intent intentLibrary = new Intent(HomeActivity.this, LibraryActivity.class);
//                                intentLibrary.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
//                                intentLibrary.putExtra("IN_LIBRARY", true);
//                                SaveSharedPreference.setStatusUser(getApplicationContext(), Constant.CHECK_IN);
//                                startActivity(intentLibrary);
//                                unsubscribe();
//                            } else {
//                                Intent barCodeIntent = new Intent(HomeActivity.this, BarCodeActivity.class);
//                                barCodeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
//                                startActivity(barCodeIntent);
//                            }
//                        }
//                    });
//
//        }
//    }
}
