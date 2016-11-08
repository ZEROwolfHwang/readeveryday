package com.sineom.thinkday.view;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;

/**
 * User: sineom(sineom@126.com)
 * Date: 2016-10-29
 * Time: 15:32
 * DESIC
 */
public abstract class SingleFragment extends Fragment {

    FragmentManager mManager;

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
        initRecycler();
    }

    public void initRecycler() {

    }
}
