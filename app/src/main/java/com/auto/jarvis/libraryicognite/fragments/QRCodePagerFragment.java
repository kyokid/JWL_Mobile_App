package com.auto.jarvis.libraryicognite.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.auto.jarvis.libraryicognite.R;
import com.auto.jarvis.libraryicognite.Utils.Constant;
import com.auto.jarvis.libraryicognite.Utils.NotificationUtils;
import com.auto.jarvis.libraryicognite.activities.LibraryActivity;
import com.auto.jarvis.libraryicognite.interfaces.ApiInterface;
import com.auto.jarvis.libraryicognite.models.output.RestService;
import com.auto.jarvis.libraryicognite.rest.ApiClient;
import com.auto.jarvis.libraryicognite.stores.SaveSharedPreference;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Date;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.auto.jarvis.libraryicognite.R.id.pbLoadingQRCode;


/**
 * Created by Havh on 1/11/2017.
 */

public class QRCodePagerFragment extends Fragment {

    @BindView(R.id.ivQrCode)
    ImageView ivQrCode;
//    @BindView(pbLoadingQRCode)
//    ProgressBar pgLoading;
    @BindView(pbLoadingQRCode)
    ProgressBar pgLoading;
    private Unbinder unbinder;
    private String userId;
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    ApiInterface apiService;

    public static QRCodePagerFragment newInstance() {
        Bundle args = new Bundle();
        QRCodePagerFragment fragment = new QRCodePagerFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_pager, container, false);
        unbinder = ButterKnife.bind(this, rootView);
        userId = SaveSharedPreference.getUsername(getContext());
        new AsynCaller().execute();
        return rootView;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @Override public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
//        getContext().unregisterReceiver(mRegistrationBroadcastReceiver);
    }

    private class AsynCaller extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... params) {
            return SaveSharedPreference.getUsername(getContext());
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pgLoading.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(String userId) {
            super.onPostExecute(userId);
            pgLoading.setVisibility(View.GONE);
            helloWorld(userId);
        }
    }

    private class AsyncQrCode extends AsyncTask<Void, Void, Bitmap> {

        @Override
        protected Bitmap doInBackground(Void... params) {
            return qrCodeGene();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pgLoading.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            ivQrCode.setImageBitmap(bitmap);
            pgLoading.setVisibility(View.GONE);
        }
    }

    private void helloWorld(final String userId) {
//        Log.d("API key = ", FirebaseInstanceId.getInstance().getToken());
//        NotificationUtils.sendNewIdToServer(userId, FirebaseInstanceId.getInstance().getToken());
////            String userId = SaveSharedPreference.getUsername(BarCodeActivity.this);
////            Intent service = new Intent(BarCodeActivity.this, IntanceNotificationIDService.class);
////            this.startService(service);
//        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
//            @Override
//            public void onReceive(Context context, Intent intent) {
//                // checking for type intent filter
//                if (intent.getAction().equals(Constant.REGISTRATION_COMPLETE)) {
//                    Log.d("Registration token:" , intent.getStringExtra("token"));
//                    NotificationUtils.sendNewIdToServer(userId, intent.getStringExtra("token"));
//                } else if (intent.getAction().equals(Constant.PUSH_NOTIFICATION)) {
//                    String message = intent.getStringExtra("message");
//                    Log.d("Push notification:", message);
//                    Intent intentLibrary = new Intent(getContext(), LibraryActivity.class);
//                    intentLibrary.putExtra("IN_LIBRARY", true);
//                    startActivity(intentLibrary);
//                }
//            }
//        };
//        IntentFilter filter = new IntentFilter(Constant.PUSH_NOTIFICATION);
//        getContext().registerReceiver(mRegistrationBroadcastReceiver, filter);
        initView(userId);
    }

    private Bitmap qrCodeGene() {
        String privateKey = SaveSharedPreference.getPrivateKey(getContext());
        try {
            JSONObject qrContent = new JSONObject();
            qrContent.put("userId", userId);
            qrContent.put("key", privateKey);
            return fromStringToBitmap(qrContent.toString());
        } catch (WriterException | JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Bitmap fromStringToBitmap(String content) throws WriterException {
        QRCodeWriter writer = new QRCodeWriter();
        BitMatrix bitMatrix = writer.encode(content, BarcodeFormat.QR_CODE, 1000, 1000);
        int width = bitMatrix.getWidth();
        int height = bitMatrix.getHeight();
        Bitmap bmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                bmp.setPixel(x, y, bitMatrix.get(x, y) ? Color.BLACK : Color.WHITE);
            }
        }
        return bmp;
    }

    private void initView(String userId) {
        qrCodeProcess(userId);
    }
    private void qrCodeProcess(final String userId) {
        String lastrequest = SaveSharedPreference.getLastRequestDate(getContext());
        Date now = new Date(Calendar.getInstance().getTimeInMillis());
        Bitmap bmp = null;
        String privateKey = "";
        if (lastrequest.equals(now.toString())) {
            new AsyncQrCode().execute();
            pgLoading.setVisibility(View.GONE);
        } else {
            apiService = ApiClient.getClient().create(ApiInterface.class);
            Call<RestService<String>> result = apiService.requestPrivateKey(userId);
            result.enqueue(new Callback<RestService<String>>() {
                @Override
                public void onResponse(Call<RestService<String>> call, Response<RestService<String>> response) {
                    if (response.isSuccessful()) {
                        String result = response.body().getData();
                        String privateKey = "";
                        JSONObject resultJson;
                        String date = "";
                        Bitmap bmp;
                        try {
                            resultJson = new JSONObject(result);
                            privateKey = resultJson.getString("key");
                            date = resultJson.getString("date");
                            JSONObject qrContent = new JSONObject();
                            qrContent.put("userId", userId);
                            qrContent.put("key", privateKey);
                            bmp = fromStringToBitmap(qrContent.toString());
                            SaveSharedPreference.setLastRequestDate(getContext(), date);
                            SaveSharedPreference.setPrivateKey(getContext(), privateKey);
                            ivQrCode.setImageBitmap(bmp);
//                            pbLoadingQRCode.setVisibility(View.GONE);
                            pgLoading.setVisibility(View.GONE);
                        } catch (WriterException e) {
                            e.printStackTrace();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onFailure(Call<RestService<String>> call, Throwable t) {
                    Toast.makeText(getContext(), "Fail to call requestPrivateKey", Toast.LENGTH_LONG).show();
                    Log.d("BarCodeActivity", t.getMessage());
//                    pgLoading.setVisibility(View.GONE);
                }
            });
        }

//        pbLoadingQRCode.setVisibility(View.GONE);
        ivQrCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), LibraryActivity.class);
                intent.putExtra("IN_LIBRARY", Constant.CHECK_IN);
                startActivity(intent);
            }
        });
    }
}
