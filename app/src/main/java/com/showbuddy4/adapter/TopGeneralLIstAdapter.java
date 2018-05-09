package com.showbuddy4.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.view.MotionEventCompat;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.showbuddy4.R;
import com.showbuddy4.dragndrop.ItemTouchHelperAdapter;
import com.showbuddy4.dragndrop.ItemTouchHelperViewHolder;
import com.showbuddy4.dragndrop.OnStartDragListener;
import com.showbuddy4.model.GetTopTenListModel;

import java.util.ArrayList;
import java.util.Collections;


public class TopGeneralLIstAdapter extends RecyclerView.Adapter<TopGeneralLIstAdapter.ItemViewHolder> implements ItemTouchHelperAdapter {

    private ArrayList<GetTopTenListModel> itemDatas;
    private ArrayList<GetTopTenListModel> itemDatasPendingRemoval;
    private static Context context;
    private final OnStartDragListener mDragStartListener;



    public interface ICallback {
        void onItemRemove(int position);
        void onItemUndo(int position);
        void onItemPositionChange(int fromPosition, int toPosition);
    }

    private ICallback listener;

    public TopGeneralLIstAdapter(Context context, OnStartDragListener dragStartListener, ArrayList<GetTopTenListModel> itemDatas, ICallback listener){
        this.context = context;
        mDragStartListener = dragStartListener;
        this.itemDatas = itemDatas;
        this.listener = listener;

        itemDatasPendingRemoval = new ArrayList<>();
    }

    @Override
    public TopGeneralLIstAdapter.ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recyclerview, parent, false);
        return  new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ItemViewHolder holder, final int position) {

        final GetTopTenListModel destination = itemDatas.get(position);

        if (itemDatasPendingRemoval.contains(destination)) {
            // we need to show the "undo" state of the row
            holder.itemView.setBackgroundColor(Color.RED);
            holder.tvDestination.setVisibility(View.GONE);
     //       holder.rlSwipeToDelete.setVisibility(View.VISIBLE);
            holder.rlSwipeToDelete.setEnabled(false);
            holder.tvUndo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemDatasPendingRemoval.remove(destination);
                    notifyItemChanged(itemDatas.indexOf(destination));
                    listener.onItemUndo(position);
                }
            });
            holder.tvDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   remove(position);
                }
            });
            holder.tvDestination.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (MotionEventCompat.getActionMasked(event) == MotionEvent.ACTION_DOWN) {
                        mDragStartListener.onStartDrag(holder);
                    }

                    return false;
                }
            });
        }else {
//            holder.tvDestination.setOnTouchListener(new View.OnTouchListener() {
//                @Override
//                public boolean onTouch(View v, MotionEvent event) {
//                    if (MotionEventCompat.getActionMasked(event) == MotionEvent.ACTION_DOWN) {
//                        mDragStartListener.onStartDrag(holder);
//                    }
//
//                    return false;
//                }
//            });
//

            holder.itemView.setBackgroundColor(Color.WHITE);
            holder.tvDestination.setVisibility(View.VISIBLE);

            holder.tvDestination.setText(destination.getName());

            holder.rlSwipeToDelete.setVisibility(View.GONE);
            holder.tvUndo.setOnClickListener(null);
            holder.tvDelete.setOnClickListener(null);
        }
    }

    @Override
    public int getItemCount() {
        return itemDatas.size();
    }

    public void pendingRemoval(final int position,int swipeDir) {
        final GetTopTenListModel destination = itemDatas.get(position);

        if (!itemDatasPendingRemoval.contains(destination)) {
            undoPreviousPositionsIfAny(position);
            itemDatasPendingRemoval.add(destination);
            notifyItemChanged(position);
        }else{
            if(swipeDir == ItemTouchHelper.START) {
                itemDatasPendingRemoval.remove(destination);
                notifyItemChanged(position);
                listener.onItemUndo(position);
            }else{
                itemDatas.remove(destination);
                notifyDataSetChanged();
                listener.onItemRemove(position);
            }
        }
    }

    private void undoPreviousPositionsIfAny(int position){
        for (int i = 0; i < itemDatas.size() ; i++) {

            if(i != position){
                if(itemDatasPendingRemoval.contains(itemDatas.get(i))){
                    itemDatasPendingRemoval.remove(itemDatas.get(i));
                    notifyItemChanged(i);
                }
            }
        }
    }

    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
        Collections.swap(itemDatas, fromPosition, toPosition);
        notifyItemMoved(fromPosition, toPosition);
        notifyItemChanged(fromPosition);
        notifyItemChanged(toPosition);
        //notifyDataSetChanged();

        return true;
    }

    public void remove(int position) {
        final GetTopTenListModel destination = itemDatas.get(position);
        if (itemDatasPendingRemoval.contains(destination)) {
            itemDatasPendingRemoval.remove(destination);
        }
        if (itemDatas.contains(destination)) {
            itemDatas.remove(position);
            notifyItemRemoved(position);
            listener.onItemRemove(position);
        }
    }

    public boolean isPendingRemoval(int position) {
        GetTopTenListModel destination = itemDatas.get(position);
        return itemDatasPendingRemoval.contains(destination);
    }

    @Override
    public void onDrop(int fromPosition, int toPosition) {
        listener.onItemPositionChange(fromPosition,toPosition);
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder implements
            ItemTouchHelperViewHolder {

        public final TextView tvDestination;
        public final LinearLayout llMain;
        public final TextView tvUndo;
        public final TextView tvDelete;
        public final RelativeLayout rlSwipeToDelete;

        public ItemViewHolder(View itemView) {
            super(itemView);
            tvDestination = (TextView) itemView.findViewById(R.id.tvDestination);
            llMain = (LinearLayout) itemView.findViewById(R.id.llMain);
            tvDelete= (TextView) itemView.findViewById(R.id.tvDelete);
            tvUndo = (TextView) itemView.findViewById(R.id.tvUndo);
            rlSwipeToDelete = (RelativeLayout) itemView.findViewById(R.id.rlSwipeToDelete);
        }

        @Override
        public void onItemSelected() {
            itemView.setBackgroundColor(context.getResources().getColor(R.color.color_ebedf0));
        }

        @Override
        public void onItemClear() {
            itemView.setBackgroundColor(Color.WHITE);
        }
    }
}
