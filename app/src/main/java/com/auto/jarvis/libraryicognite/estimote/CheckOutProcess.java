package com.auto.jarvis.libraryicognite.estimote;

import android.app.Application;
import android.view.ActionMode;
import android.widget.Toast;

import com.auto.jarvis.libraryicognite.interfaces.ApiInterface;
import com.auto.jarvis.libraryicognite.models.input.InitBorrow;
import com.auto.jarvis.libraryicognite.models.output.RestService;
import com.auto.jarvis.libraryicognite.rest.ApiClient;
import com.auto.jarvis.libraryicognite.stores.SaveSharedPreference;
import com.estimote.sdk.Beacon;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by HaVH on 2/6/17.
 */

public class CheckOutProcess extends Application{

    private BeaconID beaconID;
    ApiInterface apiService;

//    public void startCheckout(List<BeaconID> beaconIDs, String username) {
//        if (beaconIDs.size() > 0) {
//            beaconID = beaconIDs.get(0);
//            apiService = ApiClient.getClient().create(ApiInterface.class);
//            String macAddress = beaconID.getMacAddress();
//            String useId = username;
//            InitBorrow initBorrow = new InitBorrow(useId, macAddress);
//            double distance = beaconID.getDistance();
//            if (distance <= 1.0) {
//
//                Call<RestService<InitBorrow>> callCheckoutInit = apiService.initBorrow(initBorrow);
//                callCheckoutInit.enqueue(new Callback<RestService<InitBorrow>>() {
//                    @Override
//                    public void onResponse(Call<RestService<InitBorrow>> call, Response<RestService<InitBorrow>> response) {
//                        if (response.isSuccessful()) {
//                            if (response.body().isSucceed()) {
////                                Toast.makeText(getApplicationContext(), "Init Checkout", Toast.LENGTH_SHORT).show();
//                            }
//                        }
//                    }
//
//                    @Override
//                    public void onFailure(Call<RestService<InitBorrow>> call, Throwable t) {
//
//                    }
//                });
//            }
//
//        }
//    }
}
