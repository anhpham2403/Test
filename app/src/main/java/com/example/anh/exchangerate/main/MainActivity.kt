package com.example.anh.exchangerate.main

import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v4.view.PagerAdapter
import android.support.v4.view.ViewPager
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import com.example.anh.exchangerate.R
import com.example.anh.exchangerate.choosecurrency.ChooseCurrency
import com.example.anh.exchangerate.widget.UnSwipeViewPager
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*


class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
  private lateinit var mViewPager: UnSwipeViewPager
  private lateinit var mViewPagerAdapter: PagerAdapter

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)
    setSupportActionBar(toolbar)

    val toggle = ActionBarDrawerToggle(
        this, drawer_layout, toolbar, R.string.navigation_drawer_open,
        R.string.navigation_drawer_close)
    drawer_layout.addDrawerListener(toggle)
    toggle.syncState()

    nav_view.setNavigationItemSelectedListener(this)
    mViewPagerAdapter = ViewPagerAdapter(supportFragmentManager)
    mViewPager = findViewById(R.id.main_viewpager)
    mViewPager.adapter = mViewPagerAdapter
    mViewPager.offscreenPageLimit = 1
  }

  override fun onBackPressed() {
    if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
      drawer_layout.closeDrawer(GravityCompat.START)
    } else {
      super.onBackPressed()
    }
  }

  override fun onNavigationItemSelected(item: MenuItem): Boolean {
    // Handle navigation view item clicks here.
    when (item.itemId) {
      R.id.nav_favorite -> {
        mViewPager.currentItem = 0
      }
      R.id.nav_rate -> {
        mViewPager.currentItem = 1
      }
      R.id.nav_chart -> {
        mViewPager.currentItem = 2
      }
    }
    drawer_layout.closeDrawer(GravityCompat.START)
    return true
  }
}
