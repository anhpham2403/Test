package com.example.anh.exchangerate.main

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import com.example.anh.exchangerate.main.exchangeRate.ExchangeRate
import com.example.anh.exchangerate.main.favorite.Favorite

class ViewPagerAdapter(fm: FragmentManager): FragmentPagerAdapter(fm) {
    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> ExchangeRate()
            1 -> Favorite()
            else -> ExchangeRate()
        }
    }

    override fun getCount(): Int {
        return 3
    }
}