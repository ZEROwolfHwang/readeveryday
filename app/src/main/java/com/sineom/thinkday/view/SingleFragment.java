package com.sineom.thinkday.view;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sineom.thinkday.R;

import butterknife.ButterKnife;
import rx.subscriptions.CompositeSubscription;

/**
 * User: sineom(sineom@126.com)
 * Date: 2016-10-29
 * Time: 15:32
 * DESIC
 */
public abstract class SingleFragment extends Fragment {

    FragmentManager mManager;
    public Handler mHandler = new Handler();
    public CompositeSubscription mSubscription = new CompositeSubscription();

    public abstract int createView();

    public SingleFragment() {
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        initDatas();
    }


    public abstract void initDatas();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mManager = getFragmentManager();
        View view = inflater.inflate(createView(), container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
    }

    public void initView() {

    }

    public void fresh(final SwipeRefreshLayout mRefreshLayout) {
        mRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                mRefreshLayout.setRefreshing(true);
            }
        });
    }

    public void setRefreshLayout(SwipeRefreshLayout mRefreshLayout) {
        // 设定下拉圆圈的背景
        mRefreshLayout.setProgressBackgroundColor(R.color.Indigo_colorPrimary);
        //设置下拉出现小圆圈是否是缩放出现，出现的位置，最大的下拉位置
        mRefreshLayout.setProgressViewOffset(true, 50, 0);
        mRefreshLayout.setDistanceToTriggerSync(300);

        //设置下拉圆圈的大小，两个值 LARGE， DEFAULT
        mRefreshLayout.setSize(SwipeRefreshLayout.LARGE);
        // 设置下拉圆圈上的颜色，蓝色、绿色、橙色、红色
        mRefreshLayout.setColorSchemeResources(
                android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
    }

    public void closeFresh(final SwipeRefreshLayout mRefreshLayout) {
        //  通过 setEnabled(false) 禁用下拉刷新
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // 停止刷新
                mRefreshLayout.setRefreshing(false);
            }
        }, 300);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mSubscription.unsubscribe();
    }
}
