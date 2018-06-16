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
import com.example.anh.exchangerate.choosecurrency.ChooseCurrency
import com.example.anh.exchangerate.choosecurrency.ChooseCurrencyViewModel
import com.example.anh.exchangerate.source.data.local.sqlite.DBHelper
import com.example.anh.exchangerate.source.data.remote.AppServiceClient
import com.example.anh.exchangerate.source.model.Currency
import com.google.gson.JsonElement
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*


class ExchangeRateViewModel(context: Context) : BaseObservable() {
    private var mContext: Context = context
    private val dbHelper: DBHelper = DBHelper(mContext)
    var rate1: Float = 0f
        @Bindable
        get() = field
        set(value) {
            field = value
            notifyPropertyChanged(BR.rate1)
        }
    var rate2: Float = 0f
        @Bindable
        get() = field
        set(value) {
            field = value
            notifyPropertyChanged(BR.rate2)
        }
    var valueRate1: Float = 1f
        @Bindable
        get() = field
        set(value) {
            field = value
            notifyPropertyChanged(BR.valueRate1)
        }
    var valueRate2: Float = 1f
        @Bindable
        get() = field
        set(value) {
            field = value
            notifyPropertyChanged(BR.valueRate2)
        }
    private var localBroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            if (intent.action == "isCurrency1") {
                currency1 = intent.getParcelableExtra(ChooseCurrencyViewModel.EXTRA_DATA)
            } else if (intent.action == "isCurrency2") {
                currency2 = intent.getParcelableExtra(ChooseCurrencyViewModel.EXTRA_DATA)
            }
            getData()
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
                var listBaseCurrency: MutableList<Currency> = async { dbHelper.getBaseCurrency("USD", "EUR") }.await()
                currency1 = listBaseCurrency[0]
                currency2 = listBaseCurrency[1]
                dbHelper.close()
                val query1 = StringBuilder()
                query1.append(currency1.id, "_", currency2.id)
                val query2 = StringBuilder()
                query2.append(currency2.id, "_", currency1.id)
                val format = SimpleDateFormat("yyyy-MM-dd")
                val date = format.format(Calendar.getInstance().time)
                val callback = async {
                    AppServiceClient.instance.getExchangeRate(query1.toString() + "," + query2.toString(), "ultra", date)
                }.await()
                callback.enqueue(object : Callback<JsonElement> {
                    override fun onFailure(call: Call<JsonElement>?, t: Throwable?) {
                        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                    }

                    override fun onResponse(call: Call<JsonElement>?, response: Response<JsonElement>?) {
                        rate1 = response!!.body()!!.asJsonObject.get(query1.toString()).asJsonObject.get(date).asFloat
                        rate2 = response!!.body()!!.asJsonObject.get(query2.toString()).asJsonObject.get(date).asFloat
                        valueRate2 *= rate1

                    }
                })
            } catch (e: Exception) {
                Toast.makeText(mContext, e.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun getData() {
        launch(CommonPool) {
            try {
                val query1 = StringBuilder()
                query1.append(currency1.id, "_", currency2.id)
                val query2 = StringBuilder()
                query2.append(currency2.id, "_", currency1.id)
                val format = SimpleDateFormat("yyyy-MM-dd")
                val date = format.format(Calendar.getInstance().time)
                val callback = async {
                    AppServiceClient.instance.getExchangeRate(query1.toString() + "," + query2.toString(), "ultra", date)
                }.await()
                callback.enqueue(object : Callback<JsonElement> {
                    override fun onFailure(call: Call<JsonElement>?, t: Throwable?) {
                        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                    }

                    override fun onResponse(call: Call<JsonElement>?, response: Response<JsonElement>?) {
                        rate1 = response!!.body()!!.asJsonObject.get(query1.toString()).asJsonObject.get(date).asFloat
                        rate2 = response!!.body()!!.asJsonObject.get(query2.toString()).asJsonObject.get(date).asFloat
                        valueRate2 *= rate1
                    }
                })
            } catch (e: Exception) {
                Toast.makeText(mContext, e.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun onClickFrom(isCurrency1: Boolean) {
        mContext.startActivity(ChooseCurrency.getInstance(mContext, isCurrency1))
    }

    fun onDestroy() {
        LocalBroadcastManager.getInstance(mContext).unregisterReceiver(localBroadcastReceiver)
    }
}