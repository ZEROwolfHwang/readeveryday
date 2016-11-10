package com.sineom.thinkday.view;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.widget.TextView;

import com.sineom.thinkday.R;
import com.sineom.thinkday.bean.ArticleBean;
import com.sineom.thinkday.present.PresentIml;
import com.sineom.thinkday.present.UrlManager;

import butterknife.BindView;
import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 * User: sineom(sineom@126.com)
 * Date: 2016-10-29
 * Time: 15:30
 * DESIC
 */
public class ArticleFragment extends SingleFragment {
    @BindView(R.id.article_title_tv)
    TextView mArticleTitleTv;
    @BindView(R.id.article_author_tv)
    TextView mArticleAuthorTv;
    @BindView(R.id.article_tv)
    TextView mArticleTv;
    @BindView(R.id.fragment_article)
    SwipeRefreshLayout mRefreshLayout;
    private PresentIml mPresentIml;
    private Subscription mSubscribe;
    private Observable<ArticleBean> mArticleBeanObservable;

    public ArticleFragment() {
    }

    @Override
    public void initDatas() {
        mPresentIml = new PresentIml();
        mArticleBeanObservable = mPresentIml.getArticle(UrlManager.ARTICLE);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setAritcle();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                mRefreshLayout.setRefreshing(true);
            }
        });
        freshData();
    }

    private void freshData() {
        ////// 设定下拉圆圈的背景
        mRefreshLayout.setProgressBackgroundColor(R.color.Indigo_colorPrimary);
////        //设置下拉出现小圆圈是否是缩放出现，出现的位置，最大的下拉位置
        mRefreshLayout.setProgressViewOffset(true, 50, 0);
////        mRefreshLayout.setDistanceToTriggerSync(20);
////
//////设置下拉圆圈的大小，两个值 LARGE， DEFAULT
        mRefreshLayout.setSize(SwipeRefreshLayout.LARGE);
//
////// 设置下拉圆圈上的颜色，蓝色、绿色、橙色、红色
        mRefreshLayout.setColorSchemeResources(
                android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        mRefreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        // 刷新动画开始后回调到此方法

                    }
                }
        );
    }

    private void setAritcle() {
        mSubscribe = mArticleBeanObservable
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<ArticleBean>() {
                               @Override
                               public void call(ArticleBean articleBean) {
//                                    通过 setEnabled(false) 禁用下拉刷新
                                   new Handler().postDelayed(new Runnable() {
                                       @Override
                                       public void run() {
                                           // 停止刷新
                                           mRefreshLayout.setRefreshing(false);
                                       }
                                   }, 500);

                                   if (articleBean == null)
                                       return;
                                   mArticleTitleTv.setText(articleBean.title);
                                   mArticleAuthorTv.setText(articleBean.author);
                                   mArticleTv.setText(articleBean.contant);
                               }
                           },
                        new Action1<Throwable>() {
                            @Override
                            public void call(Throwable throwable) {
                                Log.d("ArticleFragment", throwable.getMessage() + "");
                            }
                        }
                );
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (!mSubscribe.isUnsubscribed())
            mSubscribe.unsubscribe();
    }

    @Override
    public int createView() {
        return R.layout.fragment_article;
    }
}
