package com.sineom.thinkday.view;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.sineom.thinkday.R;
import com.sineom.thinkday.adapter.SocietySideAdapter;

import java.util.ArrayList;

import butterknife.BindView;

/**
 * @author sineom
 * @version 1.0
 * @time 2016/11/8 14:16
 * @des ${TODO}
 * @updateAuthor ${Author}
 * @updataTIme 2016/11/8
 * @updataDes ${描述更新内容}
 */

public class SocietySide extends SingleFragment {
    @BindView(R.id.society_rv)
    RecyclerView society_rv;
    private ArrayList<String> mDatas;

    @Override
    public int createView() {
        return R.layout.societyside_layout;
    }

    public SocietySide() {
    }

    @Override
    public void initDatas() {
        mDatas = new ArrayList<>();
        mDatas.add("1");
        mDatas.add("1");
        mDatas.add("1");
        mDatas.add("1");
        mDatas.add("1");
        mDatas.add("1");
    }

    @Override
    public void initRecycler() {
        society_rv.setLayoutManager(new LinearLayoutManager(getActivity()));
        society_rv.setAdapter(new SocietySideAdapter(getActivity(), mDatas));
    }
}
