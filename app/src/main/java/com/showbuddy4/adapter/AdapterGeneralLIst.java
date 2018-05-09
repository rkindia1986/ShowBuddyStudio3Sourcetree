package com.showbuddy4.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.showbuddy4.R;
import com.showbuddy4.model.GetTopTenListModel;

import java.util.ArrayList;

/**
 * Created by User on 15-04-2018.
 */

public class AdapterGeneralLIst extends RecyclerView.Adapter<AdapterGeneralLIst.ViewHolder> {
    private ArrayList<GetTopTenListModel> mDataList;
    private Context context;

    public AdapterGeneralLIst(Context mainActivity, ArrayList<GetTopTenListModel> mDataList) {
        this.context = mainActivity;
        this.mDataList = mDataList;
    }
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView mTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);

            mTextView = (TextView) itemView.findViewById(R.id.textview);
        }

        @Override
        public void onClick(View view) {

        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.raw_drag_drop, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.mTextView.setText(mDataList.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }


}
