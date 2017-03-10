package com.example.vaio.timestone.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.vaio.timestone.R;
import com.example.vaio.timestone.model.Item;

import java.util.ArrayList;

/**
 * Created by vaio on 10/03/2017.
 */

public class EventRecyclerViewAdapter extends RecyclerView.Adapter<EventRecyclerViewAdapter.ViewHolder> {

    private ArrayList<Item> arrItem;

    public EventRecyclerViewAdapter(ArrayList<Item> arrItem) {
        this.arrItem = arrItem;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_recycler_view_event, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Item item = arrItem.get(position);
        holder.tvDate.setText(item.getE_date());
        holder.tvInfo.setText(item.getE_info());
        holder.tvType.setText(item.getE_type());
        if (position >= 0) {
            if (onCompleteLoading != null) {
                onCompleteLoading.onComplete();
            }
        }
    }

    @Override
    public int getItemCount() {
        return arrItem.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tvDate;
        TextView tvInfo;
        TextView tvType;

        public ViewHolder(View itemView) {
            super(itemView);
            tvDate = (TextView) itemView.findViewById(R.id.tvDate);
            tvInfo = (TextView) itemView.findViewById(R.id.tvInfo);
            tvType = (TextView) itemView.findViewById(R.id.tvType);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (onItemClick != null) {
                onItemClick.onClick(v, getPosition());
            }
        }
    }

    public void setOnCompleteLoading(OnCompleteLoading onCompleteLoading) {
        this.onCompleteLoading = onCompleteLoading;
    }

    private OnCompleteLoading onCompleteLoading;

    public interface OnCompleteLoading {
        void onComplete();
    }

    public void setOnItemClick(OnItemClick onItemClick) {
        this.onItemClick = onItemClick;
    }

    private OnItemClick onItemClick;

    public interface OnItemClick {
        void onClick(View view, int position);
    }
}