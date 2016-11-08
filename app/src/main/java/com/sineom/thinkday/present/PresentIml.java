package com.sineom.thinkday.present;

import org.jsoup.nodes.Document;

import rx.Observable;

/**
 * User: sineom(sineom@126.com)
 * Date: 2016-10-31
 * Time: 22:42
 * DESIC
 */
public class PresentIml implements Present {

    @Override
    public Observable<Document> getArticle(String url) {
        return GetDataManeger.sGetDataManeger().getAritcle(url);
    }
}
