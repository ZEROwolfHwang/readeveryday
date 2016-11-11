package com.sineom.thinkday.present;

import rx.Observable;

/**
 * User: sineom(sineom@126.com)
 * Date: 2016-10-31
 * Time: 22:41
 * DES
 */
public interface Present<T> {
    Observable<T> getArticle(String url);

    Observable<T> getUpData(String url, String firstUrl);


}
