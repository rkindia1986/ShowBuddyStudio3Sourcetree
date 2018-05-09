package com.showbuddy4.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.showbuddy4.R;
import com.showbuddy4.model.Model_Message;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Jainam on 07-01-2018.
 */

public class Message_Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public ArrayList<Model_Message> arrayList = new ArrayList<>();
    public ArrayList<Model_Message> selected_usersList = new ArrayList<>();
    Context context;
    private int SENDER = 0;
    private int RECEIVER = 1;
    private final int VIEW_TYPE_LOADING = 2;
    String text = "";
    private SparseBooleanArray mSelectedItemsIds;

    public Message_Adapter(ArrayList<Model_Message> arrayList, ArrayList<Model_Message> selected_usersList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
        this.selected_usersList = selected_usersList;
        mSelectedItemsIds = new SparseBooleanArray();
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = null;
        RecyclerView.ViewHolder vh = null;
        if (viewType == VIEW_TYPE_LOADING) {
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_loading_item, parent, false);
            vh = new LoadingViewHolder(v);
        } else {
            if (viewType == SENDER) {
                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_sender_msg, parent, false);
                vh = new SenderMessageHolder(v);
            } else {
                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_receiver_msg, parent, false);
                vh = new ReceiverMessageHolder(v);
            }
        }
        return vh;
    }

    //Toggle selection methods
    public void toggleSelection(int position) {
        selectView(position, !mSelectedItemsIds.get(position));
    }


    //Remove selected selections
    public void removeSelection() {
        mSelectedItemsIds = new SparseBooleanArray();
        notifyDataSetChanged();
    }


    //Put or delete selected position into SparseBooleanArray
    public void selectView(int position, boolean value) {
        if (value)
            mSelectedItemsIds.put(position, value);
        else
            mSelectedItemsIds.delete(position);

        notifyDataSetChanged();
    }

    //Get total selected count
    public int getSelectedCount() {
        return mSelectedItemsIds.size();
    }

    //Return all selected ids
    public SparseBooleanArray getSelectedIds() {
        return mSelectedItemsIds;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if (holder.getItemViewType() == VIEW_TYPE_LOADING) {
            ((LoadingViewHolder) holder).progressBar.setIndeterminate(true);
        } else {
            if (holder.getItemViewType() == SENDER) {
                ((SenderMessageHolder) holder).txt_send_msg.setText(arrayList.get(position).getMessage());
                ((SenderMessageHolder) holder).send_time.setText(arrayList.get(position).getTime());
               /* if (selected_usersList.contains(arrayList.get(position)))
//                    ((SenderMessageHolder) holder).layout_main_sender.setBackgroundColor(context.getResources().getColor(R.color.list_item_selected_state));
                else
                ((SenderMessageHolder) holder).layout_main_sender.setBackgroundColor(Color.TRANSPARENT);*/
            } else {
                ((ReceiverMessageHolder) holder).txt_recei_msg.setText(arrayList.get(position).getMessage());
                ((ReceiverMessageHolder) holder).recei_time.setText(arrayList.get(position).getTime());
                if (!TextUtils.isEmpty(arrayList.get(position).getImage())) {
                    Glide.with(context)
                            .load(arrayList.get(position).getImage())
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
                            .into(((ReceiverMessageHolder) holder).img_user);
                } else {
                    ((ReceiverMessageHolder) holder).img_user.setImageDrawable(context.getResources().getDrawable(R.mipmap.ic_app_logo));
                }

            }
            Log.e("type :", arrayList.get(position).getType() + "");
        }
    }

    @Override
    public int getItemCount() {
        return arrayList == null ? 0 : arrayList.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        if (arrayList.get(position) == null) {
            return VIEW_TYPE_LOADING;
        } else {
            if (arrayList.get(position).getType() == SENDER)
                return SENDER;
            return RECEIVER;
        }
    }

    private class SenderMessageHolder extends RecyclerView.ViewHolder {
        TextView txt_send_msg, send_time;
        LinearLayout layout;
        RelativeLayout layout_main_sender;

        public SenderMessageHolder(View itemView) {
            super(itemView);
            txt_send_msg = (TextView) itemView.findViewById(R.id.txt_sender_msg);
            send_time = (TextView) itemView.findViewById(R.id.txt_sender_time);
            layout = (LinearLayout) itemView.findViewById(R.id.lin1);
            layout_main_sender = (RelativeLayout) itemView.findViewById(R.id.layout_main_sender);

          /*  layout.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {

                    arrayList.get(getAdapterPosition()).setSelected(!arrayList.get(getAdapterPosition()).isSelected());
                    layout_main_sender.setBackgroundColor(arrayList.get(getAdapterPosition()).isSelected() ? context.getResources().getColor(R.color.list_item_selected_state) : Color.TRANSPARENT);
                *//*    Message_Activity.HideAnimDialog();

                    final BottomSheetDialog dialog = new BottomSheetDialog(context);
                    dialog.setCancelable(true);
                    dialog.setContentView(R.layout.dialog_bottom_download);

                    LinearLayout lin_forward = (LinearLayout) dialog.findViewById(R.id.lin_forward);
                    LinearLayout lin_copy = (LinearLayout) dialog.findViewById(R.id.lin_copy);
                    LinearLayout lin_delete = (LinearLayout) dialog.findViewById(R.id.lin_delete);
                    LinearLayout lin_edit = (LinearLayout) dialog.findViewById(R.id.lin_edit);
                    dialog.show();*//*

                    for (Model_Message model : arrayList) {
                        if (model.isSelected()) {
                            text += model.getMessage();
                        }
                    }
                    Log.d("TAG", "Output : " + text);
                    return false;
                }
            });*/
        }
    }

    private class ReceiverMessageHolder extends RecyclerView.ViewHolder {
        CircleImageView img_user;
        TextView txt_recei_msg, recei_time;

        public ReceiverMessageHolder(View itemView) {
            super(itemView);
            img_user = (CircleImageView) itemView.findViewById(R.id.img_profile);
            txt_recei_msg = (TextView) itemView.findViewById(R.id.txt_recei_msg);
            recei_time = (TextView) itemView.findViewById(R.id.txt_recei_time);
        }
    }

    static class LoadingViewHolder extends RecyclerView.ViewHolder {
        public ProgressBar progressBar;

        public LoadingViewHolder(View itemView) {
            super(itemView);
            progressBar = (ProgressBar) itemView.findViewById(R.id.progressBar1);
        }
    }

}

