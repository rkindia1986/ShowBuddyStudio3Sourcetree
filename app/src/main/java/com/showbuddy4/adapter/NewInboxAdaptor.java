package com.showbuddy4.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.showbuddy4.R;
import com.showbuddy4.activity.ProfileViewActivity;
import com.showbuddy4.fragment.OnImageClickListener;
import com.showbuddy4.model.InboxModelNew;

import java.util.ArrayList;

/**
 * Created by Jainam on 07-01-2018.
 */

public class NewInboxAdaptor extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements Filterable {

    private OnImageClickListener onImageClickListener;

    private ArrayList<InboxModelNew> mOriginalValues; // Original Values
    private ArrayList<InboxModelNew> mDisplayedValues;
    Context context;
    InboxModelNew model_inbox;
    private Filter planetFilter;

    public NewInboxAdaptor(ArrayList<InboxModelNew> mDisplayedValues, Context context,OnImageClickListener onImageClickListener) {
        this.mDisplayedValues = mDisplayedValues;
        this.mOriginalValues = mDisplayedValues;
        this.context = context;
        this.onImageClickListener = onImageClickListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_inbox, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {

        model_inbox = mDisplayedValues.get(position);
        ((ViewHolder) holder).txt_name.setText(model_inbox.getFirstName());
        ((ViewHolder) holder).txt_message.setText(model_inbox.getLastName());
        ((ViewHolder) holder).txt_time.setText(model_inbox.getCreatedDate());
        holder.itemView.setTag(position);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onImageClickListener.onImageClick(mDisplayedValues.get((Integer) v.getTag()));
            }
        });

        setProfilepic(mOriginalValues,((ViewHolder) holder),position);

        ((ViewHolder) holder).img_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String profile = "profile";
                String id = mOriginalValues.get(position).getFbProfileId();
                context.startActivity(new Intent(context,
                        ProfileViewActivity.class).putExtra("id", id).putExtra("profileview", profile));
            }
        });
     /*   Glide.with(context)
                .load(model_inbox.getImage())
                .asBitmap()
                .listener(new RequestListener<String, Bitmap>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<Bitmap> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Bitmap resource, String model, Target<Bitmap> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        return false;
                    }
                })
                .into(((ViewHolder) holder).img_user);*/

    }

    private void setProfilepic(ArrayList<InboxModelNew> mOriginalValues, ViewHolder holder, int position) {
    if(mOriginalValues.get(position).getFbUserPhotoUrl1()!=null){
        Glide.with(context)
                .load(mOriginalValues.get(position).getFbUserPhotoUrl1())
                .asBitmap()
                .placeholder(R.drawable.user_grey).error(R.drawable.user_grey)
                .into(holder.img_user);
    }else if(mOriginalValues.get(position).getFbUserPhotoUrl2()!=null){
        Glide.with(context)
                .load(mOriginalValues.get(position).getFbUserPhotoUrl2())
                .asBitmap()
                .placeholder(R.drawable.user_grey).error(R.drawable.user_grey)
                .into(holder.img_user);
    }
    else if(mOriginalValues.get(position).getFbUserPhotoUrl3()!=null){
        Glide.with(context)
                .load(mOriginalValues.get(position).getFbUserPhotoUrl3())
                .asBitmap()
                .placeholder(R.drawable.user_grey).error(R.drawable.user_grey)
                .into(holder.img_user);
    }
    else if(mOriginalValues.get(position).getFbUserPhotoUrl4()!=null){
        Glide.with(context)
                .load(mOriginalValues.get(position).getFbUserPhotoUrl4())
                .asBitmap()
                .placeholder(R.drawable.user_grey).error(R.drawable.user_grey)
                .into(holder.img_user);
    }
    else if(mOriginalValues.get(position).getFbUserPhotoUrl5()!=null){
        Glide.with(context)
                .load(mOriginalValues.get(position).getFbUserPhotoUrl5())
                .asBitmap()
                .placeholder(R.drawable.user_grey).error(R.drawable.user_grey)
                .into(holder.img_user);
    }
    else if(mOriginalValues.get(position).getFbUserPhotoUrl6()!=null){
        Glide.with(context)
                .load(mOriginalValues.get(position).getFbUserPhotoUrl6())
                .asBitmap()
                .placeholder(R.drawable.user_grey).error(R.drawable.user_grey)
                .into(holder.img_user);
    }

    }



    @Override
    public int getItemCount() {
        return mDisplayedValues.size();
    }

    @Override
    public Filter getFilter() {
        if (planetFilter == null)
            planetFilter = new PlanetFilter();

        return planetFilter;
    }

    private class PlanetFilter extends Filter {


        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();
            // We implement here the filter logic
            if (constraint == null || constraint.length() == 0) {
                // No filter implemented we return all the list
                results.values = mOriginalValues;
                results.count = mOriginalValues.size();
            } else {
                // We perform filtering operation
                ArrayList<InboxModelNew> nPlanetList = new ArrayList<>();

                for (InboxModelNew p : mDisplayedValues) {
                    if (p.getProfileName().toUpperCase().startsWith(constraint.toString().toUpperCase()))
                        nPlanetList.add(p);
                }

                results.values = nPlanetList;
                results.count = nPlanetList.size();
            }
            return results;
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint,
                                      FilterResults results) {

            mDisplayedValues = (ArrayList<InboxModelNew>) results.values;
            notifyDataSetChanged();

        }
    }

    public void resetData() {
        mDisplayedValues = mOriginalValues;
    }

    private class ViewHolder extends RecyclerView.ViewHolder {
        ImageView img_user;
        TextView txt_name, txt_message, txt_time, txt_total;

        public ViewHolder(View itemView) {
            super(itemView);
            img_user = (ImageView) itemView.findViewById(R.id.img_user);
            txt_name = (TextView) itemView.findViewById(R.id.txt_name);
            txt_message = (TextView) itemView.findViewById(R.id.txt_msg);
            txt_time = (TextView) itemView.findViewById(R.id.txt_time);

            /*itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    context.startActivity(new Intent(context, MessageActivity.class));
                }
            });*/

        }
    }

}

