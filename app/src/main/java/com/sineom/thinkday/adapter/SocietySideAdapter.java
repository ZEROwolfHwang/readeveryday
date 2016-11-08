package com.sineom.thinkday.adapter;

import android.content.Context;

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

    public SocietySideAdapter(Context context, List datas) {
        super(context, datas);
    }

    @Override
    int initItemLayout() {
        return R.layout.societyside_item;
    }

    @Override
    public void convert(ViewHolder holder, SocietyBean societyBean) {
        holder.setText(R.id.society_title, societyBean.title);
        holder.setText(R.id.society_desc, societyBean.listzi);
    }
}
