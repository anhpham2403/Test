package com.example.anh.exchangerate.main.exchangeRate

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.databinding.BaseObservable
import android.databinding.Bindable
import android.support.v4.content.LocalBroadcastManager
import android.widget.Toast
import com.android.databinding.library.baseAdapters.BR
import com.example.anh.exchangerate.calculator.CalculatorActivity
import com.example.anh.exchangerate.calculator.CalculatorViewModel
import com.example.anh.exchangerate.choosecurrency.ChooseCurrency
import com.example.anh.exchangerate.choosecurrency.ChooseCurrencyViewModel
import com.example.anh.exchangerate.source.CallBack
import com.example.anh.exchangerate.source.data.CurrencyRepository
import com.example.anh.exchangerate.source.data.local.sqlite.DBHelper
import com.example.anh.exchangerate.source.model.Currency
import com.example.anh.exchangerate.source.model.Rate
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.launch
import java.lang.Exception
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.*


class ExchangeRateViewModel(context: Context) : BaseObservable() {
  private var mContext: Context = context
  private val dbHelper: DBHelper = DBHelper(mContext)
  private val mCurrencyRepository: CurrencyRepository = CurrencyRepository(mContext)
  var rate1: Double = 1.0
    @Bindable
    get() = field
    set(value) {
      field = value
      notifyPropertyChanged(BR.rate1)
    }
  var rate2: Double = 1.0
    @Bindable
    get() = field
    set(value) {
      field = value
      notifyPropertyChanged(BR.rate2)
    }
  var valueRate1: Double = 1.0
    @Bindable
    get() = field
    set(value) {
      field = value
      notifyPropertyChanged(BR.valueRate1)
    }
  var valueRate2: Double = 1.0
    @Bindable
    get() = field
    set(value) {
      field = value
      notifyPropertyChanged(BR.valueRate2)
    }
  @Bindable
  val formatter = DecimalFormat("###,###,###.##", DecimalFormatSymbols(Locale.ENGLISH))
  @Bindable
  val formatter1 = DecimalFormat("###,###,###.####", DecimalFormatSymbols(Locale.ENGLISH))
  private var localBroadcastReceiver = object : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
      if (intent.action == "isCurrency1") {
        currency1 = intent.getParcelableExtra(ChooseCurrencyViewModel.EXTRA_DATA)
        valueRate1 = intent.getDoubleExtra(CalculatorViewModel.EXTRA_KQ, valueRate1)
        getData(true)
      } else if (intent.action == "isCurrency2") {
        currency2 = intent.getParcelableExtra(ChooseCurrencyViewModel.EXTRA_DATA)
        valueRate2 = intent.getDoubleExtra(CalculatorViewModel.EXTRA_KQ, valueRate2)
        getData(false)
      }
    }
  }

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
      mCurrencyRepository.getRateSeverIfNecessary(mContext, currency1, currency2,
          object : CallBack<List<Rate>> {
            override fun onSuccess(data: List<Rate>) {
              valueRate2 = valueRate1 / data[0].rate2
              rate1 = data[0].rate1
              rate2 = data[0].rate2
            }

            override fun onFailure(mes: String?) {
              Toast.makeText(mContext, mes, Toast.LENGTH_SHORT).show()
            }
          })
    }
  }

  private fun getData(isCurrency1: Boolean) {
    mCurrencyRepository.getRateSeverIfNecessary(mContext, currency1, currency2,
        object : CallBack<List<Rate>> {
          override fun onSuccess(data: List<Rate>) {
            if (isCurrency1) {
              valueRate2 = valueRate1 / data[0].rate2
            } else {
              valueRate1 = valueRate2 / data[0].rate1
            }
            rate1 = data[0].rate1
            rate2 = data[0].rate2
          }

          override fun onFailure(mes: String?) {
            Toast.makeText(mContext, mes, Toast.LENGTH_SHORT).show()
          }
        })
  }

  private fun getDataReload(isCurrency1: Boolean) {
    mCurrencyRepository.getRateServer(mContext, currency1, listOf(currency2),
        object : CallBack<List<Rate>> {
          override fun onSuccess(data: List<Rate>) {
            if (isCurrency1) {
              valueRate2 = valueRate1 / data[0].rate2
            } else {
              valueRate1 = valueRate2 / data[0].rate1

            }
          }

          override fun onFailure(mes: String?) {
            Toast.makeText(mContext, mes, Toast.LENGTH_SHORT).show()
          }
        })
  }

  fun onClickFrom(isCurrency1: Boolean) {
    mContext.startActivity(ChooseCurrency.getInstance(mContext, isCurrency1))
  }

  fun onDestroy() {
    LocalBroadcastManager.getInstance(mContext).unregisterReceiver(localBroadcastReceiver)
  }

  fun onClickItemCurrency(isCurrency1: Boolean, currency: Currency) {
    mContext.startActivity(CalculatorActivity.getInstance(mContext, isCurrency1, currency))
  }

  fun onChangePrimacy() {
    var rateChange = rate1
    rate1 = rate2
    rate2 = rateChange
    var valueRateChange = valueRate1
    valueRate1 = valueRate2
    valueRate2 = valueRateChange
    var currencyChange = currency1
    currency1 = currency2
    currency2 = currencyChange
  }

  fun onReload() {
    getDataReload(true)
  }
}
