package com.example.anh.exchangerate.main.chart

import android.os.Bundle
import android.support.constraint.ConstraintLayout
import android.support.v4.app.Fragment
import android.support.v4.view.PagerAdapter
import android.support.v4.view.ViewPager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.anh.exchangerate.R

/**
 * Created by FRAMGIA\pham.the.anh on 10/10/2018.
 */
class ChartMainFragment : Fragment() {
  private lateinit var mViewPager: ViewPager
  private lateinit var mViewPagerAdapter: PagerAdapter
  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
      savedInstanceState: Bundle?): View? {
    var layout: ConstraintLayout = inflater.inflate(R.layout.chart, container,
        false) as ConstraintLayout
    mViewPagerAdapter = ChartViewPager(activity!!.supportFragmentManager)
    mViewPager = layout.findViewById(R.id.view_pager)
    mViewPager.adapter = mViewPagerAdapter
    return layout
  }
}