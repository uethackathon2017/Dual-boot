package com.example.vaio.timestone.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.vaio.timestone.R;
import com.example.vaio.timestone.activity.MainActivity;
import com.example.vaio.timestone.activity.SearchActivity;
import com.example.vaio.timestone.activity.WebviewActivity;
import com.example.vaio.timestone.adapter.EventRecyclerViewAdapter;
import com.example.vaio.timestone.async_task.RefreshDataAsyncTask;
import com.example.vaio.timestone.database.Database;
import com.example.vaio.timestone.model.Item;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static com.example.vaio.timestone.fragment.ContentMainFragment.LINK;
import static com.example.vaio.timestone.fragment.ContentMainFragment.TAG;

/**
 * Created by vaio on 12/03/2017.
 */

public class WatchLaterFragment extends Fragment {
    private ArrayList<Item> arrItem;
    private RecyclerView recyclerView;
    private EventRecyclerViewAdapter eventRecyclerViewAdapter;
    private DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

    public WatchLaterFragment(ArrayList<Item> arrItem) {
        this.arrItem = arrItem;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        inflater = LayoutInflater.from(getContext());
        View view = inflater.inflate(R.layout.fragment_watch_later, container, false);
        initViews(view);
        return view;
    }

    private void initViews(View view) {

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        eventRecyclerViewAdapter = new EventRecyclerViewAdapter(arrItem);
        if (arrItem.size() == 0){
            Toast.makeText(getContext(), "Chưa có sự kiện nào được lưu", Toast.LENGTH_SHORT).show();
        }
        recyclerView.setAdapter(eventRecyclerViewAdapter);
        eventRecyclerViewAdapter.setOnItemClick(new EventRecyclerViewAdapter.OnItemClick() {
            @Override
            public void onClick(View view, int position) {
                final Item item = arrItem.get(position);
                item.setE_weight(item.getE_weight() + 1);
                reference.child("item").child(item.getE_id() + "").child(Database.WEIGHT).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        int weight = dataSnapshot.getValue(Integer.class);
                        Database database = new Database(getContext());
                        database.updateWeight(item.getE_id(), weight);
                        RefreshDataAsyncTask refreshDataAsyncTask = new RefreshDataAsyncTask(getContext());
                        refreshDataAsyncTask.setOnComplete(new RefreshDataAsyncTask.OnComplete() {
                            @Override
                            public void onComplete(ArrayList<Item> arrItem) {
//                                arrItemTmp.clear();
//                                arrItemTmp.addAll(arrItem);

                            }
                        });
                        refreshDataAsyncTask.execute();

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                reference.child("item").child(item.getE_id() + "").child(Database.WEIGHT).setValue(item.getE_weight());

                Intent intent = new Intent(getContext(), WebviewActivity.class);
                intent.putExtra(LINK, "http://www.google.com/search?btnI=I'm+Feeling+Lucky&q=" + arrItem.get(position).getE_info().trim()); //
                // Đường link tới nội dung
                startActivity(intent);
            }
        });
        eventRecyclerViewAdapter.setOnItemLongClick(new EventRecyclerViewAdapter.OnItemLongClick() {
            @Override
            public void onClick(View view, int position) {
                SharedPreferences sharedPreferences = getContext().getSharedPreferences(QuizFragment.SHARE_PRE, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                String s = sharedPreferences.getString(MainActivity.STRING_ID,"");
                int id = arrItem.get(position).getE_id();
                Log.e(TAG, s);
                s.replaceAll("/"+id+"/","/");
                Log.e(TAG, s);
                editor.putString(MainActivity.STRING_ID,s);
                editor.commit();
                arrItem.remove(position);
                eventRecyclerViewAdapter.notifyDataSetChanged();
            }
        });
    }
}
