package com.example.anh.exchangerate.main.favorite

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.databinding.BaseObservable
import android.databinding.Bindable
import android.support.v4.content.LocalBroadcastManager
import android.widget.Toast
import com.android.databinding.library.baseAdapters.BR
import com.example.anh.exchangerate.choosecurrency.ChooseCurrencyViewModel
import com.example.anh.exchangerate.source.CallBack
import com.example.anh.exchangerate.source.data.CurrencyRepository
import com.example.anh.exchangerate.source.data.local.sqlite.DBHelper
import com.example.anh.exchangerate.source.model.Currency
import com.example.anh.exchangerate.source.model.Rate
import com.github.mikephil.charting.data.Entry
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.launch
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*

class ChartViewModel(context: Context, index: Int) : BaseObservable() {
  private var mContext = context
  private var mIndex = index
  private val dbHelper: DBHelper = DBHelper(mContext)
  private val mCurrencyRepository: CurrencyRepository = CurrencyRepository(mContext)
  var current = mContext.getResources().getConfiguration().locale!!
  private var isCurrent1 = true
  var dt = SimpleDateFormat("yyyyy-mm-dd hh:mm:ss", current)
  private var rates: List<Rate> = arrayListOf()
  var currency1 = Currency()
    @Bindable
    get() = field
    set(value) {
      field = value
      notifyPropertyChanged(BR.currency1)
    }
  var currency2 = Currency()
    @Bindable
    get() = field
    set(value) {
      field = value
      notifyPropertyChanged(BR.currency2)
    }
  private var localBroadcastReceiver = object : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
      if (intent.action == "isCurrency1") {
        currency1 = intent.getParcelableExtra(ChooseCurrencyViewModel.EXTRA_DATA)
        getData()
      } else if (intent.action == "isCurrency2") {
        currency2 = intent.getParcelableExtra(ChooseCurrencyViewModel.EXTRA_DATA)
        getData()
      }
    }
  }
  var xVals: ArrayList<String> = arrayListOf()
    @Bindable
    get() {
      return field
    }
    set(value) {
      field = value
      notifyPropertyChanged(BR.xVals)
    }
  var yVals = ArrayList<Entry>()
    @Bindable
    get() {
      return field
    }
    set(value) {
      field = value
      notifyPropertyChanged(BR.yVals)
    }
  var priority: String = ""
  var to: Long = 0
  var from: Long = 0

  init {
    val filter = IntentFilter()
    filter.addAction("isCurrency1")
    filter.addAction("isCurrency2")
    LocalBroadcastManager.getInstance(mContext).registerReceiver(localBroadcastReceiver, filter)
    launch(CommonPool) {
      try {
        dbHelper.openDB()
        var listBaseCurrency: MutableList<Currency> = async {
          dbHelper.getBaseCurrency("USD", "EUR")
        }.await()
        currency1 = listBaseCurrency[0]
        currency2 = listBaseCurrency[1]
        dbHelper.close()
      } catch (e: Exception) {
        Toast.makeText(mContext, e.message, Toast.LENGTH_SHORT).show()
      }
      mCurrencyRepository.getHistoryRate(currency1, currency2, priority, from, Calendar.getInstance().timeInMillis,
          object : CallBack<List<Rate>> {
            override fun onSuccess(data: List<Rate>) {
              rates = data
              xVals = getDataX(rates)
              yVals = getDataY(rates)
            }

            override fun onFailure(mes: String?) {
              Toast.makeText(mContext, mes, Toast.LENGTH_SHORT).show()
            }
          })
    }
  }

  fun getDataX(data: List<Rate>): ArrayList<String> {
    var xVals: ArrayList<String> = arrayListOf()
    for (rate: Rate in data) {
      var calendar = Calendar.getInstance()
      calendar.timeInMillis = rate.time
      calendar.timeZone = TimeZone.getTimeZone("UTC")
      xVals.add(dt.format(calendar.time))
    }
    return xVals
  }

  fun getDataY(data: List<Rate>): ArrayList<Entry> {
    var xVals: ArrayList<Entry> = arrayListOf()
    for (index: Int in data.indices) {
      if (isCurrent1) {
        xVals.add(Entry(rates[index].rate1.toFloat(), index))
      } else {
        xVals.add(Entry(rates[index].rate2.toFloat(), index))
      }
    }
    return xVals
  }

  private fun getData() {
    mCurrencyRepository.getHistoryRate(currency1, currency2, priority, from, Calendar.getInstance().timeInMillis,
        object : CallBack<List<Rate>> {
          override fun onSuccess(data: List<Rate>) {
            rates = data
            xVals = getDataX(rates)
            yVals = getDataY(rates)
          }

          override fun onFailure(mes: String?) {
            Toast.makeText(mContext, mes, Toast.LENGTH_SHORT).show()
          }
        })
  }


  fun onChangePrimacy() {
    var currencyChange = currency1
    currency1 = currency2
    currency2 = currencyChange
  }

  fun onDestroy() {
    LocalBroadcastManager.getInstance(mContext).unregisterReceiver(localBroadcastReceiver)
  }
}