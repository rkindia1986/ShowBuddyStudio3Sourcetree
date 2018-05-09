package com.showbuddy4.DRAG;

/**
 * Created by User on 15-04-2018.
 */

public interface ItemTouchHelperAdapter {
    boolean onItemMove(int fromPosition, int toPosition);
    void onItemDismiss(int position);
}
