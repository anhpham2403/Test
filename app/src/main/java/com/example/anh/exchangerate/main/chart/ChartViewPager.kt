package com.example.anh.exchangerate.main.chart

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import com.example.anh.exchangerate.main.favorite.ChartFragment

/**
 * Created by FRAMGIA\pham.the.anh on 10/10/2018.
 */
class ChartViewPager(fm: FragmentManager) : FragmentPagerAdapter(fm) {
  private var title = arrayOf("1D", "1W", "1M", "3M", "6M", "1Y", "ALL", "CUSTOM")
  override fun getItem(position: Int): Fragment {
    return ChartFragment.getInstance(position)
  }

  override fun getCount(): Int {
    return 8
  }

  override fun getPageTitle(position: Int): CharSequence? {
    return title[position]
  }
}