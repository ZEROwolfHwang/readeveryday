package com.sineom.thinkday.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;

import com.sineom.thinkday.R;

import java.util.List;

/**
 * @author sineom
 * @version 1.0
 * @time 2016/11/8 16:32
 * @des ${TODO}
 * @updateAuthor ${Author}
 * @updataTIme 2016/11/8
 * @updataDes ${描述更新内容}
 */

public class SocietySideAdapter extends BaseAdapter {

    public SocietySideAdapter(Context context, List datas) {
        super(context, datas);
    }

    @Override
    int initItemLayout() {
        return R.layout.societyside_item;
    }

    @Override
    public void convert(ViewHolder holder, Object o) {

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }
}
