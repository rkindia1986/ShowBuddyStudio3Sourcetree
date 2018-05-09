package com.showbuddy4.FinalDragDrop;

/**
 * Created by smita on 10/02/18.
 */

public interface ItemTouchHelperAdapter {

    void onItemMove(int fromIndex, int toIndex);
    void onItemDismiss(int position);
}
