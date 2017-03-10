package com.example.vaio.timestone.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.View;

import com.example.vaio.timestone.fragment.NumberPickerFragment;

/**
 * Created by vaio on 10/03/2017.
 */

public class NumberPickerViewPagerAdapter extends FragmentStatePagerAdapter {
    public static final int ITEM_QUANTITY = 9;
    private static final String TAG = "NumberPickerViewPagerAdapter";
    private int to;
    private int from;
    private NumberPickerFragment numberPickerFragment;

    public NumberPickerViewPagerAdapter(FragmentManager fm, int from, int to) {
        super(fm);
        this.to = to;
        this.from = from;
    }

    @Override
    public Fragment getItem(int position) {
        int start = ITEM_QUANTITY * position + from;
        int end = start + ITEM_QUANTITY - 1;
        if (end > to) {
            end = to;
        }
        numberPickerFragment = new NumberPickerFragment(start, end);
        numberPickerFragment.setOnItemClick(new NumberPickerFragment.OnItemClick() {
            @Override
            public void onClick(View view, int position) {
                if (onItemClick != null) {
                    onItemClick.onClick(view, position);
                }
            }
        });
        return numberPickerFragment;
    }

    @Override
    public int getCount() {
        if ((to - from + 1) % ITEM_QUANTITY == 0) {
            return (to - from + 1) / ITEM_QUANTITY;
        } else {
            return (to - from + 1) / ITEM_QUANTITY + 1;
        }
    }

    public void setOnItemClick(OnItemClick onItemClick) {
        this.onItemClick = onItemClick;
    }

    private OnItemClick onItemClick;

    public interface OnItemClick {
        void onClick(View view, int position);
    }
}
