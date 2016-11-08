package com.sineom.thinkday.view;

import android.app.Fragment;
import android.app.FragmentManager;
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
    public abstract int createView();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mManager = getFragmentManager();
        View view = inflater.inflate(createView(), container, false);
        ButterKnife.bind(this, view);
        return view;
    }
}
