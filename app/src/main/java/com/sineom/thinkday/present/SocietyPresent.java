package com.sineom.thinkday.present;

import android.text.Html;
import android.util.Log;

import com.sineom.thinkday.bean.ArticleBean;
import com.sineom.thinkday.bean.SocietyBean;
import com.sineom.thinkday.model.SocietyModelImpl;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.functions.Func1;

/**
 * User: sineom(sineom@126.com)
 * Date: 2016-11-09
 * Time: 00:04
 * DESIC
 */
public class SocietyPresent implements Present<ArrayList<SocietyBean>> {

    private final SocietyModelImpl mSocietyModel;
    private ArrayList<SocietyBean> mDatas;

    public SocietyPresent() {
        mSocietyModel = new SocietyModelImpl();
        mDatas = new ArrayList<>();
    }

    public ArrayList<SocietyBean> getDatas() {
        return mDatas;
    }

    public SocietyBean saveData(String url, String title, String desc) {
        return mSocietyModel.saveSociety(url, title, desc);
    }

    @Override
    public Observable<ArrayList<SocietyBean>> getArticle(String url) {
        return GetDataManeger.sGetDataManeger().getAritcle(url)
                .throttleFirst(2, TimeUnit.SECONDS)
                .map(new Func1<Document, ArrayList<SocietyBean>>() {
                    @Override
                    public ArrayList<SocietyBean> call(Document document) {
                        long start = System.currentTimeMillis();
                        try {
                            Elements links = document.select("dd.xs2l");
                            for (Element link : links) {
                                String title = link.select("a.xi2").get(0).text();
                                String attr = link.select("a.xi2").get(0).attr("href");
                                link.getElementsByTag("em").remove();
                                link.getElementsByTag("label").remove();
                                link.getElementsByTag("span ").remove();
                                String replace = link.text().replace("分类: ", "");
                                mDatas.add(saveData(attr, title, replace));
                            }
                            long end = System.currentTimeMillis();
                            Log.d("SocietyPresent", "end-start:" + (end - start));
                        } catch (Exception e) {
                            Observable.error(e);
                        }
                        return mDatas;
                    }
                });
    }

    @Override
    public Observable<ArrayList<SocietyBean>> getUpData(String url, final String firstUrl) {
        return GetDataManeger.sGetDataManeger().getAritcle(url)
                .throttleFirst(3, TimeUnit.SECONDS)
                .filter(new Func1<Document, Boolean>() {
                    @Override
                    public Boolean call(Document document) {
                        String href = document.select("dd.xs2l").get(0).select("a.xi2").get(0).attr("href");
                        boolean b = !firstUrl.equalsIgnoreCase(href);
                        return !firstUrl.equalsIgnoreCase(href);
                    }
                })
                .map(new Func1<Document, ArrayList<SocietyBean>>() {
                    @Override
                    public ArrayList<SocietyBean> call(Document document) {
                        mDatas = new ArrayList<SocietyBean>();
                        long start = System.currentTimeMillis();
                        try {
                            Elements links = document.select("dd.xs2l");
                            for (Element link : links) {
                                String title = link.select("a.xi2").get(0).text();
                                String attr = link.select("a.xi2").get(0).attr("href");
                                link.getElementsByTag("em").remove();
                                link.getElementsByTag("label").remove();
                                link.getElementsByTag("span ").remove();
                                String replace = link.text().replace("分类: ", "");
                                mDatas.add(saveData(attr, title, replace));
                            }
                            long end = System.currentTimeMillis();
                            Log.d("SocietyPresent", "end-start:" + (end - start));
                        } catch (Exception e) {
                            Observable.error(e);
                        }
                        return mDatas;
                    }
                });
    }

    public Observable<ArticleBean> getSocietyItem(String url) {
        return GetDataManeger.sGetDataManeger()
                .getAritcle(url)
                .flatMap(new Func1<Document, Observable<ArticleBean>>() {
                    @Override
                    public Observable<ArticleBean> call(Document document) {
                        ArticleBean bean = new ArticleBean();
                        try {
                            document.getElementsByTag("img").remove();
                            Element article_content = document.getElementById("article_content");
                            article_content.removeClass("bdsharebuttonbox");
                            article_content.getElementsByTag("a").remove();
                            article_content.getElementsByTag("script").remove();
                            document.getElementsByClass("a_af").remove();
                            bean.contant1 = article_content.toString();
                            bean.title = document.select("h1").text();
                            if(bean.contant1.contains("尽在")){
                                bean.contant1.replace("尽在","");
                            }
                                Log.d("SocietyPresent", bean.title);
                            bean.contant = Html.fromHtml(bean.contant1);
                        } catch (Exception e) {
                            Observable.error(e);
                        }
                        return Observable.just(bean);
                    }
                });
    }
}
