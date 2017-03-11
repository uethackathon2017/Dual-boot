package com.example.vaio.timestone.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.vaio.timestone.R;

/**
 * Created by vaio on 10/03/2017.
 */

public class NumberPickerRecyclerViewAdapter extends RecyclerView.Adapter<NumberPickerRecyclerViewAdapter.ViewHolder> {
    private int startNumber;
    private int endNumber;

    public NumberPickerRecyclerViewAdapter(int startNumber, int endNumber) {
        this.startNumber = startNumber; // số bắt đầu trong list number picker
        this.endNumber = endNumber; // số kết thúc trong list number picker
    }

    @Override
    public NumberPickerRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.item_recycler_view_number_picker, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(NumberPickerRecyclerViewAdapter.ViewHolder holder, int position) {
        holder.textView.setText(position + startNumber + "");
    }

    @Override
    public int getItemCount() {
        return endNumber - startNumber + 1;
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView textView;

        public ViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.tvNumber);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (onItemClick != null) {
                onItemClick.onClick(v, getPosition());
            }
        }
    }

    private OnItemClick onItemClick;

    public void setOnItemClick(OnItemClick onItemClick) {
        this.onItemClick = onItemClick;
    }

    public interface OnItemClick { // lắng nghe sự kiện onlick item
        void onClick(View view, int position);
    }
}
