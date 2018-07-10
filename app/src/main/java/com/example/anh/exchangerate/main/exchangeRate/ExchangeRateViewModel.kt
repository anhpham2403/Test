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
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.text.SimpleDateFormat
import java.util.*


class ExchangeRateViewModel(context: Context) : BaseObservable() {
    private var mContext: Context = context
    private val dbHelper: DBHelper = DBHelper(mContext)
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
                getData(true, valueRate1)
            } else if (intent.action == "isCurrency2") {
                currency2 = intent.getParcelableExtra(ChooseCurrencyViewModel.EXTRA_DATA)
                valueRate2 = intent.getDoubleExtra(CalculatorViewModel.EXTRA_KQ, valueRate2)
                getData(false, valueRate2)
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
                    }

                    override fun onResponse(call: Call<JsonElement>?, response: Response<JsonElement>?) {
                        rate1 = response!!.body()!!.asJsonObject.get(query1.toString()).asJsonObject.get(date).asDouble
                        rate2 = response!!.body()!!.asJsonObject.get(query2.toString()).asJsonObject.get(date).asDouble
                        valueRate2 = valueRate1 / rate2
                    }
                })
            } catch (e: Exception) {
                Toast.makeText(mContext, e.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun getData(isCurrency1: Boolean, valueRate: Double) {
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
                    }

                    override fun onResponse(call: Call<JsonElement>?, response: Response<JsonElement>?) {
                        rate1 = response!!.body()!!.asJsonObject.get(query1.toString()).asJsonObject.get(date).asDouble
                        rate2 = response!!.body()!!.asJsonObject.get(query2.toString()).asJsonObject.get(date).asDouble
                        if (isCurrency1) {
                            valueRate2 = valueRate1 / rate2
                        } else {
                            valueRate1 = valueRate2 / rate1

                        }
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

    fun onClickItemCurrency(isCurrency1: Boolean, currency: Currency) {
        mContext.startActivity(CalculatorActivity.getInstance(mContext, isCurrency1, currency))
    }
}