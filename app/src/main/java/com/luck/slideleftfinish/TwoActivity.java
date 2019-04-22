package com.luck.slideleftfinish;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;
import com.gyf.immersionbar.ImmersionBar;

import java.util.ArrayList;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

public class TwoActivity extends QMUIActivity {

    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private String[] mTitle = new String[]{"页码1", "页码2", "页码3"};
    private ArrayList<Fragment> mFragments;
    private SimpleFragmentPagerAdapter mVpAdapter;
    private final TwoActivity mActivity = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_two);
        ImmersionBar.with(this).fullScreen(false).statusBarDarkFont(true, 0.2f).init();

        mTabLayout = findViewById(R.id.tab_layout);
        mViewPager = findViewById(R.id.vp);
        setViewPager();
    }


    public void startNextActivity() {
        startActivity(new Intent(this, ThreeActivity.class));
    }

    private void setViewPager() {
        mFragments = new ArrayList<>();
        for (String aMTitle : mTitle) {
            TestFragment fragment = TestFragment.newInstance(aMTitle);
            mFragments.add(fragment);
        }
        mVpAdapter = new SimpleFragmentPagerAdapter(this, getSupportFragmentManager(), mFragments, mTitle);
        mViewPager.setAdapter(mVpAdapter);//给ViewPager设置适配器
        mTabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);//设置tab模式，当前为系统默认模式
        mTabLayout.setupWithViewPager(mViewPager);//将TabLayout和ViewPager关联起来。
        mTabLayout.removeAllTabs();
        mVpAdapter.notifyDataSetChanged();
        mViewPager.setOffscreenPageLimit(2);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 0) {
                    mActivity.setCanDragBack(true);
                } else {
                    mActivity.setCanDragBack(false);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }
}
