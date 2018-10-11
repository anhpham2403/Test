package com.example.anh.exchangerate.utils

import android.databinding.BindingAdapter
import android.graphics.Color
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import java.util.*


class BindingUtils {
  companion object {
    @BindingAdapter("app:recyclerAdapter")
    @JvmStatic
    fun setAdapterForRecyclerView(recyclerView: RecyclerView,
        adapter: RecyclerView.Adapter<*>) {
      recyclerView.adapter = adapter
    }

    @BindingAdapter("layoutManager")
    @JvmStatic
    fun setLayoutManager(recyclerView: RecyclerView,
        layoutManagerFactory: LayoutManagers.LayoutManagerFactory) {
      recyclerView.layoutManager = layoutManagerFactory.create(recyclerView)
    }

    @BindingAdapter("app:flagCurrency")
    @JvmStatic
    fun setFlagCurrency(image: ImageView, id: String) {
      val id = image.context.resources.getIdentifier(id.toLowerCase(), "drawable",
          image.context.packageName)
      if (id > 0) {
        Glide.with(image).load(id).into(image)
      }
    }

    @BindingAdapter("app:xVals", "app:yVals")
    @JvmStatic
    fun setDataChart(lineChart: LineChart, xVals: ArrayList<String>, yVals: ArrayList<Entry>) {
      if (!xVals.isEmpty()) {
        val set1 = LineDataSet(yVals, "History rates")
        set1.fillAlpha = 110
        set1.color = Color.BLACK
        set1.setCircleColor(Color.BLACK)
        set1.lineWidth = 1f
        set1.circleRadius = 3f
        set1.setDrawCircleHole(false)
        set1.valueTextSize = 9f
        set1.setDrawFilled(true)
        val dataSets = ArrayList<ILineDataSet>()
        dataSets.add(set1)
        val data = LineData(xVals, dataSets)
        lineChart.data = data
        val l = lineChart.legend
        l.form = Legend.LegendForm.LINE
      }
    }

    @BindingAdapter("app:itemTouchHelper", "app:isEdit")
    @JvmStatic
    fun setItemTouchHelper(recyclerView: RecyclerView, itemTouchHelper: ItemTouchHelper,
        isEdit: Boolean) {
      if (isEdit) {
        itemTouchHelper.attachToRecyclerView(recyclerView)
      }
    }
  }
}