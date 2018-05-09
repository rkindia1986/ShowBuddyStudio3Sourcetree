package com.showbuddy4.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.view.MotionEventCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.showbuddy4.DRAG.ItemTouchHelperAdapter;
import com.showbuddy4.DRAG.ItemTouchHelperViewHolder;
import com.showbuddy4.DRAG.OnStartDragListener;
import com.showbuddy4.R;
import com.showbuddy4.model.GetTopTenListModel;

import java.util.Collections;
import java.util.List;

/**
 * Created by User on 15-04-2018.
 */

public class DragDropAdapter extends RecyclerView.Adapter<DragDropAdapter.ViewHolder>  implements ItemTouchHelperAdapter {
    private List<GetTopTenListModel> list;
    private final OnStartDragListener mDragStartListener;
    public DragDropAdapter(List<GetTopTenListModel> list, Context context, OnStartDragListener dragStartListener) {
        this.list = list;
        mDragStartListener = dragStartListener;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout, parent, false);
        return new ViewHolder(v);
    }
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.textView.setText(list.get(position).getName());
        holder.imageView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (MotionEventCompat.getActionMasked(event) == MotionEvent.ACTION_DOWN) {
                    mDragStartListener.onStartDrag(holder);
                }
                return false;
            }
        });
    }
    @Override
    public int getItemCount() {
        return list.size();
    }
    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
        Collections.swap(list, fromPosition, toPosition);
        notifyItemMoved(fromPosition, toPosition);
        return true;
    }
    @Override
    public void onItemDismiss(int position) {
        list.remove(position);
        notifyItemRemoved(position);
    }
    public class ViewHolder extends RecyclerView.ViewHolder implements
            ItemTouchHelperViewHolder {
        TextView textView;
        ImageView imageView;
        public ViewHolder(View itemView) {
            super(itemView);
            textView=(TextView)itemView.findViewById(R.id.text);
            imageView=(ImageView)itemView.findViewById(R.id.handle);
        }
        @Override
        public void onItemSelected() {
            itemView.setBackgroundColor(Color.LTGRAY);
        }
        @Override
        public void onItemClear() {
            itemView.setBackgroundColor(0);
        }
    }
}
