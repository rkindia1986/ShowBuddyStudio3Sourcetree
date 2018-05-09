package com.showbuddy4.DRAG;

import android.support.v7.widget.RecyclerView;

/**
 * Created by User on 15-04-2018.
 */

public interface OnStartDragListener {
    /**
     * Called when a view is requesting a start of a drag.
     */
    void onStartDrag(RecyclerView.ViewHolder viewHolder);

}
