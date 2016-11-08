package com.sineom.thinkday.view;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.sineom.thinkday.R;
import com.sineom.thinkday.present.UrlManager;
import com.sineom.thinkday.present.PresentIml;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import butterknife.BindView;
import rx.functions.Action1;

/**
 * User: sineom(sineom@126.com)
 * Date: 2016-10-29
 * Time: 15:40
 * DESIC
 */
public class BookFragment extends SingleFragment {
    private PresentIml mPresentIml;
    @BindView(R.id.text)
    TextView mTextView;


    public BookFragment() {
    }

    @Override
    public void initDatas() {
        mPresentIml = new PresentIml();
        mPresentIml.getArticle(UrlManager.SOCIETYSIDE)
                .subscribe(new Action1<Document>() {
                    @Override
                    public void call(Document document) {
                        Elements a13 = document.getElementsByClass("left_contant");
                        for (Element element : a13
                                ) {
                            Elements a = element.getElementsByTag("a");
                            for (Element hraf : a
                                    ) {
                                String href = hraf.attr("href").toString();
                                if (href.endsWith(".html")) {

                                    Log.d("BookFragment", hraf.attr("href").toString());
                                    Log.d("BookFragment", hraf.attr("title").toString());
                                    Log.d("BookFragment", element.getElementsByTag("p").text());
                                }
                            }
                        }
                        mTextView.setText(document.body().toString());
                    }
                });
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public int createView() {
        return R.layout.fragment_book;
    }


}
