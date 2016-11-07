package com.sineom.thinkday.adapter;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * User: sineom(sineom@126.com)
 * Date: 2016-11-07
 * Time: 23:31
 * DESIC
 */
public class LeftDrawerItemDecoration extends RecyclerView.ItemDecoration {
    private int space;

    public LeftDrawerItemDecoration(int space) {
        this.space = space;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        outRect.top = space;
    }
}
