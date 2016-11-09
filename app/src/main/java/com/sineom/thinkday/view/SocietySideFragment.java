package com.sineom.thinkday.view;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.sineom.thinkday.BaseActivity;
import com.sineom.thinkday.R;
import com.sineom.thinkday.adapter.SocietySideAdapter;
import com.sineom.thinkday.bean.SocietyBean;
import com.sineom.thinkday.present.GLobalData;
import com.sineom.thinkday.present.SocietyPresent;
import com.sineom.thinkday.present.UrlManager;

import java.util.ArrayList;

import butterknife.BindView;
import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

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
    private SocietyPresent mPresent;
    private LinearLayoutManager mLinearLayoutManager;
    private SocietySideAdapter mSocietySideAdapter;
    private Subscription mSocietySideFragment;
    private Observable<ArrayList<SocietyBean>> mArrayListObservable;


    @Override
    public int createView() {
        return R.layout.societyside_layout;
    }

    /**
     * v4包下系统的一个bug 必须实现
     */
    public SocietySideFragment() {
    }

    @Override
    public void initDatas() {
        mPresent = new SocietyPresent();
        mLinearLayoutManager = new LinearLayoutManager(getActivity());
        mSocietySideAdapter = new SocietySideAdapter(getActivity(), mPresent.getDatas(), new SocietySideAdapter.ItemClick() {
            @Override
            public void onItemClick(SocietyBean societyBean) {
                String url = societyBean.Url;
                SocietyItem societyItem = new SocietyItem();
                Bundle bundle = new Bundle();
                bundle.putString(GLobalData.SOCIETYSICE, societyBean.Url);
                societyItem.setArguments(bundle);
                ((BaseActivity) getActivity()).initFragment(societyItem, GLobalData.SOCIETYSICEITEM);
            }
        });
        mArrayListObservable = mPresent.getArticle(UrlManager.SOCIETYSIDE);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mSocietySideFragment = mArrayListObservable
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<ArrayList<SocietyBean>>() {
                               @Override
                               public void call(ArrayList<SocietyBean> societyBeen) {
                                   mSocietySideAdapter.setDatas(societyBeen);
                               }
                           },
                        new Action1<Throwable>() {
                            @Override
                            public void call(Throwable throwable) {
                                Log.d("SocietySideFragment", "" + throwable.getMessage());
                            }
                        }
                );
    }

    @Override
    public void initView() {
        society_rv.setLayoutManager(mLinearLayoutManager);
        society_rv.setAdapter(mSocietySideAdapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (!mSocietySideFragment.isUnsubscribed())
            mSocietySideFragment.unsubscribe();
    }
}
