package com.example.vaio.timestone.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.vaio.timestone.R;
import com.example.vaio.timestone.activity.MainActivity;
import com.example.vaio.timestone.adapter.EventRecyclerViewAdapter;
import com.example.vaio.timestone.adapter.NumberPickerViewPagerAdapter;
import com.example.vaio.timestone.model.CurrentTime;
import com.example.vaio.timestone.model.Item;
import com.viewpagerindicator.CirclePageIndicator;

import java.util.ArrayList;

/**
 * Created by vaio on 10/03/2017.
 */

@SuppressLint("ValidFragment")
public class ContentMainFragment extends Fragment implements View.OnClickListener, NumberPickerViewPagerAdapter.OnItemClick {

    // cen ->  mon -> year -> cen
    public static final String CENTURY = "Century";
    public static final String MONTH = "Month";
    public static final String YEAR = "Year";

    public static final int CENTURY_START_AT = 1;
    public static final int CENTURY_END_AT = 21;
    public static final int MONTH_START_AT = 1;
    public static final int MONTH_END_AT = 12;
    private static final String TAG = "ContentMainFragment";

    private RecyclerView recyclerView;
    private ImageView ivBack;
    private ImageView ivForward;
    private ViewPager viewPager;
    private TextView tvTitle;
    private EventRecyclerViewAdapter eventRecyclerViewAdapter;
    private NumberPickerViewPagerAdapter numberPickerViewPagerAdapter;
    private ContentLoadingProgressBar progressBar;
    private CirclePageIndicator circlePageIndicator;

    private ArrayList<Item> arrItem;
    private ArrayList<Item> arrItemTmp = new ArrayList<>();
    private String currentContent;
    private int centurySelected = 1;
    private int yearSelected = 0;
    private int monthSelected = 0;

    @SuppressLint("ValidFragment")
    public ContentMainFragment(ArrayList<Item> arrItem) {
        this.arrItem = arrItem;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        inflater = LayoutInflater.from(getContext());
        View view = inflater.inflate(R.layout.fragment_content_main, container, false);
        initViews(view);

        return view;
    }

    protected void initViews(View view) {
        try {
            arrItemTmp.addAll(arrItem);
            progressBar = (ContentLoadingProgressBar) view.findViewById(R.id.contentLoadingProgressBar);
            currentContent = CENTURY;
            circlePageIndicator = (CirclePageIndicator) view.findViewById(R.id.circlePageIndicator);

            tvTitle = (TextView) view.findViewById(R.id.tvDate);
            tvTitle.setText(currentContent);
            recyclerView = (RecyclerView) view.findViewById(R.id.recyclerViewEvent);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
            initEventRecyclerViewAdapter(arrItem);

            //
            ivBack = (ImageView) view.findViewById(R.id.ivBack);
            ivForward = (ImageView) view.findViewById(R.id.ivFoward);
            ivBack.setOnClickListener(this);
            ivForward.setOnClickListener(this);
//
            viewPager = (ViewPager) view.findViewById(R.id.viewPager);
            numberPickerViewPagerAdapter = new NumberPickerViewPagerAdapter(getFragmentManager(), CENTURY_START_AT, CENTURY_END_AT);
            numberPickerViewPagerAdapter.setOnItemClick(this);
            viewPager.setAdapter(numberPickerViewPagerAdapter);
            circlePageIndicator.setViewPager(viewPager);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    private void initEventRecyclerViewAdapter(final ArrayList<Item> arrItem) throws Exception {
        eventRecyclerViewAdapter = new EventRecyclerViewAdapter(arrItem);
        recyclerView.setAdapter(eventRecyclerViewAdapter);
        eventRecyclerViewAdapter.setOnCompleteLoading(new EventRecyclerViewAdapter.OnCompleteLoading() {
            @Override
            public void onComplete() {
                progressBar.hide();
            }
        });
        eventRecyclerViewAdapter.setOnItemClick(new EventRecyclerViewAdapter.OnItemClick() {
            @Override
            public void onClick(View view, int position) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), android.R.style.Theme_DeviceDefault_Light_Dialog_NoActionBar);
                builder.setMessage(arrItem.get(position).getE_info());
                builder.setTitle(arrItem.get(position).getE_date());
                builder.setCancelable(true);
                builder.create().show();
            }
        });
    }

    private void initNumberPickerViewPagerAdapter(int from, int to) {
        numberPickerViewPagerAdapter =
                new NumberPickerViewPagerAdapter(getFragmentManager(), from, to);
        viewPager.setAdapter(numberPickerViewPagerAdapter);
        numberPickerViewPagerAdapter.setOnItemClick(this);
    }

    private void onImageViewBackPress() throws Exception {
        switch (currentContent) {
            case CENTURY:
                initNumberPickerViewPagerAdapter(MONTH_START_AT, MONTH_END_AT);
                tvTitle.setText(MONTH);
                currentContent = MONTH;
                break;
            case YEAR:
                initNumberPickerViewPagerAdapter(CENTURY_START_AT, CENTURY_END_AT);
                tvTitle.setText(CENTURY);
                currentContent = CENTURY;

                break;
            case MONTH:
                int year_start_at = (centurySelected - 1) * 100;
                int year_end_at = centurySelected * 100;
                if (year_end_at > CurrentTime.getYear()) {
                    year_end_at = CurrentTime.getYear();
                }
                initNumberPickerViewPagerAdapter(year_start_at, year_end_at);
                tvTitle.setText(YEAR);
                currentContent = YEAR;
                break;
        }
        AnimationSet animationSet = (AnimationSet) AnimationUtils.loadAnimation(getContext(), R.anim.anim_set_left_to_right);
        tvTitle.startAnimation(animationSet);
    }

    private void onImageViewForwardPress() throws Exception {
        switch (currentContent) {
            case CENTURY:
                int year_start_at = (centurySelected - 1) * 100 + 1;
                int year_end_at = centurySelected * 100;
                if (year_end_at > CurrentTime.getYear()) {
                    year_end_at = CurrentTime.getYear();
                }
                initNumberPickerViewPagerAdapter(year_start_at, year_end_at);
                tvTitle.setText(YEAR);
                currentContent = YEAR;
                break;
            case YEAR:
                initNumberPickerViewPagerAdapter(MONTH_START_AT, MONTH_END_AT);
                tvTitle.setText(MONTH);
                currentContent = MONTH;
                break;
            case MONTH:
                tvTitle.setText(CENTURY);
                currentContent = CENTURY;
                initNumberPickerViewPagerAdapter(CENTURY_START_AT, CENTURY_END_AT);

                break;
        }
        AnimationSet animationSet = (AnimationSet) AnimationUtils.loadAnimation(getContext(), R.anim.anim_set_right_to_left);
        tvTitle.startAnimation(animationSet);
    }

    @Override
    public void onClick(View v) {
        try {
            switch (v.getId()) {
                case R.id.ivBack:
                    onImageViewBackPress();
                    break;
                case R.id.ivFoward:
                    onImageViewForwardPress();
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void notifyData() {
        if (eventRecyclerViewAdapter != null) {
            eventRecyclerViewAdapter.notifyDataSetChanged();
            arrItemTmp.clear();
            arrItemTmp.addAll(arrItem);
        }
    }

    @Override
    public void onClick(View view, int position) {
        try {

            TextView textView = (TextView) view.findViewById(R.id.tvNumber);
            switch (currentContent) {
                case CENTURY:
                    centurySelected = Integer.parseInt(textView.getText().toString());
                    yearSelected = 0;
                    monthSelected = 0;
                    break;
                case YEAR:
                    yearSelected = Integer.parseInt(textView.getText().toString());
                    break;
                case MONTH:
                    monthSelected = Integer.parseInt(textView.getText().toString());
                    break;
            }
            MainActivity activity = (MainActivity) getActivity();
            activity.initToolbar("C " + centurySelected + " / Y " + yearSelected + " / M " + monthSelected + " ");

            onImageViewForwardPress();
            arrItem.clear();
            for (int i = 0; i < arrItemTmp.size(); i++) {
                Item item = arrItemTmp.get(i);
                int year;
                if (!item.getE_year().toString().trim().equals("Không rõ")) {
                    year = Integer.parseInt(item.getE_year().toString().trim());
                } else {
                    year = 1;
                }
                int century;
                if (year % 100 == 0) {
                    century = year / 100;
                } else {
                    century = year / 100 + 1;
                }

                int month = Integer.parseInt(item.getE_month().toString().trim());
                if (century == centurySelected) {
                    if (year == yearSelected || yearSelected == 0) {
                        if (month == monthSelected || monthSelected == 0) {
                            arrItem.add(arrItemTmp.get(i));
                        }
                    }
                }
                eventRecyclerViewAdapter.notifyDataSetChanged();
            }
            if (currentContent == CENTURY) {

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
