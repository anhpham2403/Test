package com.example.anh.exchangerate.main.favorite

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.databinding.BaseObservable
import android.databinding.Bindable
import android.support.v4.content.LocalBroadcastManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.widget.Toast
import com.android.databinding.library.baseAdapters.BR
import com.example.anh.exchangerate.choosecurrency.ChooseCurrencyViewModel
import com.example.anh.exchangerate.source.CallBack
import com.example.anh.exchangerate.source.data.CurrencyRepository
import com.example.anh.exchangerate.source.data.local.sqlite.DBHelper
import com.example.anh.exchangerate.source.model.Currency
import com.example.anh.exchangerate.source.model.Rate
import com.example.anh.exchangerate.widget.SimpleItemTouchHelperCallback
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.launch
import java.lang.Exception


class FavoriteViewModel(
    context: Context) : BaseObservable(), FavoriteAdapter.OnClickItemListener<Rate>, FavoriteAdapter.OnStartDragListener {
  override fun onStartDrag(viewHolder: RecyclerView.ViewHolder) {
    itemTouchHelper?.startDrag(viewHolder)
  }

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
  var itemTouchHelper: ItemTouchHelper? = null
    @Bindable
    get() {
      return field
    }
    set(value) {
      field = value
      notifyPropertyChanged(BR.itemTouchHelper)
    }
  var editData: Boolean = false
    @Bindable
    get() {
      return field
    }
    set(value) {
      field = value
      notifyPropertyChanged(BR.editData)
      notifyPropertyChanged(BR.itemTouchHelper)
    }
  var adapter: FavoriteAdapter = FavoriteAdapter(arrayListOf(), value, this@FavoriteViewModel,
      this@FavoriteViewModel, editData)
    @Bindable
    get() = field
    set(value) {
      field = value
      notifyPropertyChanged(BR.adapter)
    }

  private var localBroadcastReceiver = object : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
      if (intent.action == "isCurrency1") {
        currentCurrency = intent.getParcelableExtra(ChooseCurrencyViewModel.EXTRA_DATA)
      } else if (intent.action == "add_favorite") {
        launch(CommonPool) {
          try {
            val currency: Currency = intent.getParcelableExtra(ChooseCurrencyViewModel.EXTRA_DATA)
            var result = false
            dbHelper.openDB()
            async {
              result = dbHelper.addFavorite(currency.id!!)
            }.await()
            dbHelper.close()
            if (result) {
              listCurrency.add(currency)
              getData()
            }
          } catch (e: Exception) {
            Toast.makeText(mContext, e.message, Toast.LENGTH_SHORT).show()
          }
        }
      }
    }
  }

  init {
    val filter = IntentFilter()
    filter.addAction("isCurrency1")
    filter.addAction("add_favorite")
    LocalBroadcastManager.getInstance(mContext).registerReceiver(localBroadcastReceiver, filter)
    initData()
    val callback = SimpleItemTouchHelperCallback(adapter)
    itemTouchHelper = ItemTouchHelper(callback)
  }

  private fun initData() {
    launch(CommonPool) {
      try {
        dbHelper.openDB()
        var listBaseCurrency: MutableList<Currency> = async {
          dbHelper.getBaseCurrency("USD", "EUR")
        }.await()
        currentCurrency = listBaseCurrency[0]
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

  fun getData() {
    launch(CommonPool) {
      mCurrencyRepository.getRateServer(mContext, currentCurrency, listCurrency,
          object : CallBack<List<Rate>> {
            override fun onSuccess(data: List<Rate>) {
              adapter.reloadData(data)
              adapter.notifyDataSetChanged()
            }

            override fun onFailure(mes: String?) {
              Toast.makeText(mContext, mes, Toast.LENGTH_SHORT).show()
            }
          })
    }
  }

  fun onDestroy() {
    LocalBroadcastManager.getInstance(mContext).unregisterReceiver(localBroadcastReceiver)
  }
}