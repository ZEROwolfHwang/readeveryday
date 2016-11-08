package com.sineom.thinkday.adapter;

import android.content.Context;
import android.view.View;

import com.sineom.thinkday.R;

import java.util.List;

/**
 * User: sineom(sineom@126.com)
 * Date: 2016-11-07
 * Time: 23:15
 * DESIC
 */
public class LeftDrawerAdapter extends BaseAdapter<String> {

    private CLick mClick;

    public LeftDrawerAdapter(Context context, List<String> datas, CLick cLick) {
        super(context, datas);
        mClick = cLick;
    }

    @Override
    int initItemLayout() {
        return R.layout.baseactivity_left_drawer_item;
    }

    @Override
    public void convert(ViewHolder holder, final String s) {
        holder.setText(R.id.drawer_item, s);
        holder.setOnClickListener(R.id.drawer_item, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mClick.onItemClick(getItemDatasIndex(s));
            }
        });
    }

    public interface CLick<T> {
        void onItemClick(T position);
    }
}
