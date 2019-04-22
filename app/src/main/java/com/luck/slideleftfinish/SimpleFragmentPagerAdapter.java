package com.luck.slideleftfinish;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import java.util.List;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;


public class SimpleFragmentPagerAdapter extends FragmentStatePagerAdapter {

    private Context mContext;
    private List<Fragment> mFragmentList;
    private String[] mTitleList;

    public SimpleFragmentPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    public SimpleFragmentPagerAdapter(Context context,
                                      FragmentManager fm,
                                      List<Fragment> fragmentList,
                                      String[] mTitleList) {
        super(fm);
        this.mContext = context;
        this.mFragmentList = fragmentList;
        this.mTitleList = mTitleList;
    }

    @Override
    public Fragment getItem(int position) {
        return mFragmentList.get(position);
    }

    @Override
    public int getCount() {
        return mFragmentList.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTitleList[position];
    }

    private int mChildCount = 0;

    @Override
    public void notifyDataSetChanged() {
        mChildCount = getCount();
        super.notifyDataSetChanged();
    }

    @Override
    public int getItemPosition(Object object) {
        if (mChildCount > 0) {
            mChildCount--;
            return POSITION_NONE;
        }
        return super.getItemPosition(object);
    }
}
