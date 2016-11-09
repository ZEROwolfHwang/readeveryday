package com.sineom.thinkday.present;

import com.sineom.thinkday.bean.ArticleBean;

import org.jsoup.nodes.Document;

import rx.Observable;

/**
 * User: sineom(sineom@126.com)
 * Date: 2016-10-31
 * Time: 22:41
 * DES
 */
public interface Present {
    Observable<ArticleBean> getArticle(String url);

    Observable<Document> getData(String url);
}
