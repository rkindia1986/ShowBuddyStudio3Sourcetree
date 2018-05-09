package com.showbuddy4.FinalDragDrop;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.showbuddy4.R;
import com.showbuddy4.Utils.CommonUses;
import com.showbuddy4.model.GetTopTenListModel;
import com.showbuddy4.model.GetTopTenModel;
import com.showbuddy4.model.SwipeAction;
import com.showbuddy4.preference.PreferenceApp;
import com.showbuddy4.retrofit.ApiClient;
import com.showbuddy4.retrofit.ApiService;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by User on 16-04-2018.
 */

public class ActiivityDragDropList extends AppCompatActivity implements OnStartDragListener{
    List<GetTopTenListModel> mDataList=new ArrayList<>();
    PreferenceApp pref;
    ProgressDialog progressDialog;
    private ItemTouchHelper mItemTouchHelper;
    RecyclerView mRecyclerView;
    LinearLayoutManager linearLayoutManager =null;
    DataRecyclerViewAdapter dataRecyclerViewAdapter;
    ImageView img_back;
    TextView tvDone;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activityy_topgeneral);
        pref=new PreferenceApp(this);
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        img_back=(ImageView)findViewById(R.id.img_back);
        tvDone=(TextView)findViewById(R.id.txt_update_profile);
        linearLayoutManager= new LinearLayoutManager(ActiivityDragDropList.this, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(linearLayoutManager);

        getfullTopTenList("genres");
        tvDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(dataRecyclerViewAdapter!=null && dataRecyclerViewAdapter.getItemCount()>0){
                    updateApi("genres");
                }
            }
        });

        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void updateApi(String genres) {
        ApiService apiService =
                ApiClient.getClient().create(ApiService.class);

        if(dataRecyclerViewAdapter.getUpdateList()!=null){
            progressDialog = new ProgressDialog(ActiivityDragDropList.this);
            progressDialog.setMessage("Please Wait...");
            progressDialog.setCancelable(false);
            progressDialog.show();


            Call<SwipeAction> call = apiService.InsertUserRatingGenres(pref.getFbId(),genres,
                    dataRecyclerViewAdapter.getUpdateList().get(0).getArtistid(),
                    dataRecyclerViewAdapter.getUpdateList().get(1).getArtistid(),dataRecyclerViewAdapter.getUpdateList().get(2).getArtistid(),
                    dataRecyclerViewAdapter.getUpdateList().get(3).getArtistid(),dataRecyclerViewAdapter.getUpdateList().get(4).getArtistid(),
                    dataRecyclerViewAdapter.getUpdateList().get(5).getArtistid(),dataRecyclerViewAdapter.getUpdateList().get(6).getArtistid(),
                    dataRecyclerViewAdapter.getUpdateList().get(7).getArtistid(),dataRecyclerViewAdapter.getUpdateList().get(8).getArtistid(),
                    dataRecyclerViewAdapter.getUpdateList().get(9).getArtistid(),dataRecyclerViewAdapter.getUpdateList().get(9).getArtistid(),
                    dataRecyclerViewAdapter.getUpdateList().get(11).getArtistid(),dataRecyclerViewAdapter.getUpdateList().get(12).getArtistid(),
                    dataRecyclerViewAdapter.getUpdateList().get(13).getArtistid(),dataRecyclerViewAdapter.getUpdateList().get(14).getArtistid());
            call.enqueue(new Callback<SwipeAction>() {
                @Override
                public void onResponse(Call<SwipeAction> call, Response<SwipeAction> response) {
                    try {
                        if (response.code() == 200) {
                            CommonUses.showToast(ActiivityDragDropList.this, "List updated successfully");
                            finish();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<SwipeAction> call, Throwable t) {
                    progressDialog.dismiss();

                }
            });
        }else {
Log.e("in","no record ");
        }


    }

//    private void updateApi(String genres) {
//        ApiService apiService =
//                ApiClient.getClient().create(ApiService.class);
//        Map<String,String>map=new HashMap<>();
//        int index=1;
//        if(dataRecyclerViewAdapter.getUpdateList()!=null){
//            progressDialog = new ProgressDialog(ActiivityDragDropList.this);
//            progressDialog.setMessage("Please Wait...");
//            progressDialog.setCancelable(false);
//            progressDialog.show();
//
//            for (int i=0;i<dataRecyclerViewAdapter.getUpdateList().size();i++){
//                map.put(String.valueOf(index),dataRecyclerViewAdapter.getUpdateList().get(i).getArtistid());
//                Log.e("index",index+"--"+dataRecyclerViewAdapter.getUpdateList().get(i).getName()+" "+
//                        dataRecyclerViewAdapter.getUpdateList().get(i).getArtistid());
//
//            }
//            index++;
//            Call<SwipeAction> call = apiService.UpdateTopgenrel(pref.getFbId(),genres,map);
//            call.enqueue(new Callback<SwipeAction>() {
//                @Override
//                public void onResponse(Call<SwipeAction> call, Response<SwipeAction> response) {
//                    try {
//                        if (response.code() == 200) {
//                            CommonUses.showToast(ActiivityDragDropList.this, "List updated successfully");
//                            finish();
//                        }
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                }
//
//                @Override
//                public void onFailure(Call<SwipeAction> call, Throwable t) {
//                    progressDialog.dismiss();
//
//                }
//            });
//        }else {
//
//        }
//
//
//    }

    public void getfullTopTenList(final String getfrom)
    {
        progressDialog = new ProgressDialog(ActiivityDragDropList.this);
        progressDialog.setMessage("Processing...");
        progressDialog.setCancelable(true);
        progressDialog.show();

        ApiService apiService = ApiClient.getClient().create(ApiService.class);

        Call<List<GetTopTenListModel>> call = apiService.GetTopTenList(pref.getFbId(), getfrom);

        call.enqueue(new Callback<List<GetTopTenListModel>>() {
            @Override
            public void onResponse(@NonNull Call<List<GetTopTenListModel>> call, @NonNull Response<List<GetTopTenListModel>> response) {
                List<GetTopTenListModel> list = response.body();
if(response.body()==null){
    Log.e("in","null response load 2nd api");
    //loadFirstTimelist(getfrom);
    getTopTenList("",getfrom,progressDialog);
  //  progressDialog.dismiss();
}
else {
    try {
        if (response.code() == 200) {

            for (int i = 0; i < list.size(); i++) {
                String edtNumber = response.body().get(i).getTextboxno();
                String edtName = response.body().get(i).getName();
                String edtTag = response.body().get(i).getArtistid();
                mDataList=response.body();

            }
            dataRecyclerViewAdapter= new DataRecyclerViewAdapter(ActiivityDragDropList.this, mDataList,new ActiivityDragDropList());

            ItemTouchHelper.Callback callback =
                    new RecyclerViewItemTouchHelperCallback(dataRecyclerViewAdapter);
            mItemTouchHelper = new ItemTouchHelper(callback);
            mItemTouchHelper.attachToRecyclerView(mRecyclerView);
            // dataRecyclerViewAdapter.delegate=this;

            mRecyclerView.setAdapter(dataRecyclerViewAdapter);
            dataRecyclerViewAdapter.notifyDataSetChanged();

        }
    } catch (Exception e) {
        e.printStackTrace();
    }

    progressDialog.dismiss();

}


            }

            @Override
            public void onFailure(@NonNull Call<List<GetTopTenListModel>> call, @NonNull Throwable t) {
                progressDialog.dismiss();
            }
        });
    }

//    private void loadFirstTimelist(String getfrom) {
//
//        progressDialog = new ProgressDialog(ActiivityDragDropList.this);
//        progressDialog.setMessage("Please Wait...");
//        progressDialog.setCancelable(true);
//        progressDialog.show();
//
//        Gson gson = new GsonBuilder().serializeNulls().create();
//        ApiService apiService = ApiClient.getClient().create(ApiService.class);
//
//        Call<List<GetTopFirstTime>> call = apiService.GetFirstTopList(pref.getFbId(), getfrom);
//
//        call.enqueue(new Callback<List<GetTopFirstTime>>() {
//            @Override
//            public void onResponse(Call<List<GetTopFirstTime>> call, Response<List<GetTopFirstTime>> response) {
//                Log.e("res",response.body().toString()+"<<");
//                if(response.code()==200){
// GetTopTenListModel listModel=new GetTopTenListModel();
//                    try {
//                        for (int i=0;i<response.body().size();i++){
//                            listModel.setArtistid(response.body().get(i).getId());
//                            listModel.setName(response.body().get(i).getName());
//                            mDataList.add(listModel);
//                        }
//
//
//                        dataRecyclerViewAdapter= new DataRecyclerViewAdapter(ActiivityDragDropList.this, mDataList,new ActiivityDragDropList());
//
//                        ItemTouchHelper.Callback callback =
//                                new RecyclerViewItemTouchHelperCallback(dataRecyclerViewAdapter);
//                        mItemTouchHelper = new ItemTouchHelper(callback);
//                        mItemTouchHelper.attachToRecyclerView(mRecyclerView);
//                        // dataRecyclerViewAdapter.delegate=this;
//
//                        mRecyclerView.setAdapter(dataRecyclerViewAdapter);
//                        dataRecyclerViewAdapter.notifyDataSetChanged();
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//
//                }
//                progressDialog.dismiss();
//
//            }
//
//            @Override
//            public void onFailure(Call<List<GetTopFirstTime>> call, Throwable t) {
//                progressDialog.dismiss();
//            }
//        });
//
//
//
//    }



    private void loadFirstTimelist(String getfrom) {

        progressDialog = new ProgressDialog(ActiivityDragDropList.this);
        progressDialog.setMessage("Please Wait...");
        progressDialog.setCancelable(true);
        progressDialog.show();

        Gson gson = new GsonBuilder().serializeNulls().create();
        ApiService apiService = ApiClient.getClient().create(ApiService.class);

        Call<JSONArray> call = apiService.GetFirstTopList(pref.getFbId(), getfrom);

        call.enqueue(new Callback<JSONArray>() {
            @Override
            public void onResponse(Call<JSONArray> call, Response<JSONArray> response) {
                Log.e("res",response.body().toString()+"<<");

                try {
                    Log.e("in res array",response.body().getJSONObject(0).toString()+"<");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
//                if(response.code()==200){
//                    GetTopTenListModel listModel=new GetTopTenListModel();
//                    try {
//                        for (int i=0;i<response.body().size();i++){
//                            listModel.setArtistid(response.body().get(i).getId());
//                            listModel.setName(response.body().get(i).getName());
//                            mDataList.add(listModel);
//                        }
//
//
//                        dataRecyclerViewAdapter= new DataRecyclerViewAdapter(ActiivityDragDropList.this, mDataList,new ActiivityDragDropList());
//
//                        ItemTouchHelper.Callback callback =
//                                new RecyclerViewItemTouchHelperCallback(dataRecyclerViewAdapter);
//                        mItemTouchHelper = new ItemTouchHelper(callback);
//                        mItemTouchHelper.attachToRecyclerView(mRecyclerView);
//                        // dataRecyclerViewAdapter.delegate=this;
//
//                        mRecyclerView.setAdapter(dataRecyclerViewAdapter);
//                        dataRecyclerViewAdapter.notifyDataSetChanged();
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }

//                }
                progressDialog.dismiss();

            }

            @Override
            public void onFailure(Call<JSONArray> call, Throwable t) {
                progressDialog.dismiss();
            }
        });



    }




    public void getTopTenList(String search, String searchfrom, final ProgressDialog pd) {
//        progressDialog = new ProgressDialog(this);
//        progressDialog.setMessage("Please Wait...");
//        progressDialog.setCancelable(true);
//        progressDialog.show();

        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        //Call<JSONArray> call = apiService.GetFirstTopList(pref.getFbId(), getfrom);

        Call<List<GetTopTenModel>> call = apiService.GetFirTopList(pref.getFbId(), searchfrom);

        call.enqueue(new Callback<List<GetTopTenModel>>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(@NonNull Call<List<GetTopTenModel>> call, @NonNull Response<List<GetTopTenModel>> response) {
                try {

                    List<GetTopTenModel> list = response.body();
                    if (response.code() == 200) {
                        if(pd!=null && pd.isShowing()){
                            pd.dismiss();
                        }

                        if (list.size() > 0) {
                            for (int i = 0; i < list.size(); i++) {
                                GetTopTenListModel listModel=new GetTopTenListModel();
                                listModel.setName(list.get(i).getName());
                                listModel.setArtistid(list.get(i).getId());
                                mDataList.add( listModel);
                                Log.e("in list",list.get(i).getName()+"<<");
                              //  mDataList.set(listModel.setName(list.get(i).getName()));
//                                SearchDataNew searchdata = new SearchDataNew();
//                                searchdata.setName(list.get(i).getName());
//                                searchdata.setId(list.get(i).getId());
//                                listall.add(searchdata);
                            }
                            //mDataList.add(listModel);


                          }
                        dataRecyclerViewAdapter= new DataRecyclerViewAdapter(ActiivityDragDropList.this, mDataList,new ActiivityDragDropList());

                        ItemTouchHelper.Callback callback =
                                new RecyclerViewItemTouchHelperCallback(dataRecyclerViewAdapter);
                        mItemTouchHelper = new ItemTouchHelper(callback);
                        mItemTouchHelper.attachToRecyclerView(mRecyclerView);
                        // dataRecyclerViewAdapter.delegate=this;
                        mRecyclerView.setAdapter(dataRecyclerViewAdapter);
                        dataRecyclerViewAdapter.notifyDataSetChanged();


                        //SET ITS PROPETRIES
//                        mLayoutManager = new LinearLayoutManager(context);
//                        rv.setLayoutManager(mLayoutManager);
//                        rv.setItemAnimator(new DefaultItemAnimator());
//
//                        //ADAPTER
//                        adapter = new NewSearchListActivity.MyAdapter(context, listall);
//                        rv.setAdapter(adapter);


                    }
                    if(pd!=null && pd.isShowing()){
                        pd.dismiss();
                    }



                } catch (Exception e) {
                    e.printStackTrace();
                    if(pd!=null && pd.isShowing()){
                        pd.dismiss();
                    }
                    //
mDataList.clear();
dataRecyclerViewAdapter.notifyDataSetChanged();
//                    listall.clear(); //clear list
//                    adapter.notifyDataSetChanged(); //let your adapter know about the changes and reload view.

                }
            }

            @Override
            public void onFailure(@NonNull Call<List<GetTopTenModel>> call, @NonNull Throwable t) {
                if(pd!=null && pd.isShowing()){
                    pd.dismiss();
                }
                mDataList.clear();
                dataRecyclerViewAdapter.notifyDataSetChanged();

//                listall.clear(); //clear list
//                adapter.notifyDataSetChanged(); //let your adapter know about the changes and reload view.


            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(progressDialog!=null && progressDialog.isShowing()){
            progressDialog.dismiss();
        }
    }

    @Override
    public void onStartDrag(RecyclerView.ViewHolder viewHolder) {
        if (mItemTouchHelper!=null){
            Log.e("in","Ac onstart");
            mItemTouchHelper.startDrag(viewHolder);
        }

    }
}
