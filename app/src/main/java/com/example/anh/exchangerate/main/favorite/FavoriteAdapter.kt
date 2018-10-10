package com.example.anh.exchangerate.main.favorite

import android.databinding.DataBindingUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.anh.exchangerate.R
import com.example.anh.exchangerate.databinding.ItemFavoriteBinding
import com.example.anh.exchangerate.source.model.Rate


class FavoriteAdapter(rates: MutableList<Rate>, listener: OnClickItemListener<Rate>) : RecyclerView.Adapter<FavoriteAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: ItemFavoriteBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_favorite, parent, false)
        return ViewHolder(binding, mListener)
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

    private var mRates: MutableList<Rate> = rates
    private var mListener: OnClickItemListener<Rate> = listener

    fun updateDate(rates: List<Rate>) {
        if (rates.isEmpty()) {
            return
        }
        mRates.addAll(mRates)
        notifyDataSetChanged()
    }

    inner class ViewHolder(rateBinding: ItemFavoriteBinding, listener: OnClickItemListener<Rate>) : RecyclerView.ViewHolder(rateBinding.root) {
        private var mRateBinding = rateBinding
        private var mListener: OnClickItemListener<Rate> = listener

        fun bind(rate: Rate) {
            mRateBinding.viewModel = rate
            mRateBinding.listener = mListener
            mRateBinding.executePendingBindings()
        }
    }

    interface OnClickItemListener<T> {
        fun onCLickItem(item: T)
    }
}
