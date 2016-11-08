package com.sineom.thinkday.present;

import org.jsoup.nodes.Document;

import rx.Observable;

/**
 * User: sineom(sineom@126.com)
 * Date: 2016-10-31
 * Time: 22:41
 * DES
 */
public interface Present {
    Observable<Document> getArticle(String url);

    void getBook();
}
