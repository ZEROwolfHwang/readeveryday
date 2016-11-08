package com.sineom.thinkday.present;

import android.util.Log;

import com.sineom.thinkday.bean.SocietyBean;
import com.sineom.thinkday.model.SocietyModelImpl;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 * User: sineom(sineom@126.com)
 * Date: 2016-11-09
 * Time: 00:04
 * DESIC
 */
public class SocietyPresent implements Present {

    private final SocietyModelImpl mSocietyModel;
    private ArrayList<SocietyBean> mDatas;

    public SocietyPresent() {
        mSocietyModel = new SocietyModelImpl();
        mDatas = new ArrayList<>();
    }

    public void getDatasFormHtml(String url) {
        getArticle(url)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Document>() {
                               @Override
                               public void call(Document document) {
                                   Elements a13 = document.getElementsByClass("left_contant");
                                   for (Element element : a13
                                           ) {
                                       Elements a = element.getElementsByTag("a");
                                       for (Element hraf : a) {
                                           String href = hraf.attr("href").toString();
                                           if (href.endsWith(".html")) {
                                               mDatas.add(mSocietyModel.saveSociety(hraf.attr("href").toString(),
                                                       hraf.attr("title").toString(), element.getElementsByTag("p").text()));
//                                    Log.d("BookFragment", hraf.attr("href").toString());
//                                    Log.d("BookFragment", hraf.attr("title").toString());
//                                    Log.d("BookFragment", element.getElementsByTag("p").text());
                                           }
                                       }
                                   }
                               }
                           },
                        new Action1<Throwable>() {
                            @Override
                            public void call(Throwable throwable) {
                                Log.d("SocietyPresent", throwable.getMessage());
                            }
                        });
    }

    @Override
    public Observable<Document> getArticle(String url) {
        return GetDataManeger.sGetDataManeger().getAritcle(url);
    }

    public ArrayList<SocietyBean> getDatas() {
        return mDatas;
    }
}
