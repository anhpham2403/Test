package com.example.anh.exchangerate.main.favorite

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.anh.exchangerate.R
import com.example.anh.exchangerate.databinding.ItemChartBinding

class ChartFragment : Fragment() {
  companion object {
    @JvmStatic
    fun getInstance(index: Int): ChartFragment {
      var bundle = Bundle()
      bundle.putInt("position", index)
      var chartFragment = ChartFragment()
      chartFragment.arguments = bundle
      return chartFragment
    }
  }

  private lateinit var mChartViewModel: ChartViewModel
  private var mIndex = 0
  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
      savedInstanceState: Bundle?): View? {
    mIndex = arguments!!.getInt("position", 0)
    val chartBinding: ItemChartBinding = DataBindingUtil.inflate(inflater, R.layout.item_chart,
        container, false)
    chartBinding.viewModel = mChartViewModel
    setHasOptionsMenu(true)
    return chartBinding.root
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    mChartViewModel = ChartViewModel(this.context!!, mIndex)
  }
}