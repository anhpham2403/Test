package com.example.anh.exchangerate.main.exchangeRate

import android.os.Bundle
import android.support.constraint.ConstraintLayout
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.anh.exchangerate.R

class ExchangeRate : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var layout: ConstraintLayout = inflater.inflate(R.layout.exchange_rate, container,false) as ConstraintLayout
        return layout
    }

    companion object {
        fun getInstance(): Fragment {
            return ExchangeRate()
        }
    }
}