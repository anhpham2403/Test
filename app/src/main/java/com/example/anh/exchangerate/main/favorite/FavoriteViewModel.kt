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
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.launch
import java.lang.Exception


class FavoriteViewModel(
    context: Context) : BaseObservable(), FavoriteAdapter.OnClickItemListener<Rate> {
  override fun onCLickItem(item: Rate) {
    TODO(
        "not implemented") //To change body of created functions use File | Settings | File Templates.
  }

  private var mContext = context
  private val dbHelper: DBHelper = DBHelper(mContext)
  private var listCurrency: MutableList<Currency> = arrayListOf()
  private val mCurrencyRepository: CurrencyRepository = CurrencyRepository(mContext)
  var value: Double = 1.0
    @Bindable
    get() {
      return field
    }
    set(value) {
      field = value
      notifyPropertyChanged(BR.value)
    }
  var currentCurrency: Currency = Currency()
    @Bindable
    get() = field
    set(value) {
      field = value
      notifyPropertyChanged(BR.currentCurrency)
    }
  var adapter: FavoriteAdapter = FavoriteAdapter(arrayListOf(), value, this@FavoriteViewModel)
    @Bindable
    get() = field
    set(value) {
      field = value
      notifyPropertyChanged(BR.adapter)
    }

  init {
    initData()
  }

  private var localBroadcastReceiver = object : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
      if (intent.action == "isCurrency1") {
        currentCurrency = intent.getParcelableExtra(ChooseCurrencyViewModel.EXTRA_DATA)
      }
    }
  }

  private fun initData() {
    val filter = IntentFilter()
    filter.addAction("isCurrency1")
    LocalBroadcastManager.getInstance(mContext).registerReceiver(localBroadcastReceiver, filter)
    launch(CommonPool) {
      try {
        dbHelper.openDB()
        var listBaseCurrency: MutableList<Currency> = async {
          dbHelper.getBaseCurrency("USD", "EUR")
        }.await()
        if (currentCurrency != null) {
          currentCurrency = listBaseCurrency[0]
        }
        dbHelper.close()
      } catch (e: Exception) {
        Toast.makeText(mContext, e.message, Toast.LENGTH_SHORT).show()
      }
      try {
        dbHelper.openDB()
        listCurrency = async { dbHelper.getAllFavorites() }.await()
        dbHelper.close()
      } catch (e: Exception) {
        Toast.makeText(mContext, e.message, Toast.LENGTH_SHORT).show()
      }
      mCurrencyRepository.getRateServer(mContext, currentCurrency, listCurrency,
          object : CallBack<List<Rate>> {
            override fun onSuccess(data: List<Rate>) {
              adapter.updateDate(data)
              adapter.notifyDataSetChanged()
            }

            override fun onFailure(mes: String?) {
              Toast.makeText(mContext, mes, Toast.LENGTH_SHORT).show()
            }
          })
    }
  }
}