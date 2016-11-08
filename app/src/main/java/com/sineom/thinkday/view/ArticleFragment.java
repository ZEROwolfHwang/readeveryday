package com.sineom.thinkday.view;

import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.widget.TextView;

import com.sineom.thinkday.R;
import com.sineom.thinkday.present.UrlManager;
import com.sineom.thinkday.present.PresentIml;

import org.jsoup.nodes.Document;

import butterknife.BindView;
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

    public ArticleFragment() {
    }

    @Override
    public void initDatas() {
        mPresentIml = new PresentIml();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setAritcle();
    }

    private void setAritcle() {
        mSubscribe = mPresentIml.getArticle(UrlManager.ARTICLE)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Document>() {
                               @Override
                               public void call(Document document) {
                                   mArticleTitleTv.setText(document.getElementsByClass("articleTitle").text());
                                   mArticleAuthorTv.setText(document.getElementsByClass("articleAuthorName").text());
                                   mArticleTv.setText(Html.fromHtml(document.getElementsByClass("articleContent").toString()));
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
        mSubscribe.unsubscribe();
    }

    @Override
    public int createView() {
        return R.layout.fragment_article;
    }
}
