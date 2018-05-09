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

import com.showbuddy4.R;
import com.showbuddy4.activity.MessageActivity;
import com.showbuddy4.model.Model_Inbox;

import java.util.ArrayList;

/**
 * Created by Jainam on 07-01-2018.
 */

public class Inbox_Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements Filterable {



    private ArrayList<Model_Inbox> mOriginalValues; // Original Values
    private ArrayList<Model_Inbox> mDisplayedValues;
    Context context;
    Model_Inbox model_inbox;
    private Filter planetFilter;

    public Inbox_Adapter(ArrayList<Model_Inbox> mDisplayedValues, Context context) {
        this.mDisplayedValues = mDisplayedValues;
        this.mOriginalValues = mDisplayedValues;
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_inbox, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        model_inbox = mDisplayedValues.get(position);
        ((ViewHolder) holder).txt_name.setText(model_inbox.getName());
        ((ViewHolder) holder).txt_message.setText(model_inbox.getMsg());
        ((ViewHolder) holder).txt_time.setText(model_inbox.getTime());
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
                ArrayList<Model_Inbox> nPlanetList = new ArrayList<Model_Inbox>();

                for (Model_Inbox p : mDisplayedValues) {
                    if (p.getName().toUpperCase().startsWith(constraint.toString().toUpperCase()))
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

            mDisplayedValues = (ArrayList<Model_Inbox>) results.values;
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

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    context.startActivity(new Intent(context, MessageActivity.class));
                }
            });

        }
    }

}

