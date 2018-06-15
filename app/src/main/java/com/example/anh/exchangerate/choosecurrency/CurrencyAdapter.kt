package com.example.anh.exchangerate.choosecurrency

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.anh.exchangerate.databinding.ItemCurrencyBinding
import com.example.anh.exchangerate.source.model.Currency

class CurrencyAdapter : RecyclerView.Adapter<CurrencyAdapter.ViewHolder> {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemCurrencyBinding: ItemCurrencyBinding = ItemCurrencyBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(itemCurrencyBinding, mOnItemClick)
    }

    override fun getItemCount(): Int {
        return mCurrencies.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindData(mCurrencies[position])
    }

    private var mCurrencies: MutableList<Currency>
    private var mOnItemClick: OnItemClick<Currency>

    constructor(mCurrencies: MutableList<Currency>, onItemClick: OnItemClick<Currency>) {
        this.mCurrencies = mCurrencies
        this.mOnItemClick = onItemClick
    }

    fun updateData(currencies: MutableList<Currency>) {
        mCurrencies.addAll(currencies)
        notifyDataSetChanged()
    }

    class ViewHolder : RecyclerView.ViewHolder {
        private var mItemCurrencyBinding: ItemCurrencyBinding
        private var mOnItemClick: OnItemClick<Currency>

        constructor(itemCurrencyBinding: ItemCurrencyBinding, onItemClick: OnItemClick<Currency>) : super(itemCurrencyBinding.root) {
            mItemCurrencyBinding = itemCurrencyBinding
            mOnItemClick = onItemClick
        }

        fun bindData(currency: Currency) {
            mItemCurrencyBinding.viewModel = currency
            mItemCurrencyBinding.listener = mOnItemClick
            mItemCurrencyBinding.executePendingBindings()
        }
    }
}