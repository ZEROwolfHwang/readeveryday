package com.sineom.thinkday.view;

import android.os.Bundle;
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

    private void setAritcle() {
        mSubscribe = mArticleBeanObservable
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<ArticleBean>() {
                               @Override
                               public void call(ArticleBean articleBean) {
                                   mArticleTitleTv.setText(articleBean.title);
                                   mArticleAuthorTv.setText(articleBean.author);
                                   mArticleTv.setText(articleBean.contant);
                               }
                           },
                        new Action1<Throwable>() {
                            @Override
                            public void call(Throwable throwable) {
                                Log.d("ArticleFragment", throwable.getMessage());
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
