package com.example.anh.exchangerate.calculator

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.databinding.BaseObservable
import android.databinding.Bindable
import android.support.v4.content.LocalBroadcastManager
import com.android.databinding.library.baseAdapters.BR
import com.example.anh.exchangerate.choosecurrency.ChooseCurrency
import com.example.anh.exchangerate.choosecurrency.ChooseCurrencyViewModel
import com.example.anh.exchangerate.source.model.Currency
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.*


class CalculatorViewModel(context: Context, isCurrency1: Boolean, currency1: Currency) : BaseObservable() {
    var isCurrency1 = isCurrency1
        @Bindable
        get() = field
        set(value) {
            field = value
            notifyPropertyChanged(BR.currency1)
        }
    var currency: Currency = currency1
        @Bindable
        get() = field
        set(value) {
            field = value
            notifyPropertyChanged(BR.currency)
        }
    private var mContext: Context = context
    var value: String = "0"
        @Bindable
        get() = field
        set(value) {
            field = value
            notifyPropertyChanged(BR.value)
        }
    var kg: Double = 0.0
    var isEqual = true
    private var localBroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            currency = intent.getParcelableExtra(ChooseCurrencyViewModel.EXTRA_DATA)
        }
    }

    init {
        val filter = IntentFilter()
        filter.addAction("isCurrency1")
        filter.addAction("isCurrency2")
        LocalBroadcastManager.getInstance(mContext).registerReceiver(localBroadcastReceiver, filter)
    }

    fun onClickFrom(isCurrency1: Boolean) {
        mContext.startActivity(ChooseCurrency.getInstance(mContext, isCurrency1))
    }

    fun onDestroy() {
        LocalBroadcastManager.getInstance(mContext).unregisterReceiver(localBroadcastReceiver)
    }

    fun operationButtonClick(s: Char) {
        if (value.endsWith("+") || value.endsWith("-") || value.endsWith("÷") || value.endsWith("×") || value.isEmpty()) {
            return
        }
        value += s
    }

    fun numberButtonClick(s: Char) {
        if (value.equals("0")) {
            value = ""
        }
        value += s
    }

    fun commaButtonClick() {
        if (!value.contains(".")) {
            value += if (value.endsWith("+") || value.endsWith("-") || value.endsWith("÷") || value.endsWith("×") || value.isEmpty()) {
                "0."
            } else {
                "."
            }
        }
    }

    fun equalButtonClick() {
        var number: MutableList<String> = arrayListOf()
        var stack: Stack<Char> = Stack()
        if (value.isEmpty()) {
            kg = 0.0
        } else {
            if (isOperator(value[value.length - 1])) {
                value += "0"
            }
            var dem = 0
            for (i in value) {
                if (isOperator(i)) {
                    dem++
                    if (!stack.isEmpty() && priority(stack.peek()) > priority(i)) {
                        number.add(stack.pop().toString())
                    }
                    stack.add(i)
                } else {
                    if (number.size > dem) {
                        number[dem] = number[dem] + i.toString()
                    } else {
                        number.add(dem, i.toString())

                    }
                }
            }
            dem++
            var s = ""
            for (j in stack) {
                s += j.toString()
            }
            if (!s.isEmpty()) {
                number.add(dem, s)
            }
        }
        for (j in number.indices) {
            if (number[j].length == 1 && isOperator(number[j].single())) {
                var s1 = number[j - 1].replace(",".toRegex(), "")
                var s2 = number[j - 2].replace(",".toRegex(), "")
                when (number[j]) {
                    "+" -> {
                        kg = s2.toDouble() + s1.toDouble()
                    }
                    "-" -> {
                        kg = s2.toDouble() - s1.toDouble()
                    }
                    "÷" -> {
                        kg = s2.toDouble() / s1.toDouble()
                    }
                    "×" -> {
                        kg = s2.toDouble() * s1.toDouble()
                    }
                }
            }
        }
        value = priceWithoutDecimal(kg)
    }

    private fun isOperator(c: Char): Boolean {
        val operator = charArrayOf('+', '-', '×', '÷')
        Arrays.sort(operator)
        return Arrays.binarySearch(operator, c) > -1
    }

    private fun priority(s: Char): Int {
        return if (s.equals("+") || s.equals("-")) {
            1
        } else {
            2
        }
    }

    fun deleteButtonClick() {
        if (value.isEmpty()) {
            return
        }
        var stringBuilder = StringBuilder(value)
        value = stringBuilder.deleteCharAt(value.length - 1).toString()
    }

    private fun priceWithoutDecimal(price: Double?): String {
        val formatter = DecimalFormat("###,###,###.##", DecimalFormatSymbols(Locale.ENGLISH))
        return formatter.format(price)
    }
}
