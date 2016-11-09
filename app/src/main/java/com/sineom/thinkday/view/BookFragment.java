package com.sineom.thinkday.view;

import android.os.Bundle;
import android.widget.TextView;

import com.sineom.thinkday.R;
import com.sineom.thinkday.present.PresentIml;

import butterknife.BindView;

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
