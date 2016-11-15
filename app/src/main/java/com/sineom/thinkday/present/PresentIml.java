package com.sineom.thinkday.present;

import android.text.Html;

import com.sineom.thinkday.bean.ArticleBean;

import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.concurrent.TimeUnit;

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
                .throttleFirst(2, TimeUnit.SECONDS)
                .flatMap(new Func1<Document, Observable<ArticleBean>>() {
                    @Override
                    public Observable<ArticleBean> call(Document document) {
                        ArticleBean bean = new ArticleBean();
                        try {
                            bean.title = document.select("h2.articleTitle").text();
                            bean.author = document.select("div.articleAuthorName").text();
                            bean.contant = Html.fromHtml(document.select("div.articleContent").toString());
                            return Observable.just(bean);
                        } catch (Exception e) {
                            return Observable.error(e);
                        }
                    }
                });
    }

    public Observable<ArticleBean> getRandomArticle(String url) {
        return GetDataManeger.sGetDataManeger().getAritcle(url)
                .throttleFirst(2, TimeUnit.SECONDS)
                .flatMap(new Func1<Document, Observable<ArticleBean>>() {
                    @Override
                    public Observable<ArticleBean> call(Document document) {
                        Elements article_text = document.getElementsByClass("article_text");
                        article_text.get(0).getElementsByTag("p").get(0).remove();
                        ArticleBean bean = new ArticleBean();
                        try {
                            bean.title = document.select("h1").text();
                            bean.author = document.select("p.article_author").text();
                            bean.contant = Html.fromHtml(article_text.toString());
                            return Observable.just(bean);
                        } catch (Exception e) {
                            return Observable.error(e);
                        }
                    }
                });
    }

    @Override
    public Observable<ArticleBean> getUpData(String url, String firstUrl) {
        return null;
    }

}
