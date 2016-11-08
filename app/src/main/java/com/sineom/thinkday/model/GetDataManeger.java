package com.sineom.thinkday.model;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * User: sineom(sineom@126.com)
 * Date: 2016-10-31
 * Time: 22:42
 * DESIC
 */
public class GetDataManeger {
    public Observable<Document> getAritcle(String url) {
        return Observable.just(url)
                .flatMap(new Func1<String, Observable<Document>>() {
                    @Override
                    public Observable<Document> call(String s) {
                        Document document = null;
                        try {
                            document = Jsoup.connect(s)
                                    .timeout(5000)
                                    .get();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        return Observable.just(document);
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
