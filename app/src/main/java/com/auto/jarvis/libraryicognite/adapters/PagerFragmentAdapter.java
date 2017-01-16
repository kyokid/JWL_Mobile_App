package com.auto.jarvis.libraryicognite.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.auto.jarvis.libraryicognite.NFCPagerFragment;
import com.auto.jarvis.libraryicognite.QRCodePagerFragment;

/**
 * Created by Nguyen.D.Hoang on 1/11/2017.
 */

public class PagerFragmentAdapter extends FragmentPagerAdapter {

    public PagerFragmentAdapter(FragmentManager fm) {
        super(fm);
    }
    @Override
    public Fragment getItem(int position) {
        if(position == 0){
            return NFCPagerFragment.newInstance();
        }
        return QRCodePagerFragment.newInstance(String.valueOf(position));
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
