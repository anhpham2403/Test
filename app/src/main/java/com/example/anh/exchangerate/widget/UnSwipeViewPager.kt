package com.example.anh.exchangerate.widget

/**
 * Created by FRAMGIA\pham.the.anh on 11/10/2018.
 */
import android.content.Context
import android.support.v4.view.ViewPager
import android.util.AttributeSet
import android.view.MotionEvent

class UnSwipeViewPager : ViewPager {
  constructor(context: Context) : super(context) {}

  constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {}

  override fun onInterceptTouchEvent(arg0: MotionEvent): Boolean {
    return false
  }

  override fun canScrollHorizontally(direction: Int): Boolean {
    return false
  }

  override fun onTouchEvent(ev: MotionEvent): Boolean {
    return false
  }

  override fun setCurrentItem(item: Int) {
    super.setCurrentItem(item, false)
  }
}