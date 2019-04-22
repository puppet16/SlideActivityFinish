/*
 * Tencent is pleased to support the open source community by making QMUI_Android available.
 *
 * Copyright (C) 2017-2018 THL A29 Limited, a Tencent company. All rights reserved.
 *
 * Licensed under the MIT License (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 *
 * http://opensource.org/licenses/MIT
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is
 * distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.luck.slideleftfinish;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.luck.slideleftfinish.weight.QMUIStatusBarHelper;
import com.luck.slideleftfinish.weight.QMUISwipeBackActivityManager;
import com.luck.slideleftfinish.weight.SwipeBackLayout;
import com.luck.slideleftfinish.weight.SwipeBackgroundView;
import com.luck.slideleftfinish.weight.SlideBackUtils;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import static com.luck.slideleftfinish.weight.SwipeBackLayout.EDGE_LEFT;

public class QMUIActivity extends AppCompatActivity {
    private final String TAG = getClass().getSimpleName();
    private SwipeBackLayout.ListenerRemover mListenerRemover;
    private SwipeBackgroundView mSwipeBackgroundView;
    private boolean mIsInSwipeBack = false;
    protected boolean mIsCanDragBack = true;

    private SwipeBackLayout.SwipeListener mSwipeListener = new SwipeBackLayout.SwipeListener() {

        @Override
        public void onScrollStateChange(int state, float scrollPercent) {
            Log.i(TAG, "SwipeListener:onScrollStateChange: state = " + state + " ;scrollPercent = " + scrollPercent);
            mIsInSwipeBack = state != SwipeBackLayout.STATE_IDLE;
            if (state == SwipeBackLayout.STATE_IDLE) {
                if (mSwipeBackgroundView != null) {
                    if (scrollPercent <= 0.0F) {
                        mSwipeBackgroundView.unBind();
                        mSwipeBackgroundView = null;
                    } else if (scrollPercent >= 1.0F) {
                        // unBind mSwipeBackgroundView until onDestroy
                        finish();
                        int exitAnim = mSwipeBackgroundView.hasChildWindow() ?
                                R.anim.swipe_back_exit_still : R.anim.swipe_back_exit;
                        overridePendingTransition(R.anim.swipe_back_enter, exitAnim);
                    }
                }
            }
        }

        @Override
        public void onScroll(int edgeFlag, float scrollPercent) {
            if (mSwipeBackgroundView != null) {
                scrollPercent = Math.max(0f, Math.min(1f, scrollPercent));
                int targetOffset = (int) (Math.abs(backViewInitOffset()) * (1 - scrollPercent));
                SwipeBackLayout.offsetInScroll(mSwipeBackgroundView, edgeFlag, targetOffset);
            }
        }

        @Override
        public void onEdgeTouch(int edgeFlag) {
            Log.i(TAG, "SwipeListener:onEdgeTouch: edgeFlag = " + edgeFlag);
            onDragStart();
            ViewGroup decorView = (ViewGroup) getWindow().getDecorView();
            if (decorView != null) {
                Activity prevActivity = QMUISwipeBackActivityManager.getInstance()
                        .getPenultimateActivity(QMUIActivity.this);
                if (decorView.getChildAt(0) instanceof SwipeBackgroundView) {
                    mSwipeBackgroundView = (SwipeBackgroundView) decorView.getChildAt(0);
                } else {
                    mSwipeBackgroundView = new SwipeBackgroundView(QMUIActivity.this);
                    decorView.addView(mSwipeBackgroundView, 0, new FrameLayout.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                }
                mSwipeBackgroundView.bind(prevActivity, QMUIActivity.this, restoreSubWindowWhenDragBack());
                SwipeBackLayout.offsetInEdgeTouch(mSwipeBackgroundView, edgeFlag,
                        Math.abs(backViewInitOffset()));
            }
        }

        @Override
        public void onScrollOverThreshold() {
            Log.i(TAG, "SwipeListener:onEdgeTouch:onScrollOverThreshold");
        }
    };
    private SwipeBackLayout.Callback mSwipeCallback = new SwipeBackLayout.Callback() {
        @Override
        public boolean canSwipeBack() {
            return QMUISwipeBackActivityManager.getInstance().canSwipeBack() && canDragBack();
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        QMUIStatusBarHelper.translucent(this);
    }

    @Override
    public void setContentView(View view) {
        super.setContentView(newSwipeBackLayout(view));
    }

    @Override
    public void setContentView(int layoutResID) {
        SwipeBackLayout swipeBackLayout = SwipeBackLayout.wrap(this,
                layoutResID, dragBackEdge(), mSwipeCallback);
        if (translucentFull()) {
            swipeBackLayout.getContentView().setFitsSystemWindows(false);
        } else {
            swipeBackLayout.getContentView().setFitsSystemWindows(true);
        }
        mListenerRemover = swipeBackLayout.addSwipeListener(mSwipeListener);
        super.setContentView(swipeBackLayout);
    }

    @Override
    public void setContentView(View view, ViewGroup.LayoutParams params) {
        super.setContentView(newSwipeBackLayout(view), params);
    }

    private View newSwipeBackLayout(View view) {
        if (translucentFull()) {
            view.setFitsSystemWindows(false);
        } else {
            view.setFitsSystemWindows(true);
        }
        final SwipeBackLayout swipeBackLayout = SwipeBackLayout.wrap(view, dragBackEdge(), mSwipeCallback);
        mListenerRemover = swipeBackLayout.addSwipeListener(mSwipeListener);
        return swipeBackLayout;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mListenerRemover != null) {
            mListenerRemover.remove();
        }
        if (mSwipeBackgroundView != null) {
            mSwipeBackgroundView.unBind();
            mSwipeBackgroundView = null;
        }
    }

    /**
     * final this method, if need override this method, use doOnBackPressed as an alternative
     */
    @Override
    public final void onBackPressed() {
        if (!mIsInSwipeBack) {
            doOnBackPressed();
        }
    }

    protected void doOnBackPressed() {
        super.onBackPressed();
    }

    public boolean isInSwipeBack() {
        return mIsInSwipeBack;
    }

    /**
     * disable or enable drag back
     *
     * @return
     */
    protected boolean canDragBack() {
        return mIsCanDragBack;
    }
    protected void setCanDragBack(boolean isCan) {
        Log.d(TAG,"设置能否滑动关闭："+isCan);
        mIsCanDragBack = isCan;
    }
    /**
     * if enable drag back,
     *
     * @return
     */
    protected int backViewInitOffset() {
        return 0;
    }

    /**
     * called when drag back started.
     */
    protected void onDragStart() {

    }


    protected int dragBackEdge() {
        return EDGE_LEFT;
    }

    /**
     * Immersive processing
     *
     * @return if true, the area under status bar belongs to content; otherwise it belongs to padding
     */
    protected boolean translucentFull() {
        return false;
    }

    /**
     * restore sub window(e.g dialog) when drag back to previous activity
     *
     * @return
     */
    protected boolean restoreSubWindowWhenDragBack() {
        return true;
    }

    private static int NO_REQUESTED_ORIENTATION_SET = -100;
    private boolean mConvertToTranslucentCauseOrientationChanged = false;
    private int mPendingRequestedOrientation = NO_REQUESTED_ORIENTATION_SET;

    public void convertToTranslucentCauseOrientationChanged() {
        SlideBackUtils.convertActivityToTranslucent(this);
        mConvertToTranslucentCauseOrientationChanged = true;
    }

    @Override
    public void setRequestedOrientation(int requestedOrientation) {
        if (mConvertToTranslucentCauseOrientationChanged && (Build.VERSION.SDK_INT == Build.VERSION_CODES.O
                || Build.VERSION.SDK_INT == Build.VERSION_CODES.O_MR1)) {
            mPendingRequestedOrientation = requestedOrientation;
        } else {
            super.setRequestedOrientation(requestedOrientation);
        }

    }

    @SuppressLint("WrongConstant")
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (mConvertToTranslucentCauseOrientationChanged) {
            mConvertToTranslucentCauseOrientationChanged = false;
            SlideBackUtils.convertActivityFromTranslucent(this);
            if (mPendingRequestedOrientation != NO_REQUESTED_ORIENTATION_SET) {
                super.setRequestedOrientation(mPendingRequestedOrientation);
                mPendingRequestedOrientation = NO_REQUESTED_ORIENTATION_SET;
            }
        }
    }
}
