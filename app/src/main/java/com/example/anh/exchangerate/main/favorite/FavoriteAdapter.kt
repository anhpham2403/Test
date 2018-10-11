package com.example.anh.exchangerate.main.favorite

import android.databinding.DataBindingUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.anh.exchangerate.R
import com.example.anh.exchangerate.databinding.ItemFavoriteBinding
import com.example.anh.exchangerate.source.model.Rate


class FavoriteAdapter(rates: List<Rate>, value: Double,
    listener: OnClickItemListener<Rate>) : RecyclerView.Adapter<FavoriteAdapter.ViewHolder>() {

  private var mRates: MutableList<Rate> = rates as MutableList<Rate>
  private var mListener: OnClickItemListener<Rate> = listener
  private var mValue = value
  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
    val binding: ItemFavoriteBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.context),
        R.layout.item_favorite, parent, false)
    return ViewHolder(binding, mListener, mValue)
  }

  override fun getItemCount(): Int {
    if (mRates.isEmpty()) {
      return 0
    }
    return mRates.size
  }

  override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    holder.bind(mRates[position])
  }

  fun updateDate(rates: List<Rate>) {
    if (rates.isEmpty()) {
      return
    }
    mRates.addAll(rates)
  }

  inner class ViewHolder(rateBinding: ItemFavoriteBinding,
      listener: OnClickItemListener<Rate>, value: Double) : RecyclerView.ViewHolder(
      rateBinding.root) {
    private var mRateBinding = rateBinding
    private var mListener: OnClickItemListener<Rate> = listener
    private var mValue: Double = value
    fun bind(rate: Rate) {
      mRateBinding.viewModel = rate
      mRateBinding.listener = mListener
      mRateBinding.value = mValue
      mRateBinding.executePendingBindings()
    }
  }

  interface OnClickItemListener<T> {
    fun onCLickItem(item: T)
  }
}
