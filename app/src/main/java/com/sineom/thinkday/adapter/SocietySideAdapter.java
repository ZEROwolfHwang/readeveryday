package com.sineom.thinkday.adapter;

import android.content.Context;
import android.view.View;

import com.sineom.thinkday.R;
import com.sineom.thinkday.bean.SocietyBean;

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

public class SocietySideAdapter extends BaseAdapter<SocietyBean> {

    public ItemClick mItemClick;

    public SocietySideAdapter(Context context, List datas, ItemClick itemClick) {
        super(context, datas);
        mItemClick = itemClick;
    }

    @Override
    int initItemLayout() {
        return R.layout.societyside_item;
    }

    @Override
    public void convert(ViewHolder holder, final SocietyBean societyBean) {
        holder.setText(R.id.society_title, societyBean.title);
        holder.setText(R.id.society_desc, societyBean.listzi);
        holder.setOnClickListener(R.id.society_root_view, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mItemClick.onItemClick(societyBean);
            }
        });
    }

    public void setDatas(List datas) {
        mDatas = datas;
        notifyDataSetChanged();
    }

    public interface ItemClick {
        void onItemClick(SocietyBean societyBean);
    }
}



