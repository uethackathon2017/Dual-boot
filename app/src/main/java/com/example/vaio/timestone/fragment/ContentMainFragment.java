package com.example.vaio.timestone.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
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
import com.example.vaio.timestone.activity.WebviewActivity;
import com.example.vaio.timestone.adapter.EventRecyclerViewAdapter;
import com.example.vaio.timestone.adapter.NumberPickerViewPagerAdapter;
import com.example.vaio.timestone.database.Database;
import com.example.vaio.timestone.model.CurrentTime;
import com.example.vaio.timestone.model.Item;
import com.example.vaio.timestone.async_task.OnNumberPickerSelectedAsyncTask;
import com.example.vaio.timestone.async_task.RefreshDataAsyncTask;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
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
    public static final String TAG = "ContentMainFragment";
    public static final String LINK = "link";

    private RecyclerView recyclerView;
    private ImageView ivBack;
    private ImageView ivForward;
    private ViewPager viewPager;
    private TextView tvTitle, tvNotification;
    private EventRecyclerViewAdapter eventRecyclerViewAdapter;
    private NumberPickerViewPagerAdapter numberPickerViewPagerAdapter;
    private ContentLoadingProgressBar contentLoadingProgressBar;
    private CirclePageIndicator circlePageIndicator;

    private ArrayList<Item> arrItem = new ArrayList<>(); // mảng dữ liệu hiển thị chính
    private ArrayList<Item> arrItemTmp = new ArrayList<>(); // mảng nhớ tạm
    private String currentContent;
    private int centurySelected = 1; // khai báo biến với giá trị default century
    private int yearSelected = 0; // khai báo biến với giá trị default year
    private int monthSelected = 0; // khai báo biến với giá trị default month
    private DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
    private String stringIDData = "/";
    private SharedPreferences sharedPreferences;

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
            sharedPreferences = getContext().getSharedPreferences(QuizFragment.SHARE_PRE, Context.MODE_PRIVATE);
            stringIDData = sharedPreferences.getString(MainActivity.STRING_ID, "/");

            arrItemTmp.addAll(arrItem); // add tất cả phần tử của dữ liệu sang một mảng tạm
            contentLoadingProgressBar = (ContentLoadingProgressBar) view.findViewById(R.id.contentLoadingProgressBar);
            currentContent = CENTURY; // nội dung hiển thị hiện tại theo cent hoặc year hoặc month
            circlePageIndicator = (CirclePageIndicator) view.findViewById(R.id.circlePageIndicator);

            tvNotification = (TextView) view.findViewById(R.id.textViewNotification);
            tvTitle = (TextView) view.findViewById(R.id.tvDate);
            tvTitle.setText(currentContent); // set title mặc định
            recyclerView = (RecyclerView) view.findViewById(R.id.recyclerViewEvent);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
            initEventRecyclerViewAdapter(arrItem);
            //
            ivBack = (ImageView) view.findViewById(R.id.ivBack); // phím back
            ivForward = (ImageView) view.findViewById(R.id.ivFoward); // phím forward
            ivBack.setOnClickListener(this);
            ivForward.setOnClickListener(this);
            //
            viewPager = (ViewPager) view.findViewById(R.id.viewPager);
            numberPickerViewPagerAdapter = new NumberPickerViewPagerAdapter(getFragmentManager(), CENTURY_START_AT, CENTURY_END_AT);
            numberPickerViewPagerAdapter.setOnItemClick(this);
            viewPager.setAdapter(numberPickerViewPagerAdapter);
            circlePageIndicator.setViewPager(viewPager); // đồng bộ trạng thái viewpager và circle page indicator
            initNumberPickerViewPagerAdapter(CENTURY_START_AT, CENTURY_END_AT); // khởi tạo mặc định number picker theo thế kỉ
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void showDialogContent(final Context context, final String content, final int id) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context, android.R.style.Theme_DeviceDefault_Light_Dialog_NoActionBar);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.setNegativeButton("Xem sau", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (!stringIDData.contains("/" + id + "/")) {
                    stringIDData = stringIDData + id + "/";
                }
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(MainActivity.STRING_ID, stringIDData);
                editor.commit();
            }
        });
        builder.setMessage(content);
        builder.create().show();
    }

    private void initEventRecyclerViewAdapter(final ArrayList<Item> arrItem) throws Exception {
        // khởi tạo adapter event recycler view
        eventRecyclerViewAdapter = new EventRecyclerViewAdapter(arrItem);
        eventRecyclerViewAdapter.setOnItemLongClick(new EventRecyclerViewAdapter.OnItemLongClick() {
            @Override
            public void onClick(View view, int position) {
                showDialogContent(getContext(), arrItem.get(position).getE_info(), arrItem.get(position).getE_id());
            }
        });
        recyclerView.setAdapter(eventRecyclerViewAdapter);
        eventRecyclerViewAdapter.setOnCompleteLoading(new EventRecyclerViewAdapter.OnCompleteLoading() {
            @Override
            public void onComplete() {
                contentLoadingProgressBar.hide(); // ẩn progress bar khi đã load xong dữ liệu
            }
        });
        eventRecyclerViewAdapter.setOnItemClick(new EventRecyclerViewAdapter.OnItemClick() {
            @Override
            public void onClick(View view, int position) {
                final Item item = arrItem.get(position);
                item.setE_weight(item.getE_weight() + 1); // tăng mức độ ưu tiên của bản ghi dữ liệu theo lượt xem
                reference.child("item").child(item.getE_id() + "").child(Database.WEIGHT).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        int weight = dataSnapshot.getValue(Integer.class); // lấy độ ưu tiên từ firebase
                        Database database = new Database(getContext()); // cập nhật database để có thể sử dụng offile
                        database.updateWeight(item.getE_id(), weight); // cập nhật
                        RefreshDataAsyncTask refreshDataAsyncTask = new RefreshDataAsyncTask(getContext());
                        refreshDataAsyncTask.setOnComplete(new RefreshDataAsyncTask.OnComplete() {
                            @Override
                            public void onComplete(ArrayList<Item> arrItem) {
                                arrItemTmp.clear(); // làm mới mảng dữ liệu
                                arrItemTmp.addAll(arrItem); // add dữ liệu mới vào mảng
                            }
                        });
                        refreshDataAsyncTask.execute();

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                reference.child(MainActivity.ITEM).child(item.getE_id() + "").child(Database.WEIGHT).setValue(item.getE_weight());

                Intent intent = new Intent(getContext(), WebviewActivity.class);
                intent.putExtra(LINK, "http://www.google.com/search?btnI=I'm+Feeling+Lucky&q=" + arrItem.get(position).getE_info().trim()); //
                // Đường link tới nội dung
                startActivity(intent);
            }
        });
    }

    public void initNumberPickerViewPagerAdapter(int from, int to) {
        // Khởi tạo adapter cho viewpager
        numberPickerViewPagerAdapter = new NumberPickerViewPagerAdapter(getFragmentManager(), from, to);
        viewPager.setAdapter(numberPickerViewPagerAdapter);
        numberPickerViewPagerAdapter.setOnItemClick(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        initNumberPickerViewPagerAdapter(CENTURY_START_AT, CENTURY_END_AT);
    }

    private void onImageViewBackPress() {
        try {
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
                    int year_start_at = (centurySelected - 1) * 100; // tính năm bắt đầu
                    int year_end_at = centurySelected * 100; //  tính năm kết thúc
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
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private void onImageViewForwardPress() {
        try {

            switch (currentContent) {
                case CENTURY:
                    int year_start_at = (centurySelected - 1) * 100 + 1; //  tính năm bắt đầu
                    int year_end_at = centurySelected * 100; // tính năm kết thúc
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
            tvTitle.startAnimation(animationSet); // Hiệu ứng chuyển nội dung
        } catch (Exception e) {
            e.printStackTrace();
        }

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

    @Override
    public void onClick(View view, int position) {
        try {

            contentLoadingProgressBar.show();
            TextView textView = (TextView) view.findViewById(R.id.tvNumber);
            switch (currentContent) {
                case CENTURY:
                    centurySelected = Integer.parseInt(textView.getText().toString()); // set giá trị của thế kỉ đã chọn
                    yearSelected = 0; //
                    monthSelected = 0; //
                    break;
                case YEAR:
                    yearSelected = Integer.parseInt(textView.getText().toString());// set giá trị của năm đã chọn
                    break;
                case MONTH:
                    monthSelected = Integer.parseInt(textView.getText().toString()); //set giá trị của tháng đã chọn
                    break;

            }
            MainActivity activity = (MainActivity) getActivity();
            activity.initToolbar("C " + centurySelected + " / Y " + yearSelected + " / M " + monthSelected + " ");
            OnNumberPickerSelectedAsyncTask onNumberPickerSelectedAsyncTask = new OnNumberPickerSelectedAsyncTask(arrItemTmp, centurySelected, yearSelected, monthSelected);
            onNumberPickerSelectedAsyncTask.setOnSuccessfulLoadingData(new OnNumberPickerSelectedAsyncTask.OnSuccessfulLoadingData() {
                @Override
                public void onSuccess(ArrayList<Item> arrItemT) {
                    arrItem.clear();
                    arrItem.addAll(arrItemT);
                    if (arrItem.size() == 0) {
                        recyclerView.setVisibility(View.GONE);
                        tvNotification.setVisibility(View.VISIBLE);
                        tvNotification.setText("Không có sự kiện nào trong khoảng thời gian này");
                    } else {
                        recyclerView.setVisibility(View.VISIBLE);
                        tvNotification.setVisibility(View.GONE);
                    }
                    eventRecyclerViewAdapter.notifyDataSetChanged();
                    contentLoadingProgressBar.hide();
                    onImageViewForwardPress(); // tự động next sang nội dung mới

                }
            });
            onNumberPickerSelectedAsyncTask.execute();


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
