package com.sineom.thinkday.view;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sineom.thinkday.R;
import com.sineom.thinkday.present.GLobalData;
import com.sineom.thinkday.present.SingleFragment;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * User: sineom(sineom@126.com)
 * Date: 2016-10-29
 * Time: 14:01
 * DESIC
 */
public class MainFragment extends SingleFragment {
    @BindView(R.id.bookTv)
    TextView mBookTv;

    @Override
    public View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_main, container, false);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        ((BaseActivity) getActivity()).toolbarTitle.setText("每日一识");
    }

    @OnClick(R.id.essayTv)
    public void essayTv(View view) {

        Fragment article = mManager.findFragmentById(R.id.fragment_article);
        if (article == null)
            article = new ArticleFragment();
        mManager.beginTransaction()
                .replace(R.id.fragment_content, article, GLobalData.ARTICLE)
                .commit();
    }

    @OnClick(R.id.bookTv)
    public void bookTv(View view) {
        Fragment book = mManager.findFragmentById(R.id.fl_root);
        if (book == null)
            book = new BookFragment();
        mManager.beginTransaction()
                .replace(R.id.fragment_content, book, GLobalData.BOOK)
                .commit();
    }
}
