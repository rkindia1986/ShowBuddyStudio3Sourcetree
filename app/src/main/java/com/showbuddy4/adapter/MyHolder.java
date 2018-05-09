package com.showbuddy4.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.showbuddy4.R;
import com.showbuddy4.event.ItemClickListener;

/**
 * Created by Ashish on 2/17/2018.
 */

public class MyHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    //OUR VIEWS
    public ImageView img;
    public TextView nameTxt;
    public TextView posTxt;

    ItemClickListener itemClickListener;


    public MyHolder(View itemView) {
        super(itemView);

        this.img = (ImageView) itemView.findViewById(R.id.playerImage);
        this.nameTxt = (TextView) itemView.findViewById(R.id.nameTxt);
        this.posTxt = (TextView) itemView.findViewById(R.id.posTxt);

        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        this.itemClickListener.onItemClick(v, getLayoutPosition());

    }

    public void setItemClickListener(ItemClickListener ic) {
        this.itemClickListener = ic;
    }
}
