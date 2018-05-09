package com.showbuddy4.activity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.showbuddy4.R;
import com.showbuddy4.adapter.MyHolder;
import com.showbuddy4.event.ItemClickListener;
import com.showbuddy4.model.CustomFilter;
import com.showbuddy4.model.GetTopTenModel;
import com.showbuddy4.model.SearchDataNew;
import com.showbuddy4.retrofit.ApiClient;
import com.showbuddy4.retrofit.ApiService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Ashish on 2/17/2018.
 */

public class NewSearchListActivity extends AppCompatActivity {
    SearchView sv;
    RecyclerView rv;
    ProgressDialog progressDialog;
    private RecyclerView.LayoutManager mLayoutManager;
    String titlename = "";
    String id = "";
    String name = "";

    String strPosition = "";
    String strCatTitle = "";

    private Context context;

    ArrayList<SearchDataNew> listall = new ArrayList<>();


    MyAdapter adapter;
    ImageView img_back;
    TextView titleTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newsearchlist);
        context = NewSearchListActivity.this;
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        sv = (SearchView) findViewById(R.id.mSearch);
        rv = (RecyclerView) findViewById(R.id.myRecycler);

        titleTV = (TextView) findViewById(R.id.titleTV);
        img_back = (ImageView) findViewById(R.id.img_back);

        Intent i = getIntent();

        if (i.hasExtra("Category_Title")) {
            strCatTitle = i.getStringExtra("Category_Title");
            titleTV.setText(strCatTitle);

            if (strCatTitle.equals("Top 10 Solo Artists")) {
                getTopTenList("", "solo_artists");
            } else if (strCatTitle.equals("Top 10 Live Acts")) {
                getTopTenList("", "live_acts");
            } else if (strCatTitle.equals("Top 10 Bands")) {
                getTopTenList("", "bands_groups");
            } else if (strCatTitle.equals("Top 10 Emerging Acts")) {
                getTopTenList("", "emerging_acts");
            } else if (strCatTitle.equals("Top 15 Genres")) {
                getTopTenList("", "genres");
            }
        }
        strPosition = i.getStringExtra("POSITION");

        //SEARCH
        sv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                //FILTER AS YOU TYPE
                /*adapter.getFilter().filter(query);*/
                listall.clear();
                if (strCatTitle.equals("Top 10 Solo Artists")) {
                    getTopTenList(query, "solo_artists");
                } else if (strCatTitle.equals("Top 10 Live Acts")) {
                    getTopTenList(query, "live_acts");
                } else if (strCatTitle.equals("Top 10 Bands")) {
                    getTopTenList(query, "bands_groups");
                } else if (strCatTitle.equals("Top 10 Emerging Acts")) {
                    getTopTenList(query, "emerging_acts");
                } else if (strCatTitle.equals("Top 15 Genres")) {
                    getTopTenList(query, "genres");
                }
                return false;
            }
        });

        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }


       /*p.setName("Ander Herera");
        players.add(p);

        p=new SearchDataNew();
        p.setName("David De Geaa");
        players.add(p);

        p=new SearchDataNew();
        p.setName("Michael Carrick");
        players.add(p);

        p=new SearchDataNew();
        p.setName("Juan Mata");
        players.add(p);

        p=new SearchDataNew();
        p.setName("Diego Costa");
        players.add(p);

        p=new SearchDataNew();
        p.setName("Oscar");
        players.add(p);*/


    public void getTopTenList(String search, String searchfrom) {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please Wait...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        ApiService apiService = ApiClient.getClient().create(ApiService.class);

        Call<List<GetTopTenModel>> call = apiService.GetTopList(search, searchfrom);

        call.enqueue(new Callback<List<GetTopTenModel>>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(@NonNull Call<List<GetTopTenModel>> call, @NonNull Response<List<GetTopTenModel>> response) {
                try {
                    List<GetTopTenModel> list = response.body();
                    if (response.code() == 200) {
                        progressDialog.dismiss();

                        if (list.size() > 0) {
                            for (int i = 0; i < list.size(); i++) {
                                SearchDataNew searchdata = new SearchDataNew();
                                searchdata.setName(list.get(i).getName());
                                searchdata.setId(list.get(i).getId());
                                searchdata.setThumb_url(list.get(i).getThumb_url());
                                listall.add(searchdata);
                            }

                        }

                        //SET ITS PROPETRIES
                        mLayoutManager = new LinearLayoutManager(context);
                        rv.setLayoutManager(mLayoutManager);
                        rv.setItemAnimator(new DefaultItemAnimator());

                        //ADAPTER
                        adapter = new MyAdapter(context, listall);
                        rv.setAdapter(adapter);


                    }


                } catch (Exception e) {
                    e.printStackTrace();

                    listall.clear(); //clear list
                    adapter.notifyDataSetChanged(); //let your adapter know about the changes and reload view.

                }
            }

            @Override
            public void onFailure(@NonNull Call<List<GetTopTenModel>> call, @NonNull Throwable t) {
                progressDialog.dismiss();
                listall.clear(); //clear list
                adapter.notifyDataSetChanged(); //let your adapter know about the changes and reload view.


            }
        });
    }

    public class MyAdapter extends RecyclerView.Adapter<MyHolder> implements Filterable {

        Context c;
        public ArrayList<SearchDataNew> players;
        ArrayList<SearchDataNew> filterList;
        CustomFilter filter;


        public MyAdapter(Context ctx, ArrayList<SearchDataNew> players) {
            this.c = ctx;
            this.players = players;
            this.filterList = players;
        }


        @Override
        public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            //CONVERT XML TO VIEW ONBJ
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_item_new, null);

            //HOLDER
            MyHolder holder = new MyHolder(v);

            return holder;
        }

        //DATA BOUND TO VIEWS
        @Override
        public void onBindViewHolder(MyHolder holder, int position) {

            //BIND DATA
            /*holder.posTxt.setText(players.get(position).getId());*/
            holder.nameTxt.setText(players.get(position).getName());

            TextDrawable.IBuilder mDrawableBuilder;
            mDrawableBuilder = TextDrawable.builder()
                    .beginConfig()
                    .withBorder(4)
                    .endConfig()
                    .round();


            TextDrawable drawable = mDrawableBuilder.build(String.valueOf(players.get(position).getName().charAt(0)), getResources().getColor(R.color.colorPrimary));
            //holder.img.setImageDrawable(drawable);

            Glide.with(context)
                    .load(filterList.get(position).getThumb_url())
                    .placeholder(drawable)
                    .error(drawable)

                    .bitmapTransform(new CircleTransform(NewSearchListActivity.this))

                    .listener(new RequestListener<String, GlideDrawable>() {
                       @Override
                       public boolean onException(Exception e, String model, com.bumptech.glide.request.target.Target<GlideDrawable> target, boolean isFirstResource) {
                           Log.e("TAG", "Error loading image", e);

                           return false;
                       }

                       @Override
                       public boolean onResourceReady(GlideDrawable resource, String model, com.bumptech.glide.request.target.Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                           return false;
                       }
                   })
                   .into(holder.img);
            //IMPLEMENT CLICK LISTENET
            holder.setItemClickListener(new ItemClickListener() {
                @Override
                public void onItemClick(View v, int pos) {
                    Snackbar.make(v, players.get(pos).getId(), Snackbar.LENGTH_SHORT).show();
                    id = players.get(pos).getId();
                    name = players.get(pos).getName();

                    Intent in = getIntent();
                    in.putExtra("id", id);
                    in.putExtra("name", name);
                    in.putExtra("POSITION", strPosition);
                    in.putExtra("Category_Title", strCatTitle);
                    setResult(111, in);
                    finish();


                }
            });

        }

        //GET TOTAL NUM OF PLAYERS
        @Override
        public int getItemCount() {
            return players.size();
        }

        //RETURN FILTER OBJ
        @Override
        public Filter getFilter() {
            if (filter == null) {
                filter = new CustomFilter(filterList, this);
            }

            return filter;
        }
    }

    static class CircleTransform  extends BitmapTransformation {
        public CircleTransform(Context context) {
            super(context);
        }

        @Override protected Bitmap transform(BitmapPool pool, Bitmap toTransform, int outWidth, int outHeight) {
            return circleCrop(pool, toTransform);
        }

        private static Bitmap circleCrop(BitmapPool pool, Bitmap source) {
            if (source == null) return null;

            int size = Math.min(source.getWidth(), source.getHeight());
            int x = (source.getWidth() - size) / 2;
            int y = (source.getHeight() - size) / 2;

            // TODO this could be acquired from the pool too
            Bitmap squared = Bitmap.createBitmap(source, x, y, size, size);

            Bitmap result = pool.get(size, size, Bitmap.Config.ARGB_8888);
            if (result == null) {
                result = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888);
            }

            Canvas canvas = new Canvas(result);
            Paint paint = new Paint();
            paint.setShader(new BitmapShader(squared, BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP));
            paint.setAntiAlias(true);
            float r = size / 2f;
            canvas.drawCircle(r, r, r, paint);
            return result;
        }

        @Override public String getId() {
            return getClass().getName();
        }
    }

}
