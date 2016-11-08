package com.sineom.thinkday.view;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.sineom.thinkday.R;
import com.sineom.thinkday.adapter.SocietySideAdapter;
import com.sineom.thinkday.bean.SocietyBean;
import com.sineom.thinkday.present.GLobalData;
import com.sineom.thinkday.present.SocietyPresent;

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

public class SocietySideFragment extends SingleFragment {
    @BindView(R.id.society_rv)
    RecyclerView society_rv;
    private ArrayList<SocietyBean> mDatas;
    private SocietyPresent mPresent;

    @Override
    public int createView() {
        return R.layout.societyside_layout;
    }

    public SocietySideFragment() {
    }

    @Override
    public void initDatas() {
        mPresent = new SocietyPresent();
        mPresent.getDatasFormHtml(GLobalData.SOCIETYSICE);
        mDatas = mPresent.getDatas();
    }

    @Override
    public void initRecycler() {
        society_rv.setLayoutManager(new LinearLayoutManager(getActivity()));
        society_rv.setAdapter(new SocietySideAdapter(getActivity(), mDatas));
    }
}
