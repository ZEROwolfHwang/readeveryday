package com.sineom.thinkday.present;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

import rx.Observable;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * User: sineom(sineom@126.com)
 * Date: 2016-10-31
 * Time: 22:42
 * DESIC
 */
public class GetDataManeger {
    private static GetDataManeger mGetDataManeger;

    private GetDataManeger() {

    }

    public static GetDataManeger sGetDataManeger() {
        if (mGetDataManeger == null) {
            synchronized (GetDataManeger.class) {
                if (mGetDataManeger == null)
                    mGetDataManeger = new GetDataManeger();
            }
        }
        return mGetDataManeger;
    }

    public Observable<Document> getAritcle(String url) {
        return Observable.just(url)
                .flatMap(new Func1<String, Observable<Document>>() {
                    @Override
                    public Observable<Document> call(String s) {
                        Document document = null;
                        try {
                            document = Jsoup.connect(s)
                                    .timeout(3000)// 设置连接超时时间
                                    .method(Connection.Method.POST)
                                    .get();
//                                    .post();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        return Observable.just(document);
                    }
                })
                .subscribeOn(Schedulers.io());
    }
}
