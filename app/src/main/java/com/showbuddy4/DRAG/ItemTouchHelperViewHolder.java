package com.showbuddy4.DRAG;

/**
 * Created by User on 15-04-2018.
 */

public interface ItemTouchHelperViewHolder {
     // * Implementations should update the item view to indicate it's active state. */
    void onItemSelected();
    /*
    Called when completed the move or swipe, and the active item * state should be cleared.
    */
    void onItemClear();
}
