package com.example.anh.exchangerate.source.data

import android.app.Activity
import android.content.Context
import com.example.anh.exchangerate.App
import com.example.anh.exchangerate.source.CallBack
import com.example.anh.exchangerate.source.data.remote.AppServiceClient
import com.example.anh.exchangerate.source.model.Currency
import com.example.anh.exchangerate.source.model.Rate
import com.google.gson.JsonElement
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*

class CurrencyRepository(context: Context) {
    companion object {
        const val TIME_RELOAD = 60000
    }

    private var context = context
    fun getRateLocal(currencyId1: String?, currencyId2: String?): Rate {
        val listRate = (context.applicationContext as App).listRate
        for (i in listRate) {
            if ((i.currency1 != null && i.currency1!!.id.equals(currencyId1)) && i.currency2 != null && i.currency2!!.id.equals(currencyId2)) {
                return i
            }
            if ((i.currency1 != null && i.currency1!!.id.equals(currencyId2)) && i.currency2 != null && i.currency2!!.id.equals(currencyId1)) {
                return Rate(i.currency2!!, i.rate2, i.currency1!!, i.rate1, i.time)
            }
        }
        return Rate()
    }

    fun getTimeLocal(currencyId1: String?, currencyId2: String?): Long {
        val listRate = (context.applicationContext as App).listRate
        for (i in listRate) {
            if (((i.currency1 != null && i.currency1!!.id.equals(currencyId2)) && i.currency2 != null && i.currency2!!.id.equals(currencyId1)) ||
                    ((i.currency1 != null && i.currency1!!.id.equals(currencyId1)) && i.currency2 != null && i.currency2!!.id.equals(currencyId2))) {
                return i.time
            }
        }
        return 0
    }

    fun getRateServer(context: Context, currency1: Currency, currency2: Currency, call1: CallBack<Rate>) {
        var rate: Rate = Rate(currency1, currency2)
        launch(CommonPool) {
            try {
                if (currency1.id == null || currency2.id == null) {
                    (context as Activity).runOnUiThread({
                        call1.onFailure("null id")
                    })
                } else {
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
                            rate.rate1 = response!!.body()!!.asJsonObject.get(query1.toString()).asJsonObject.get(date).asDouble
                            rate.rate2 = response!!.body()!!.asJsonObject.get(query2.toString()).asJsonObject.get(date).asDouble
                            rate.time = Calendar.getInstance().timeInMillis
                            var listRate = (context.applicationContext as App).listRate
                            for (rateList: Rate in listRate) {
                                if ((rateList.currency1?.id == rate.currency1?.id && rateList.currency2?.id == rate.currency2?.id)
                                        || (rateList.currency1?.id == rate.currency2?.id && rateList.currency2?.id == rate.currency1?.id)) {
                                    listRate.remove(rateList)
                                    listRate.add(rate)
                                    call1.onSuccess(rate)
                                    return
                                }
                            }
                            listRate.add(rate)
                            call1.onSuccess(rate)
                        }
                    })
                }
            } catch (e: Exception) {
                call1.onFailure(e.message)
            }
        }
    }

    fun getRateSeverIfNecessary(context: Context, currency1: Currency, currency2: Currency, call1: CallBack<Rate>) {
        if (getTimeLocal(currency1.id, currency2.id) + TIME_RELOAD >= Calendar.getInstance().timeInMillis) {
            var list: Rate = getRateLocal(currency1.id, currency2.id)
            call1.onSuccess(list)
        } else {
            getRateServer(context, currency1, currency2, call1)
        }
    }
}
