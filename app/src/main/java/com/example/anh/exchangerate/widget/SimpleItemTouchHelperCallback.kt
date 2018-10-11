package com.example.anh.exchangerate.widget

import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import com.example.anh.exchangerate.main.favorite.FavoriteAdapter


/**
 * Created by FRAMGIA\pham.the.anh on 11/10/2018.
 */
class SimpleItemTouchHelperCallback(
    private val mAdapter: FavoriteAdapter) : ItemTouchHelper.Callback() {

  override fun isLongPressDragEnabled(): Boolean {
    return true
  }

  override fun isItemViewSwipeEnabled(): Boolean {
    return false
  }


  override fun getMovementFlags(recyclerView: RecyclerView,
      viewHolder: RecyclerView.ViewHolder): Int {
    val dragFlags = ItemTouchHelper.UP or ItemTouchHelper.DOWN
    val swipeFlags = ItemTouchHelper.START or ItemTouchHelper.END
    return ItemTouchHelper.Callback.makeMovementFlags(dragFlags, swipeFlags)
  }

  override fun onMove(recyclerView: RecyclerView, source: RecyclerView.ViewHolder,
      target: RecyclerView.ViewHolder): Boolean {
    mAdapter.onItemMove(source.adapterPosition, target.adapterPosition)
    return true
  }

  override fun onSwiped(viewHolder: RecyclerView.ViewHolder, i: Int) {
    mAdapter.onItemDismiss(viewHolder.adapterPosition)
  }

  override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
    if (actionState != ItemTouchHelper.ACTION_STATE_IDLE) {
      val itemViewHolder = viewHolder as FavoriteAdapter.ViewHolder?
      itemViewHolder!!.onItemSelected()
    }

    super.onSelectedChanged(viewHolder, actionState)
  }

  override fun clearView(recyclerView: RecyclerView?, viewHolder: RecyclerView.ViewHolder) {
    super.clearView(recyclerView, viewHolder)

    val itemViewHolder = viewHolder as FavoriteAdapter.ViewHolder
    itemViewHolder.onItemClear()
  }
}