package com.sineom.thinkday.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import java.util.List;

/**
 * @author sineom
 * @version 1.0
 * @time 2016/11/8 15:48
 * @des ${TODO}
 * @updateAuthor ${Author}
 * @updataTIme 2016/11/8
 * @updataDes ${描述更新内容}
 */

public abstract class BaseAdapter<T> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public List<T> mDatas;
    public Context mContext;
    public static final int NORMALTYPE = 0;
    public static final int LOADDATATYPE = 1;

    public BaseAdapter(Context context, List<T> datas) {
        mContext = context;
        mDatas = datas;
    }

    @Override
    public int getItemCount() {
        return mDatas.size() == 0 ? 0 : mDatas.size() + 1;
    }

    public int getItemDatasIndex(T t) {
        return mDatas.indexOf(t);
    }

    /**
     * 底部FootView布局
     */
    public static class FootViewHolder extends RecyclerView.ViewHolder {
        public FootViewHolder(View view) {
            super(view);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position + 1 == getItemCount()) {
            Log.d("SocietySideAdapter", "position + 1:" + (position + 1));
            Log.d("SocietySideAdapter", "getItemCount():" + getItemCount());
            return LOADDATATYPE;
        } else {
            return NORMALTYPE;
        }
    }
}
