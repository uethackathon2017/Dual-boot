package com.example.vaio.timestone.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.vaio.timestone.R;
import com.example.vaio.timestone.adapter.NumberPickerRecyclerViewAdapter;

/**
 * Created by vaio on 10/03/2017.
 */

@SuppressLint("ValidFragment")
public class NumberPickerFragment extends Fragment {
    public static final int SPAN_COUNT = 3; // số cột recyclerview bằng  3
    private RecyclerView recyclerView;
    private int start;
    private int end;

    @SuppressLint("ValidFragment")
    public NumberPickerFragment(int start, int end) {
        this.start = start;
        this.end = end;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        inflater = LayoutInflater.from(getContext());
        View view = inflater.inflate(R.layout.fragment_number_picker, container, false);
        try {
            initViews(view);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return view;
    }

    private void initViews(View view) throws Exception {
        // Khởi tạo danh sách number picker
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerViewNumberPicker);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), SPAN_COUNT, GridLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(gridLayoutManager);
        NumberPickerRecyclerViewAdapter numberPickerRecyclerViewAdapter = new NumberPickerRecyclerViewAdapter(start, end);
        recyclerView.setAdapter(numberPickerRecyclerViewAdapter);
        numberPickerRecyclerViewAdapter.setOnItemClick(new NumberPickerRecyclerViewAdapter.OnItemClick() {
            @Override
            public void onClick(View view, int position) {
                if (onItemClick != null) {
                    onItemClick.onClick(view, position);
                }
            }
        });
    }

    private OnItemClick onItemClick;

    public void setOnItemClick(OnItemClick onItemClick) {
        this.onItemClick = onItemClick;
    }

    public interface OnItemClick {
        // lắng nghe sự kiện on item click
        void onClick(View view, int position);
    }

}

