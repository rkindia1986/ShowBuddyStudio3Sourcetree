package com.showbuddy4.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.showbuddy4.R;
import com.showbuddy4.fragment.OnImageClickListener;
import com.showbuddy4.model.InboxModelNew;

import java.util.ArrayList;

/**
 * Created by User on 30-04-2018.
 */

public class HorizontalChatList extends RecyclerView.Adapter<HorizontalChatList.ViewHolder>  {

    private ArrayList<InboxModelNew> mOriginalValues; // Original Values
    private ArrayList<InboxModelNew>mtempVaues;

    Context context;
    InboxModelNew model_inbox;
    private OnImageClickListener onImageClickListener;
    private OnItemGetData onItemGetData;
    public interface OnItemGetData {
        void onItemGetData(InboxModelNew model);
    }
    public HorizontalChatList(ArrayList<InboxModelNew> mOriginalValues, Context context,OnImageClickListener onImageClickListener,OnItemGetData onItemGetData) {
        this.mOriginalValues = mOriginalValues;
        this.context = context;
        this.onImageClickListener = onImageClickListener;
        this.onItemGetData=onItemGetData;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.raw_horizontal, parent, false);
        return new HorizontalChatList.ViewHolder(v);    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        model_inbox = mOriginalValues.get(position);

        ((ViewHolder) holder).txt_name.setText(mOriginalValues.get(position).getFirstName());
        setProfilepic(mOriginalValues,((ViewHolder) holder),position);
        holder.itemView.setTag(position);

        ((ViewHolder) holder).itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                model_inbox=mOriginalValues.get(position);
                model_inbox.setChatwith("yes");
                mOriginalValues.set(position,model_inbox);

                onImageClickListener.onImageClick(mOriginalValues.get((Integer) v.getTag()));
onItemGetData.onItemGetData(mOriginalValues.get((Integer) v.getTag()));

mOriginalValues.remove(model_inbox);

            }
        });
    }

    @Override
    public int getItemCount() {
        return (mOriginalValues==null)?0:mOriginalValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView img_user;
        TextView txt_name;

        public ViewHolder(View itemView) {
            super(itemView);
            img_user = (ImageView) itemView.findViewById(R.id.img_user);
            txt_name = (TextView) itemView.findViewById(R.id.txt_name);


        }
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

}
