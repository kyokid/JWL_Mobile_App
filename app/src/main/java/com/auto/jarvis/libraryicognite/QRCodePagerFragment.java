package com.auto.jarvis.libraryicognite;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

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

    public static QRCodePagerFragment newInstance(String title) {
        Bundle args = new Bundle();
        args.putString("title", title);
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
    }

    @Override public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


    private void initView() {


    }
}
