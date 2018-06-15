package com.example.anh.exchangerate.main.exchangeRate

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.anh.exchangerate.R
import com.example.anh.exchangerate.databinding.ExchangeRateBinding

class ExchangeRate : Fragment() {
    private lateinit var mExchangeRateViewModel: ExchangeRateViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mExchangeRateViewModel = ExchangeRateViewModel(context!!)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val exchangeRateBinding: ExchangeRateBinding = DataBindingUtil.inflate(inflater, R.layout.exchange_rate, container, false)
        exchangeRateBinding.viewModel = mExchangeRateViewModel
        return exchangeRateBinding.root
    }

    companion object {
        fun getInstance(): Fragment {
            return ExchangeRate()
        }
    }
}