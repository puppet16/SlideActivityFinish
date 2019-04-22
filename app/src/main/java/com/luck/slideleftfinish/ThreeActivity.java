package com.luck.slideleftfinish;

import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;
import com.gyf.immersionbar.ImmersionBar;
import com.luck.slideleftfinish.twosolutionweight.GestureViewGroup;

import java.util.ArrayList;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager.widget.ViewPager;

public class ThreeActivity extends FragmentActivity {

    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private String[] mTitle = new String[]{"页码4", "页码5", "页码6"};
    private ArrayList<Fragment> mFragments;
    private SimpleFragmentPagerAdapter mVpAdapter;
    private final ThreeActivity mActivity = this;
    private GestureViewGroup mRoot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_three);
        ImmersionBar.with(this).fullScreen(false).statusBarDarkFont(true, 0.2f).init();
        mTabLayout = findViewById(R.id.tab_layout);
        mViewPager = findViewById(R.id.vp);
        mRoot = findViewById(R.id.root);
        mRoot.setGestureViewGroupGoneListener(new GestureViewGroup.GestureViewGroupGoneListener() {
            @Override
            public void onFinish() {
                mActivity.finish();
            }
        });
        setViewPager();
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
                if(position == 0) {
                    mRoot.setCanSlideFinish(true);
                } else {
                    mRoot.setCanSlideFinish(false);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }
}
