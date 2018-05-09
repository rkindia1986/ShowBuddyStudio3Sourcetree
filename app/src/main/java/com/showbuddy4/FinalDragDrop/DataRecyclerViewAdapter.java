package com.showbuddy4.FinalDragDrop;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.showbuddy4.R;
import com.showbuddy4.model.GetTopTenListModel;

import java.util.Collections;
import java.util.List;

/**
 * Created by User on 16-04-2018.
 */

public class DataRecyclerViewAdapter  extends RecyclerView.Adapter<DataRecyclerViewAdapter.MyViewHolder> implements ItemTouchHelperAdapter{

    private List<GetTopTenListModel> dataModelArrayList;
    private Context mContext;
    private  OnStartDragListener mDragStartListener;
    public RecyclerViewItemClickInterface delegate;

    public DataRecyclerViewAdapter(Context context, List<GetTopTenListModel> dataModelArrayList, OnStartDragListener mDragStartListener) {
        this.mContext = context;
        this.dataModelArrayList = dataModelArrayList;
        this.mDragStartListener=mDragStartListener;
    }

    @Override
    public DataRecyclerViewAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.raw_drag_drop, parent, false);

        return new DataRecyclerViewAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {

        holder.mName.setText(String.valueOf(position)+". "+dataModelArrayList.get(position).getName());

        holder.mMainCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //delegate.onItemClicked(String.valueOf(dataModel.getName()));
            }
        });

        holder.mMainCardView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    mDragStartListener.onStartDrag(holder);
                    Log.e("in","adapter start");
                }
                return false;
            }
        });
    }

    @Override
    public void onItemMove(int fromIndex, int toIndex) {

        if (fromIndex < dataModelArrayList.size() && toIndex < dataModelArrayList.size()) {
            if (fromIndex < toIndex) {
                for (int i = fromIndex; i < toIndex; i++) {
                    Collections.swap(dataModelArrayList, i, i + 1);
                }
            } else {
                for (int i = fromIndex; i > toIndex; i--) {
                    Collections.swap(dataModelArrayList, i, i - 1);
                }
            }
            notifyItemMoved(fromIndex, toIndex);
            Log.e("in","from ="+fromIndex+"to"+toIndex);
        }
    }

    @Override
    public void onItemDismiss(int position) {
        if (dataModelArrayList.size()< position) {
            dataModelArrayList.remove(position);
            notifyItemRemoved(position);
        }
    }



    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView mName;

        LinearLayout mMainCardView;

        public MyViewHolder(View view) {
            super(view);
            mName = view.findViewById(R.id.textview);
            mMainCardView = view.findViewById(R.id.root);
        }



    }

    @Override
    public int getItemCount() {
        return dataModelArrayList.size();
    }

    public List<GetTopTenListModel>getUpdateList(){return dataModelArrayList;}
}
