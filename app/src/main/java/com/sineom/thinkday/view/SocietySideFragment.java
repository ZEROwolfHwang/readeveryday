package com.sineom.thinkday.view;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.sineom.thinkday.R;
import com.sineom.thinkday.adapter.SocietySideAdapter;
import com.sineom.thinkday.bean.SocietyBean;
import com.sineom.thinkday.present.SocietyPresent;
import com.sineom.thinkday.present.UrlManager;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import butterknife.BindView;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 * @author sineom
 * @version 1.0
 * @time 2016/11/8 14:16
 * @des ${TODO}
 * @updateAuthor ${Author}
 * @updataTIme 2016/11/8
 * @updataDes ${描述更新内容}
 */

public class SocietySideFragment extends SingleFragment {
    @BindView(R.id.society_rv)
    RecyclerView society_rv;
    private SocietyPresent mPresent;
    private LinearLayoutManager mLinearLayoutManager;
    private SocietySideAdapter mSocietySideAdapter;

    @Override
    public int createView() {
        return R.layout.societyside_layout;
    }

    public SocietySideFragment() {
    }

    @Override
    public void initDatas() {
        mPresent = new SocietyPresent();
        mLinearLayoutManager = new LinearLayoutManager(getActivity());
        mSocietySideAdapter = new SocietySideAdapter(getActivity(), mPresent.getDatas(), new SocietySideAdapter.ItemClick() {
            @Override
            public void onItemClick(SocietyBean societyBean) {

            }
        });
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mPresent.getArticle(UrlManager.SOCIETYSIDE)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Document>() {
                               @Override
                               public void call(Document document) {
                                   long start = System.currentTimeMillis();
                                   Elements a13 = document.getElementsByClass("left_contant");
                                   for (Element element : a13) {
                                       Elements a = element.getElementsByTag("a");
                                       for (Element hraf : a) {
                                           String href = hraf.attr("href").toString();
                                           if (href.endsWith(".html")) {
                                               mPresent.getDatas().add(mPresent.saveData(hraf.attr("href").toString(),
                                                       hraf.attr("title").toString(), element.getElementsByTag("p").text()));
                                           }
                                       }
                                   }
                                   long end = System.currentTimeMillis();
//                                   society_rv.setLayoutManager(mLinearLayoutManager);
//                                   society_rv.setAdapter( new SocietySideAdapter(getActivity(), mPresent.getDatas()));
                                   mSocietySideAdapter.setDatas(mPresent.getDatas());

                                   Log.d("SocietySideFragment", "end-start:" + (end - start));
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
    public void initRecycler() {
        society_rv.setLayoutManager(mLinearLayoutManager);
        society_rv.setAdapter(mSocietySideAdapter);
    }
}
