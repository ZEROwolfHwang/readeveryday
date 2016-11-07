package com.sineom.thinkday.view;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sineom.thinkday.R;
import com.sineom.thinkday.BaseActivity;
import com.sineom.thinkday.present.PresentIml;
import com.sineom.thinkday.present.SingleFragment;

import org.jsoup.nodes.Document;

import butterknife.BindView;
import rx.Subscription;
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
    private BaseActivity mBaseActivity;

    @Override
    public View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_article, container, false);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mBaseActivity = (BaseActivity) activity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        mBaseActivity.toolbarTitle.setText("每日一文");
        mPresentIml = new PresentIml();
        setAritcle();
    }

    private void setAritcle() {
        mSubscribe = mPresentIml.getArticle()
                .subscribe(new Action1<Document>() {
                    @Override
                    public void call(Document document) {
                        mArticleTitleTv.setText(document.getElementsByClass("articleTitle").text());
                        mArticleAuthorTv.setText(document.getElementsByClass("articleAuthorName").text());
                        mArticleTv.setText(document.body().toString());
                        Log.d("ArticleFragment", "--"+document.getElementsByTag("audio").attr("src"));
                    }
                });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mSubscribe.unsubscribe();
    }
}
