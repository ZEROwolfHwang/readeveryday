package com.sineom.thinkday.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sineom.thinkday.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * User: sineom(sineom@126.com)
 * Date: 2016-11-07
 * Time: 23:15
 * DESIC
 */
public class LeftDrawerAdapter extends RecyclerView.Adapter<LeftDrawerAdapter.ViewHolder> {

    private CLick mClick;
    private ArrayList<String> mDatas;
    private Context mContext;

    public LeftDrawerAdapter(Context context, ArrayList<String> datas, CLick cLick) {
        mContext = context;
        mDatas = datas;
        mClick = cLick;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder holder = new ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.baseactivity_left_drawer_item, parent, false));
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.itemTV.setText(mDatas.get(position));
    }

    @Override
    public int getItemCount() {
        return mDatas == null ? 0 : mDatas.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.drawer_item)
        TextView itemTV;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemTV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mClick.onItemClick(getLayoutPosition());
                }
            });
        }
    }

    public interface CLick {
        void onItemClick(int position);
    }
}
