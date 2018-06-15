package com.example.anh.exchangerate.main.exchangeRate

import android.content.Context
import android.databinding.BaseObservable
import com.example.anh.exchangerate.choosecurrency.ChooseCurrency

class ExchangeRateViewModel(context: Context) : BaseObservable() {
    private var context: Context = context
    fun onClickFrom() {
        context.startActivity(ChooseCurrency.getInstance(context))
    }
}