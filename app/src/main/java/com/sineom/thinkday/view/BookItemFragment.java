package com.sineom.thinkday.view;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.TextView;

import com.sineom.thinkday.BaseActivity;
import com.sineom.thinkday.R;
import com.sineom.thinkday.bean.ArticleBean;
import com.sineom.thinkday.present.GLobalData;
import com.sineom.thinkday.present.SocietyPresent;

import butterknife.BindView;
import butterknife.OnClick;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 * @author sineom
 * @version 1.0
 * @time 2016/11/9 17:31
 * @des ${TODO}
 * @updateAuthor ${Author}
 * @updataTIme 2016/11/9
 * @updataDes ${描述更新内容}
 */

public class BookItemFragment extends SingleFragment {

    private final SocietyPresent mPresent;
    @BindView(R.id.article_title_tv)
    TextView mArticleTitleTv;
    @BindView(R.id.article_tv)
    TextView mArticleTv;
    @BindView(R.id.society_swipeRefresh)
    SwipeRefreshLayout mSwipeRefreshLayout;

    private Observable<ArticleBean> mSocietyItem;


    public BookItemFragment() {
        mPresent = new SocietyPresent();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setRefreshLayout(mSwipeRefreshLayout);
        fresh(mSwipeRefreshLayout);
    }

    @Override
    public void initDatas() {
        mSocietyItem = mPresent.getSocietyItem(getArguments().getString(GLobalData.SOCIETYSICE));
    }

    @Override
    public int createView() {
        return R.layout.fragment_society_item;
    }

    @OnClick(R.id.fab_back)
    public void back(View view) {
        ((BaseActivity) getActivity()).initFragment(new BookFragment(), "BookItemFragment");
//        ((BaseActivity) getActivity()).fragmentHideAndShow(BookItemFragment.this, new BookFragment());
    }

    @Override
    public void initView() {
        mSocietyItem.observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<ArticleBean>() {
                               @Override
                               public void call(ArticleBean bean) {
                                   closeFresh(mSwipeRefreshLayout);
                                   mSwipeRefreshLayout.setEnabled(false);
                                   mArticleTitleTv.setText(bean.title);
                                   mArticleTv.setText(bean.contant);
                               }
                           },
                        new Action1<Throwable>() {
                            @Override
                            public void call(Throwable throwable) {
                                closeFresh(mSwipeRefreshLayout);
                            }
                        }
                );
    }
}
