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
//                        Elements links = document.getElementsByClass("left_contant");
                        long start = System.currentTimeMillis();
                        Elements links = document.select("div.left_contant");
                        for (Element link : links) {
                            Elements select = link.select("div.contant_title > a");
                            String href = select.attr("href");
                            String title = select.attr("title");
                            String contant = link.select("div.listzi").text();
                            mDatas.add(saveData(href, title, contant));
                        }


//                        Elements a13 = document.getElementsByClass("left_contant");
//                        for (Element element : a13) {
//                            Elements a = element.getElementsByTag("a");
//                            for (Element hraf : a) {
//                                String href = hraf.attr("href").toString();
//                                if (href.endsWith(".html")) {
//                                    mDatas.add(saveData(hraf.attr("href").toString(),
//                                            hraf.attr("title").toString(), element.getElementsByTag("p").text()));
//                                }
//                            }
//                        }
                        long end = System.currentTimeMillis();
                        Log.d("SocietyPresent", "end-start:" + (end - start));
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
                        bean.contant = Html.fromHtml(document.select("div.neir").toString());
                        return Observable.just(bean);
                    }
                });
    }
}
