package com.example.anh.exchangerate.source.model

import android.databinding.BaseObservable
import android.databinding.Bindable
import com.android.databinding.library.baseAdapters.BR
import com.example.anh.exchangerate.utils.Constant.Companion.FORMATER

class Rate() : BaseObservable() {

    var currency1: Currency? = null
        @Bindable
        get() = field
        set(value) {
            field = value
            notifyPropertyChanged(BR.currency1)
        }
    var rate1: Double = 1.0
        @Bindable
        get() = field
        set(value) {
            field = value
            notifyPropertyChanged(BR.rate1)
        }
    var currency2: Currency? = null
        @Bindable
        get() = field
        set(value) {
            field = value
            notifyPropertyChanged(BR.currency2)
        }
    var rate2: Double = 1.0
        @Bindable
        get() = field
        set(value) {
            field = value
            notifyPropertyChanged(BR.rate2)
        }
    var time: Long = 0
        @Bindable
        get() = field
        set(value) {
            field = value
            notifyPropertyChanged(BR.time)
        }
    @Bindable
    var exchange: String = "1 " + currency1!!.currencyName + " = " + FORMATER.format(rate2) + " " + currency2!!.currencyName

    constructor(currency1: Currency, rate1: Double, currency2: Currency, rate2: Double, time: Long) : this() {
        this.currency1 = currency1
        this.rate1 = rate1
        this.currency2 = currency2
        this.rate2 = rate2
        this.time = time
    }

    constructor(rateResponse: RateViewModel) : this() {
        this.currency1 = rateResponse.currency1
        this.currency2 = rateResponse.currency2
        this.rate2 = rateResponse.rate2
        this.rate1 = rateResponse.rate1
        this.time = rateResponse.time
    }
}