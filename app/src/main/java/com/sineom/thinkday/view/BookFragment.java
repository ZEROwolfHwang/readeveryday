package com.sineom.thinkday.view;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.sineom.thinkday.BaseActivity;
import com.sineom.thinkday.R;
import com.sineom.thinkday.adapter.SocietySideAdapter;
import com.sineom.thinkday.bean.SocietyBean;
import com.sineom.thinkday.present.BookPresent;
import com.sineom.thinkday.present.GLobalData;
import com.sineom.thinkday.present.UrlManager;
import com.sineom.thinkday.utils.RxHolder;

import java.util.ArrayList;

import butterknife.BindView;
import rx.Observable;
import rx.Subscription;
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

public class BookFragment extends SingleFragment {
    @BindView(R.id.society_rv)
    RecyclerView society_rv;
    @BindView(R.id.society_swipeRefresh)
    SwipeRefreshLayout mRefreshLayout;
    private BookPresent mPresent;
    private LinearLayoutManager mLinearLayoutManager;
    private SocietySideAdapter mSocietySideAdapter;
    private Subscription mSocietySideFragment;
    private Observable<ArrayList<SocietyBean>> mArrayListObservable;
    private int page = 1;
    private int mLastPosition;

    @Override
    public int createView() {
        return R.layout.societyside_layout;
    }

    /**
     * v4包下系统的一个bug 必须实现
     */
    public BookFragment() {
    }


    @Override
    public void initDatas() {
        mPresent = new BookPresent();
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
        mArrayListObservable = mPresent.getArticle(UrlManager.BOOK + page);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        fresh(mRefreshLayout);
        getData();
    }

    private void getData() {
        mSocietySideFragment = mArrayListObservable
                .compose(RxHolder.<ArrayList<SocietyBean>>io_main())
                .subscribe(new Action1<ArrayList<SocietyBean>>() {
                               @Override
                               public void call(ArrayList<SocietyBean> societyBeen) {
                                   mSocietySideAdapter.setDatas(societyBeen);
                                   closeFresh(mRefreshLayout);
                               }
                           },
                        new Action1<Throwable>() {
                            @Override
                            public void call(Throwable throwable) {
                                closeFresh(mRefreshLayout);
                                Log.d("SocietySideFragment", "" + throwable.getMessage());
                            }
                        }
                );
        mSubscription.add(mSocietySideFragment);
    }

    @Override
    public void initView() {
        society_rv.setLayoutManager(mLinearLayoutManager);
        society_rv.setAdapter(mSocietySideAdapter);
        setRefreshLayout(mRefreshLayout);
        mRefreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        // 刷新动画开始后回调到此方法
                        mPresent.getUpData(UrlManager.SOCIETYSIDE + 1, mPresent.getDatas().size() == 0 ? null : mPresent.getDatas().get(0).Url)
                                .compose(RxHolder.<ArrayList<SocietyBean>>io_main())
                                .subscribe(new Action1<ArrayList<SocietyBean>>() {
                                               @Override
                                               public void call(ArrayList<SocietyBean> societyBeen) {
                                                   closeFresh(mRefreshLayout);
                                                   mSocietySideAdapter.setDatas(mPresent.getDatas());
                                                   society_rv.scrollToPosition(0);
                                               }
                                           },
                                        new Action1<Throwable>() {
                                            @Override
                                            public void call(Throwable throwable) {
                                                closeFresh(mRefreshLayout);
                                            }
                                        });
                    }
                }
        );
        society_rv.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE && mLastPosition + 1 == mSocietySideAdapter.getItemCount()) {
                    mSocietySideAdapter.changeMoreStatus(SocietySideAdapter.LOADING_MORE);
                    Subscription subscribe = mPresent.getArticle(UrlManager.SOCIETYSIDE + (++page))
                            .compose(RxHolder.<ArrayList<SocietyBean>>io_main())
                            .subscribe(new Action1<ArrayList<SocietyBean>>() {
                                @Override
                                public void call(ArrayList<SocietyBean> societyBeen) {
                                    mSocietySideAdapter.setDatas(societyBeen);
                                }
                            }, new Action1<Throwable>() {
                                @Override
                                public void call(Throwable throwable) {

                                }
                            });
                    mSubscription.add(subscribe);
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                mLastPosition = mLinearLayoutManager.findLastVisibleItemPosition();
            }
        });
    }
}
