package com.sineom.thinkday.adapter;

import android.content.Context;
import android.support.annotation.IntDef;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sineom.thinkday.R;
import com.sineom.thinkday.bean.SocietyBean;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author sineom
 * @version 1.0
 * @time 2016/11/8 16:32
 * @des ${TODO}
 * @updateAuthor ${Author}
 * @updataTIme 2016/11/8
 * @updataDes ${描述更新内容}
 */

//public class SocietySideAdapter extends BaseAdapter<SocietyBean> {

public class SocietySideAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    //上拉加载更多
    public static final int PULLUP_LOAD_MORE = 0;
    //正在加载中
    public static final int LOADING_MORE = 1;
    //上拉加载更多状态-默认为0
    private int load_more_status = 0;
    private LayoutInflater mInflater;
    private static final int TYPE_ITEM = 0;  //普通Item View
    private static final int TYPE_FOOTER = 1;  //顶部FootView
    public ItemClick mItemClick;
    public ArrayList<SocietyBean> mDatas;

    public SocietySideAdapter(Context context, ArrayList<SocietyBean> datas, ItemClick itemClick) {
        mItemClick = itemClick;
        this.mInflater = LayoutInflater.from(context);
        mDatas = datas;
    }

    public void setDatas(ArrayList<SocietyBean> datas) {
        mDatas = datas;
        notifyDataSetChanged();
    }

    /**
     * item显示类型
     *
     * @param parent
     * @param viewType
     * @return
     */
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //进行判断显示类型，来创建返回不同的View
        if (viewType == TYPE_ITEM) {
            View view = mInflater.inflate(R.layout.societyside_item, parent, false);
            //这边可以做一些属性设置，甚至事件监听绑定
            //view.setBackgroundColor(Color.RED);
            ItemViewHolder itemViewHolder = new ItemViewHolder(view);
            return itemViewHolder;
        } else if (viewType == TYPE_FOOTER) {
            View foot_view = mInflater.inflate(R.layout.foot_view, parent, false);
            //这边可以做一些属性设置，甚至事件监听绑定
            //view.setBackgroundColor(Color.RED);
            FootViewHolder footViewHolder = new FootViewHolder(foot_view);
            return footViewHolder;
        }
        return null;
    }

    /**
     * 数据的绑定显示
     *
     * @param holder
     * @param position
     */
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final int layoutPosition = holder.getLayoutPosition();
        if (holder instanceof ItemViewHolder) {
            ((ItemViewHolder) holder).itemTitle_tv.setText(mDatas.get(position).title);
            ((ItemViewHolder) holder).itemContant_tv.setText(mDatas.get(position).listzi);
            holder.itemView.setTag(position);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mItemClick.onItemClick(mDatas.get(layoutPosition));
                }
            });
        } else if (holder instanceof FootViewHolder) {
            FootViewHolder footViewHolder = (FootViewHolder) holder;
            switch (load_more_status) {
                case PULLUP_LOAD_MORE:
                    footViewHolder.foot_view_item_tv.setText("上拉加载更多...");
                    break;
                case LOADING_MORE:
                    footViewHolder.foot_view_item_tv.setText("正在加载更多数据...");
                    break;
            }
        }
    }

    /**
     * 进行判断是普通Item视图还是FootView视图
     *
     * @param position
     * @return
     */
    @Override
    public int getItemViewType(int position) {
        // 最后一个item设置为footerView
        if (position + 1 == getItemCount()) {
            return TYPE_FOOTER;
        } else {
            return TYPE_ITEM;
        }
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    //自定义的ViewHolder，持有每个Item的的所有界面元素
    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.society_title)
        public TextView itemTitle_tv;
        @BindView(R.id.society_desc)
        public TextView itemContant_tv;

        public ItemViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    /**
     * 底部FootView布局
     */
    public static class FootViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.loadMore)
        public TextView foot_view_item_tv;
        @BindView(R.id.footRootView)
        public RelativeLayout foot_view_item_root;

        public FootViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }


    /**
     * //上拉加载更多
     * PULLUP_LOAD_MORE=0;
     * //正在加载中
     * LOADING_MORE=1;
     * //加载完成已经没有更多数据了
     * NO_MORE_DATA=2;
     *
     * @param status
     */
    public void changeMoreStatus(@Status int status) {
        load_more_status = status;
        notifyDataSetChanged();
    }

    //添加数据
    public void addItem(List<SocietyBean> newDatas) {
        newDatas.addAll(mDatas);
        mDatas.removeAll(mDatas);
        mDatas.addAll(newDatas);
        notifyDataSetChanged();
    }

    public void addMoreItem(List<SocietyBean> newDatas) {
        mDatas.addAll(newDatas);
        notifyDataSetChanged();
    }

    public interface ItemClick {
        void onItemClick(SocietyBean societyBean);
    }

    @IntDef({PULLUP_LOAD_MORE, LOADING_MORE})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Status {
    }
}