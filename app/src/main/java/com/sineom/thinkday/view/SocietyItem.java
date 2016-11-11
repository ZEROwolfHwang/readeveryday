package com.sineom.thinkday.view;

import android.util.Log;
import android.widget.TextView;

import com.sineom.thinkday.R;
import com.sineom.thinkday.bean.ArticleBean;
import com.sineom.thinkday.present.GLobalData;
import com.sineom.thinkday.present.SocietyPresent;

import butterknife.BindView;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 * @author sineom
 * @version 1.0
 * @time 2016/11/9 17:31
 * @des ${TODO}
 * @updateAuthor ${Author}
 * @updataTIme 2016/11/9
 * @updataDes ${描述更新内容}
 */

public class SocietyItem extends SingleFragment {

    private final SocietyPresent mPresent;
    @BindView(R.id.article_title_tv)
    TextView mArticleTitleTv;
    @BindView(R.id.article_tv)
    TextView mArticleTv;
    private Observable<ArticleBean> mSocietyItem;


    public SocietyItem() {
        mPresent = new SocietyPresent();
    }

    @Override
    public void initDatas() {
        mSocietyItem = mPresent.getSocietyItem(getArguments().getString(GLobalData.SOCIETYSICE));
    }

    @Override
    public int createView() {
        return R.layout.fragment_society_item;
    }

    @Override
    public void initView() {
        mSocietyItem.observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<ArticleBean>() {
                               @Override
                               public void call(ArticleBean bean) {
                                   Log.d("SocietyPresent", "bean.contant:" + bean.contant);
                                   Log.d("SocietyPresent", " bean.title:" +  bean.title);
                                   mArticleTitleTv.setText(bean.title);
                                   mArticleTv.setText(bean.contant);
                               }
                           },
                        new Action1<Throwable>() {
                            @Override
                            public void call(Throwable throwable) {

                            }
                        }
                );
    }
}
