package com.auto.jarvis.libraryicognite;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.skyfishjy.library.RippleBackground;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


/**
 * A simple {@link Fragment} subclass.
 */
public class NFCPagerFragment extends Fragment {
    private Unbinder unbinder;

    @BindView(R.id.ivButton)
    ImageView ivButton;
    @BindView(R.id.ripple)
    RippleBackground ripple;

    public static NFCPagerFragment newInstance() {
        NFCPagerFragment fragment = new NFCPagerFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_nfcpager, container, false);
        unbinder = ButterKnife.bind(this, rootView);
        initView();
        return rootView;
    }

    private void initView() {
        ivButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!ripple.isRippleAnimationRunning()){
                    ripple.startRippleAnimation();
                }else {
                    ripple.stopRippleAnimation();
                }
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
