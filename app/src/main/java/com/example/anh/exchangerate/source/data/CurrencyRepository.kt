package com.example.anh.exchangerate.source.data

import android.content.Context
import com.example.anh.exchangerate.App
import com.example.anh.exchangerate.source.CallBack
import com.example.anh.exchangerate.source.data.remote.AppServiceClient
import com.example.anh.exchangerate.source.model.Currency
import com.example.anh.exchangerate.source.model.Rate
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.DefaultDispatcher
import kotlinx.coroutines.experimental.launch
import kotlinx.coroutines.experimental.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception
import java.util.*
import kotlin.collections.HashMap

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

    fun getRateServer(context: Context, currency1: Currency, currencies2: List<Currency>, call1: CallBack<List<Rate>>) {
        var rates: MutableList<Rate>
        launch(CommonPool) {
            try {
                var rates: List<Rate>
                var queries: MutableMap<String, String> = HashMap()
                for (currency in currencies2) {
                    queries["id"] = currency.id!!.toUpperCase()
                }
                val callback = withContext(DefaultDispatcher) {
                    AppServiceClient.instance.getExchangeRate(currency1.id!!.toUpperCase(), queries)
                }
                callback.enqueue(object : Callback<List<Rate>> {
                    override fun onFailure(call: Call<List<Rate>>?, t: Throwable?) {
                    }

                    override fun onResponse(call: Call<List<Rate>>?, response: Response<List<Rate>>?) {
                        rates = response!!.body()!!
                        for (i in rates.indices) {
                            rates[i].currency1 = currency1
                            rates[i].currency2 = currencies2[i]
                            rates[i].time = Calendar.getInstance().timeInMillis
                        }

                        var listRate = (context.applicationContext as App).listRate
                        for (rate in rates) {
                            for (rateList: Rate in listRate) {
                                if ((rateList.currency1?.id == rate.currency1?.id && rateList.currency2?.id == rate.currency2?.id)
                                        || (rateList.currency1?.id == rate.currency2?.id && rateList.currency2?.id == rate.currency1?.id)) {
                                    listRate.remove(rateList)
                                }
                            }
                        }
                        listRate.addAll(rates)
                        call1.onSuccess(rates)
                    }
                })
            } catch (e: Exception) {
                call1.onFailure(e.message)
            }
        }
    }

    fun getRateSeverIfNecessary(context: Context, currency1: Currency, currency2: Currency, call1: CallBack<List<Rate>>) {
        if (getTimeLocal(currency1.id, currency2.id) + TIME_RELOAD >= Calendar.getInstance().timeInMillis) {
            var list: Rate = getRateLocal(currency1.id, currency2.id)
            call1.onSuccess(listOf(list))
        } else {
            getRateServer(context, currency1, listOf(currency2), call1)
        }
    }
}
