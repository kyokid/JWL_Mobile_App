package com.auto.jarvis.libraryicognite;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * A simple {@link Fragment} subclass.
 */
public class NFCPagerFragment extends Fragment {

    public static NFCPagerFragment newInstance(String title) {
        Bundle args = new Bundle();
        args.putString("title", title);
        NFCPagerFragment fragment = new NFCPagerFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_nfcpager, container, false);
    }

}
