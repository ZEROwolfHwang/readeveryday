package com.sineom.thinkday.view;

import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sineom.thinkday.R;
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

    @Override
    public View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_article, container, false);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresentIml = new PresentIml();
        setAritcle();
    }

    private void setAritcle() {
        mSubscribe = mPresentIml.getArticle("http://meiriyiwen.com/")
                .subscribe(new Action1<Document>() {
                    @Override
                    public void call(Document document) {
                        mArticleTitleTv.setText(document.getElementsByClass("articleTitle").text());
                        mArticleAuthorTv.setText(document.getElementsByClass("articleAuthorName").text());
                        mArticleTv.setText(Html.fromHtml(document.getElementsByClass("articleContent").toString()));
                    }
                });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mSubscribe.unsubscribe();
    }
}
