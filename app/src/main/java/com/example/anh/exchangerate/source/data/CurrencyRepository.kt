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
      if ((i.currency1 != null && i.currency1!!.id.equals(
          currencyId1)) && i.currency2 != null && i.currency2!!.id.equals(currencyId2)) {
        return i
      }
      if ((i.currency1 != null && i.currency1!!.id.equals(
          currencyId2)) && i.currency2 != null && i.currency2!!.id.equals(currencyId1)) {
        return Rate(i.currency2!!, i.rate2, i.currency1!!, i.rate1, i.time)
      }
    }
    return Rate()
  }

  private fun getTimeLocal(currencyId1: String?, currencyId2: String?): Long {
    val listRate = (context.applicationContext as App).listRate
    return listRate
        .firstOrNull {
          ((it.currency1 != null && it.currency1!!.id.equals(
              currencyId2)) && it.currency2 != null && it.currency2!!.id.equals(currencyId1)) ||
              ((it.currency1 != null && it.currency1!!.id.equals(
                  currencyId1)) && it.currency2 != null && it.currency2!!.id.equals(currencyId2))
        }
        ?.time
        ?: 0
  }

  fun getRateServer(context: Context, currency1: Currency, currencies2: List<Currency>,
      call1: CallBack<List<Rate>>) {
    launch(CommonPool) {
      try {
        var rates: List<Rate>
        var queries: MutableList<String> = arrayListOf()
        currencies2.mapTo(queries) { it.id!!.toUpperCase() }
        val callback = withContext(DefaultDispatcher) {
          AppServiceClient.instance.getExchangeRate(currency1.id!!.toUpperCase(), queries)
        }
        callback.enqueue(object : Callback<List<Rate>> {
          override fun onFailure(call: Call<List<Rate>>?, t: Throwable?) {
            call1.onFailure(t?.message)
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
              listRate
                  .filter {
                    ((it.currency1?.id == rate.currency1?.id && it.currency2?.id == rate.currency2?.id)
                        || (it.currency1?.id == rate.currency2?.id && it.currency2?.id == rate.currency1?.id))
                  }
                  .forEach { listRate.remove(it) }
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

  fun getRateSeverIfNecessary(context: Context, currency1: Currency, currency2: Currency,
      call1: CallBack<List<Rate>>) {
    if (getTimeLocal(currency1.id,
        currency2.id) + TIME_RELOAD >= Calendar.getInstance().timeInMillis) {
      var list: Rate = getRateLocal(currency1.id, currency2.id)
      call1.onSuccess(listOf(list))
    } else {
      getRateServer(context, currency1, listOf(currency2), call1)
    }
  }

  fun getHistoryRate(currency1: Currency, currency2: Currency, priority: String,
      from: Long, to: Long, call1: CallBack<List<Rate>>) {
    launch(CommonPool) {
      try {
        var rates: List<Rate>
        var queries: MutableMap<String, String?> = HashMap()
        queries.put("id", currency2.id!!.toLowerCase())
        queries.put("from", from.toString())
        queries.put("to", to.toString())
        queries.put("priority", priority)
        val callback = withContext(DefaultDispatcher) {
          AppServiceClient.instance.getHistoryRate(currency1.id!!.toLowerCase(), queries)
        }
        callback.enqueue(object : Callback<List<Rate>> {
          override fun onFailure(call: Call<List<Rate>>?, t: Throwable?) {
            call1.onFailure(t?.message)
          }

          override fun onResponse(call: Call<List<Rate>>?, response: Response<List<Rate>>?) {
            rates = response!!.body()!!
            for (i in rates.indices) {
              rates[i].currency1 = currency1
              rates[i].currency2 = currency2
            }
            call1.onSuccess(rates)
          }
        })
      } catch (e: Exception) {
        call1.onFailure(e.message)
      }
    }
  }
}
