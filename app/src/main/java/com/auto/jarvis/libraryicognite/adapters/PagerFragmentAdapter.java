package com.auto.jarvis.libraryicognite.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.auto.jarvis.libraryicognite.fragments.NFCPagerFragment;
import com.auto.jarvis.libraryicognite.fragments.QRCodePagerFragment;

/**
 * Created by Havh on 1/11/2017.
 */

public class PagerFragmentAdapter extends FragmentPagerAdapter {
    private String userId;

    public PagerFragmentAdapter(FragmentManager fm, String userId) {
        super(fm);
        this.userId = userId;
    }
    @Override
    public Fragment getItem(int position) {
        if(position == 0){
            return NFCPagerFragment.newInstance();
        }
        return QRCodePagerFragment.newInstance();
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return String.valueOf(position);
    }
}
