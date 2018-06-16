package com.example.anh.exchangerate.choosecurrency

import android.content.Context
import android.content.Intent
import android.databinding.BaseObservable
import android.databinding.Bindable
import android.support.v4.content.LocalBroadcastManager
import android.widget.Toast
import com.android.databinding.library.baseAdapters.BR
import com.example.anh.exchangerate.source.data.local.sqlite.DBHelper
import com.example.anh.exchangerate.source.model.Currency
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.launch


class ChooseCurrencyViewModel(context: Context, isCurrency1: Boolean) : BaseObservable(), OnItemClick<Currency> {
    companion object {
        const val EXTRA_DATA = "EXTRA_DATA"
    }

    private val context: Context = context
    private val isCurrency1: Boolean = isCurrency1
    private val dbHelper = DBHelper(context)
    override fun onItemTimelineClick(item: Currency) {
        val intent = Intent()
        intent.putExtra(EXTRA_DATA, item)
        if (isCurrency1) {
            intent.action = "isCurrency1"
        } else {
            intent.action = "isCurrency2"
        }
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent)
        (context as ChooseCurrency).finish()
    }

    private var mCurrencies: ArrayList<Currency> = ArrayList()

    var currencyAdapter: CurrencyAdapter = CurrencyAdapter(mCurrencies, this)
        set(value) {
            field = value
            notifyPropertyChanged(BR.currencyAdapter)
        }
        @Bindable
        get() = field


    init {
        getData()
    }

    private fun getData() {
        launch(CommonPool) {
            try {
                dbHelper.openDB()
                var listCurrency = async { dbHelper.getAllCurrency() }.await()
                dbHelper.close()
                currencyAdapter.updateData(listCurrency)
            } catch (e: Exception) {
                Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
            }
        }

    }

    fun onDestroy() {
    }
}