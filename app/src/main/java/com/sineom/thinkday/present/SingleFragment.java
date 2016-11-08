package com.sineom.thinkday.present;

import android.app.Fragment;
import android.app.FragmentManager;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
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

    public FragmentManager mManager;
    public Typeface mCustomFont;

    public abstract View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState);

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mManager = getFragmentManager();
        View view = createView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, view);
        mCustomFont = Typeface.createFromAsset(getActivity().getAssets(), "msyh.ttf");
        return view;
    }
}
