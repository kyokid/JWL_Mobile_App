package com.auto.jarvis.libraryicognite;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.auto.jarvis.libraryicognite.activities.InsideLibraryActivity;
import com.auto.jarvis.libraryicognite.activities.LibraryActivity;
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

/**
 * Created by Nguyen.D.Hoang on 1/11/2017.
 */

public class QRCodePagerFragment extends Fragment {

    @BindView(R.id.ivQrCode)
    ImageView ivQrCode;
    private Unbinder unbinder;
    private String userId;

    public static QRCodePagerFragment newInstance(String title, String userId) {
        Bundle args = new Bundle();
        args.putString("title", title);
        args.putString("userId", userId);
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

        initView();
        return rootView;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        String title = getArguments().getString("title");
        String userId = getArguments().getString("userId");

        QRCodeWriter writer = new QRCodeWriter();
        JSONObject jsonObject = new JSONObject();
        try {
            //TODO add userId string here
            jsonObject.put("userId", userId);
            jsonObject.put("createDate", new Date(Calendar.getInstance().getTimeInMillis()));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            BitMatrix bitMatrix = writer.encode(jsonObject.toString(), BarcodeFormat.QR_CODE, 1000, 1000);
            int width = bitMatrix.getWidth();
            int height = bitMatrix.getHeight();
            Bitmap bmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    bmp.setPixel(x, y, bitMatrix.get(x, y) ? Color.BLACK : Color.WHITE);
                }
            }
            ivQrCode.setImageBitmap(bmp);

        } catch (WriterException e) {
            e.printStackTrace();
        }

        ivQrCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), LibraryActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


    private void initView() {


    }
}
