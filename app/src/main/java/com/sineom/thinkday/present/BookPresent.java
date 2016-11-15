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
public class BookPresent implements Present<ArrayList<SocietyBean>> {

    private final SocietyModelImpl mSocietyModel;
    private ArrayList<SocietyBean> mDatas;

    public BookPresent() {
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

                                String attr = link.getElementsByTag("em").get(0).attr("href");
                                link.getElementsByTag("em").remove();
                                link.getElementsByTag("label").remove();
                                link.getElementsByTag("span ").remove();
                                String replace = link.text().replace("分类: ", "");
                                Log.d("BookPresent", attr);
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
                .map(new Func1<Document, ArrayList<SocietyBean>>() {
                    @Override
                    public ArrayList<SocietyBean> call(Document document) {
                        long start = System.currentTimeMillis();
                        try {
                            Elements links = document.select("div.left_contant");
                            for (Element link : links) {
                                Elements select = link.select("div.contant_title > a");
                                String href = select.attr("href");
                                if (firstUrl.equals(href)) {
                                    Observable.empty();
                                    break;
                                }
                                String title = select.attr("title");
                                String contant = link.select("div.listzi").text();
                                mDatas.add(0, saveData(href, title, contant));
                            }
                            long end = System.currentTimeMillis();
                            Log.d("SocietyPresent", "end-start:" + (end - start));
                        } catch (Exception e) {
                            Observable.error(e);
                        }
                        return null;
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
                            document.getElementsByTag("img ").remove();
                            bean.contant = Html.fromHtml(document.select("div.neir").toString());
                            bean.title = document.select("h1").text();

                        } catch (Exception e) {
                            Observable.error(e);
                        }
                        return Observable.just(bean);
                    }
                });
    }
}
