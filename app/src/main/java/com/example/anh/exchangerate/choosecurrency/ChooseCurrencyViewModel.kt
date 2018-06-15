package com.example.anh.exchangerate.choosecurrency

import android.content.Context
import android.databinding.BaseObservable
import android.databinding.Bindable
import android.widget.Toast
import com.android.databinding.library.baseAdapters.BR
import com.example.anh.exchangerate.source.data.DBHelper
import com.example.anh.exchangerate.source.model.Currency
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.launch


class ChooseCurrencyViewModel(context: Context) : BaseObservable(), OnItemClick<Currency> {
    private val context: Context = context
    private val dbHelper = DBHelper(context)
    override fun onItemTimelineClick(item: Currency) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
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