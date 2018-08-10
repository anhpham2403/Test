package com.example.anh.exchangerate.source.data

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
    fun getRateLocal(currencyId: String?): Rate {
        val listRate = (context.applicationContext as App).listRate
        for (i in listRate) {
            if (i.currency != null && i.currency!!.id.equals(currencyId)) {
                return i
            }
        }
        return Rate()
    }

    fun getTimeLocal(currencyId: String?): Long {
        val listRate = (context.applicationContext as App).listRate
        for (i in listRate) {
            if (i.currency != null && i.currency!!.id.equals(currencyId)) {
                return i.time
            }
        }
        return 0
    }

    fun getRateServer(context: Context, currency1: Currency, currency2: Currency, call1: CallBack<MutableList<Rate>>) {
        var list: MutableList<Rate> = arrayListOf(Rate(currency1), Rate(currency2))
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
                        list[0].rate = response!!.body()!!.asJsonObject.get(query1.toString()).asJsonObject.get(date).asDouble
                        list[1].rate = response!!.body()!!.asJsonObject.get(query2.toString()).asJsonObject.get(date).asDouble
                        list[0].time = Calendar.getInstance().timeInMillis
                        list[1].time = Calendar.getInstance().timeInMillis
                        call1.onSuccess(list)
                    }
                })
            } catch (e: Exception) {
                call1.onFailure(e.message)
            }
        }
    }

    fun getRateSeverIfNecessary(context: Context, currency1: Currency, currency2: Currency, call1: CallBack<MutableList<Rate>>) {
        if (getTimeLocal(currency1.id) + TIME_RELOAD < Calendar.getInstance().timeInMillis && getTimeLocal(currency2.id) + TIME_RELOAD < Calendar.getInstance().timeInMillis) {
            var list: MutableList<Rate> = arrayListOf(Rate(currency1), Rate(currency2))
            list.add(getRateLocal(currency1.id))
            list.add(getRateLocal(currency2.id))
            call1.onSuccess(list)
        } else {
            getRateServer(context, currency1, currency2, call1)
        }
    }
}
