package com.example.anh.exchangerate.widget

import android.support.v7.widget.helper.ItemTouchHelper

/**
 * Created by FRAMGIA\pham.the.anh on 11/10/2018.
 */
interface ItemTouchHelperViewHolder {

  /**
   * Called when the [ItemTouchHelper] first registers an item as being moved or swiped.
   * Implementations should update the item view to indicate it's active state.
   */
  fun onItemSelected()


  /**
   * Called when the [ItemTouchHelper] has completed the move or swipe, and the active item
   * state should be cleared.
   */
  fun onItemClear()
}