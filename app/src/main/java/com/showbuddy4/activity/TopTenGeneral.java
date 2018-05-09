package com.showbuddy4.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.widget.Toast;

import com.showbuddy4.DRAG.SimpleDividerItemDecoration;
import com.showbuddy4.R;
import com.showbuddy4.adapter.TopGeneralLIstAdapter;
import com.showbuddy4.dragndrop.OnStartDragListener;
import com.showbuddy4.model.GetTopTenListModel;
import com.showbuddy4.preference.PreferenceApp;
import com.showbuddy4.retrofit.ApiClient;
import com.showbuddy4.retrofit.ApiService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by User on 15-04-2018.
 */

public class TopTenGeneral extends AppCompatActivity {
    private ArrayList<GetTopTenListModel> itemDatas = new ArrayList<>();
    List<GetTopTenListModel>mDataList=null;
    private TopGeneralLIstAdapter adapter;
   // private DragDropAdapter adapter;
    //private AdapterGeneralLIst adapter;
    private RecyclerView rvItems;
   // private ItemTouchHelper mItemTouchHelper;
   ItemTouchHelper touchHelper;
    PreferenceApp pref;
    ProgressDialog progressDialog;
    private ItemTouchHelper mItemTouchHelper;
    ArrayList<GetTopTenListModel>listModels=new ArrayList<>();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activityy_topgeneral);
        pref = new PreferenceApp(this);
        rvItems = (RecyclerView)findViewById(R.id.recyclerview);
        getfullTopTenList("genres");
    }


    public void getfullTopTenList(final String getfrom) {
        progressDialog = new ProgressDialog(TopTenGeneral.this);
        progressDialog.setMessage("Please Wait...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        ApiService apiService = ApiClient.getClient().create(ApiService.class);

        Call<List<GetTopTenListModel>> call = apiService.GetTopTenList(pref.getFbId(), getfrom);

        call.enqueue(new Callback<List<GetTopTenListModel>>() {
            @Override
            public void onResponse(@NonNull Call<List<GetTopTenListModel>> call, @NonNull Response<List<GetTopTenListModel>> response) {
                List<GetTopTenListModel> list = response.body();

                try {
                    if (response.code() == 200) {

                        for (int i = 0; i < list.size(); i++) {
                            String edtNumber = response.body().get(i).getTextboxno();
                            String edtName = response.body().get(i).getName();
                            String edtTag = response.body().get(i).getArtistid();
                            mDataList=response.body();
                            rvItems.setHasFixedSize(true);
                           // ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallbackItemTouchHelper);

                            rvItems.addItemDecoration(new SimpleDividerItemDecoration(TopTenGeneral.this));

                            rvItems.setLayoutManager(new LinearLayoutManager(TopTenGeneral.this));

                            listModels.addAll(response.body());
                            adapter=new TopGeneralLIstAdapter(TopTenGeneral.this, new OnStartDragListener() {
                                @Override
                                public void onStartDrag(RecyclerView.ViewHolder viewHolder) {
                                    Log.e("in","start drag");

                                    mItemTouchHelper.startDrag(viewHolder);
                                    mItemTouchHelper.startSwipe(viewHolder);

                                }
                            }, listModels, new TopGeneralLIstAdapter.ICallback() {
                                @Override
                                public void onItemRemove(int position) {

                                }

                                @Override
                                public void onItemUndo(int position) {

                                }

                                @Override
                                public void onItemPositionChange(int fromPosition, int toPosition) {

                                    Toast.makeText(TopTenGeneral.this,"Item is moved from "+fromPosition+" to "+toPosition,Toast.LENGTH_LONG).show();
                                }
                            });
                              //adapter=new TopGeneralLIstAdapter(this,this,listModels,this);
//                            adapter = new AdapterGeneralLIst(TopTenGeneral.this, listModels);
                            rvItems.setAdapter(adapter);
  //                          itemTouchHelper.attachToRecyclerView(rvItems);

//                            ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(TopTenGeneral.this,adapter);
//                            mItemTouchHelper = new ItemTouchHelper(callback);
//                            mItemTouchHelper.attachToRecyclerView(rvItems);

//                            ItemTouchHelper itemTouchHelper = new ItemTouchHelper(mIthCallback);
//                            itemTouchHelper.attachToRecyclerView(rvItems);

                            //rvItems.setHasFixedSize(true);
                            //adapter = new TopGeneralLIstAdapter(this, this,  body,this);

//                            rvItems.setAdapter(adapter);
//                            adapter.notifyDataSetChanged();
//                            setAdapter(response.body());
//
//                            ItemTouchHelper.Callback callback = new com.showbuddy2.DRAG.SimpleItemTouchHelperCallback(adapter);
//                           touchHelper= new ItemTouchHelper(callback);
//                            touchHelper.attachToRecyclerView(rvItems);

//                            ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(TopTenGeneral.this,adapter);
//                            mItemTouchHelper = new ItemTouchHelper(callback);
//                            mItemTouchHelper.attachToRecyclerView(rvItems);

                        }

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                progressDialog.dismiss();

            }

            @Override
            public void onFailure(@NonNull Call<List<GetTopTenListModel>> call, @NonNull Throwable t) {
                progressDialog.dismiss();
            }
        });
    }
    ItemTouchHelper.Callback mIthCallback = new ItemTouchHelper.Callback() {

        @Override
        public boolean isLongPressDragEnabled() {
            return super.isLongPressDragEnabled();
        }

        // method for getting the direction of movements
        @Override
        public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
            int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN; // for drag and drop
            int swipeFlags = ItemTouchHelper.START | ItemTouchHelper.END; // for swipe left and right
            return makeMovementFlags(dragFlags, swipeFlags);
        }
        // method for drag and drop
        @Override
        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
            Collections.swap(mDataList, viewHolder.getAdapterPosition(), target.getAdapterPosition());
            adapter.notifyItemMoved(viewHolder.getAdapterPosition(), target.getAdapterPosition());
            //adapter.notifyDataSetChanged();
            return true;
        }
        // method for swiping left and right
        @Override
        public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
            //mDataList.remove(viewHolder.getAdapterPosition());
            //mAdapter.notifyItemRemoved(viewHolder.getAdapterPosition());
        }
    };


    ItemTouchHelper.SimpleCallback simpleCallbackItemTouchHelper = new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN, ItemTouchHelper.RIGHT)
    {

        @Override
        public boolean isLongPressDragEnabled() {
            //return super.isLongPressDragEnabled();
            return true;
        }


        @Override
        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {

            final int fromPosition = viewHolder.getAdapterPosition();
            final int toPosition = target.getAdapterPosition();
            if (viewHolder.getItemViewType() != target.getItemViewType()) {
                return false;
            }
//            GetTopTenListModel prev = listModels.remove(fromPosition);
//            listModels.add(toPosition > fromPosition ? toPosition - 1 : toPosition, prev);
//            adapter.notifyItemMoved(fromPosition, toPosition);

            adapter.notifyItemMoved(fromPosition, toPosition);
//            adapter.notifyItemChanged(fromPosition);
//            adapter.notifyItemChanged(toPosition);
            return true;
        }

        @Override
        public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
            int position = viewHolder.getAdapterPosition();
//            mDataList.remove(position);
//            adapter.notifyDataSetChanged();
        }

        @Override
        public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {

            super.clearView(recyclerView, viewHolder);
            viewHolder.itemView.setAlpha(1.0f);


        }

    };


    private void setAdapter(List<GetTopTenListModel> body) {
//        adapter = new TopGeneralLIstAdapter(this, this,  body,this);
//        rvItems.setAdapter(adapter);
//        adapter.notifyDataSetChanged();
    }


}
