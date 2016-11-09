package com.sineom.thinkday.present;

import android.text.Html;

import com.sineom.thinkday.bean.ArticleBean;

import org.jsoup.nodes.Document;

import rx.Observable;
import rx.functions.Func1;

/**
 * User: sineom(sineom@126.com)
 * Date: 2016-10-31
 * Time: 22:42
 * DESIC
 */
public class PresentIml implements Present<ArticleBean> {

    @Override
    public Observable<ArticleBean> getArticle(String url) {
        return GetDataManeger.sGetDataManeger().getAritcle(url)
                .flatMap(new Func1<Document, Observable<ArticleBean>>() {
                    @Override
                    public Observable<ArticleBean> call(Document document) {
                        ArticleBean bean = new ArticleBean();
                        bean.title = document.select("h2.articleTitle").text();
                        bean.author = document.select("div.articleAuthorName").text();
                        bean.contant = Html.fromHtml(document.select("div.articleContent").toString());
                        return Observable.just(bean);
                    }
                });
    }

}
