package com.sineom.thinkday.view;

import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.FrameLayout;

import com.sineom.thinkday.R;
import com.sineom.thinkday.present.PresentIml;

import butterknife.BindView;

/**
 * User: sineom(sineom@126.com)
 * Date: 2016-10-29
 * Time: 15:40
 * DESIC
 */
public class BookFragment extends SingleFragment implements SurfaceHolder.Callback {
    @BindView(R.id.sv_video)
    SurfaceView surfaceView;
    private PresentIml mPresentIml;
    @BindView(R.id.fl_root)
    FrameLayout root;

    public BookFragment() {
    }

    @Override
    public void initDatas() {

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mPresentIml = new PresentIml();
        surfaceView.getHolder().addCallback(this);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {

    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }

    @Override
    public int createView() {
        return R.layout.fragment_book;
    }


}
