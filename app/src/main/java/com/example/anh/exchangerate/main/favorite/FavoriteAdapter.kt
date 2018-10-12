package com.example.anh.exchangerate.main.favorite

import android.databinding.DataBindingUtil
import android.graphics.Color
import android.support.v4.view.MotionEventCompat
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.ViewGroup
import com.example.anh.exchangerate.R
import com.example.anh.exchangerate.databinding.ItemFavoriteBinding
import com.example.anh.exchangerate.source.model.Rate
import com.example.anh.exchangerate.widget.ItemTouchHelperAdapter
import com.example.anh.exchangerate.widget.ItemTouchHelperViewHolder
import java.util.*


class FavoriteAdapter(rates: List<Rate>, value: Double,
    listener: OnClickItemListener<Rate>,
    onStartDragListener: OnStartDragListener,
    isEditItem: Boolean) : RecyclerView.Adapter<FavoriteAdapter.ViewHolder>(), ItemTouchHelperAdapter {
  private var mRates: MutableList<Rate> = rates as MutableList<Rate>
  private var mListener: OnClickItemListener<Rate> = listener
  private var mValue = value
  private var mOnStartDragListener: OnStartDragListener = onStartDragListener
  private var mIsEditItem: Boolean = isEditItem

  fun editItem(isEditItem: Boolean) {
    mIsEditItem = isEditItem
    this.notifyDataSetChanged()
  }

  override fun onItemMove(fromPosition: Int, toPosition: Int) {
    if (fromPosition < toPosition) {
      for (i in fromPosition until toPosition) {
        Collections.swap(mRates, i, i + 1)
      }
    } else {
      for (i in fromPosition downTo toPosition + 1) {
        Collections.swap(mRates, i, i - 1)
      }
    }
    notifyItemMoved(fromPosition, toPosition)
  }

  override fun onItemDismiss(position: Int) {
    mRates.removeAt(position)
    notifyItemRemoved(position)
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
    val binding: ItemFavoriteBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.context),
        R.layout.item_favorite, parent, false)
    return ViewHolder(binding, mListener, mValue, mOnStartDragListener, mIsEditItem)
  }

  override fun getItemCount(): Int {
    if (mRates.isEmpty()) {
      return 0
    }
    return mRates.size
  }

  override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    holder.mIsEditItem = mIsEditItem
    holder.bind(mRates[position])

  }

  fun updateDate(rates: List<Rate>) {
    if (rates.isEmpty()) {
      return
    }
    mRates.addAll(rates)
    notifyDataSetChanged()
  }

  fun reloadData(rates: List<Rate>) {
    mRates = arrayListOf()
    if (rates.isEmpty()) {
      return
    }
    mRates.addAll(rates)
    notifyDataSetChanged()
  }

  inner class ViewHolder(rateBinding: ItemFavoriteBinding,
      listener: OnClickItemListener<Rate>, value: Double,
      onStartDragListener: OnStartDragListener, isEditItem: Boolean) : RecyclerView.ViewHolder(
      rateBinding.root), ItemTouchHelperViewHolder {
    override fun onItemSelected() {
      itemView.setBackgroundColor(Color.LTGRAY)
    }

    override fun onItemClear() {
      itemView.setBackgroundColor(0)
    }

    private var mRateBinding = rateBinding
    private var mListener: OnClickItemListener<Rate> = listener
    private var mValue: Double = value
    private var mOnStartDragListener: OnStartDragListener = onStartDragListener
    var mIsEditItem: Boolean = isEditItem
    fun bind(rate: Rate) {
      mRateBinding.viewModel = rate
      mRateBinding.listener = mListener
      mRateBinding.value = mValue
      mRateBinding.handle.setOnTouchListener { _, event ->
        if (MotionEventCompat.getActionMasked(event) ==
            MotionEvent.ACTION_DOWN) {
          mOnStartDragListener.onStartDrag(this@ViewHolder)
        }
        false
      }
      mRateBinding.isEditItem = mIsEditItem
      mRateBinding.executePendingBindings()
    }
  }

  interface OnClickItemListener<T> {
    fun onCLickItem(item: T)
  }

  interface OnStartDragListener {
    fun onStartDrag(viewHolder: RecyclerView.ViewHolder)
  }
}
