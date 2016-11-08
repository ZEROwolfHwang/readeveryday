package com.sineom.thinkday.present;

import android.view.View;
import android.widget.TextView;

import org.jsoup.nodes.Document;

import rx.Observable;

/**
 * User: sineom(sineom@126.com)
 * Date: 2016-10-31
 * Time: 22:42
 * DESIC
 */
public class PresentIml implements Present {

    private final GetDataManeger mManeger;

    public PresentIml() {
        mManeger = new GetDataManeger();
    }

    @Override
    public Observable<Document> getArticle(String url) {
        return mManeger.getAritcle(url);
    }

    public void setText(View view, String text) {
        TextView textView = (TextView) view;
        textView.setText(text);
    }
}
