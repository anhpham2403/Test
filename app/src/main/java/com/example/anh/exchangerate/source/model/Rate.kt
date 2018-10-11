package com.example.anh.exchangerate.source.model

import android.databinding.BaseObservable
import android.databinding.Bindable
import com.android.databinding.library.baseAdapters.BR
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.*

class Rate() : BaseObservable() {
  var currency1: Currency? = null
    @Bindable
    get() {
      return field
    }
    set(value) {
      field = value
      notifyPropertyChanged(BR.currency1)
    }
  var rate1: Double = 1.0
    @Bindable
    get() {
      return field
    }
    set(value) {
      field = value
      notifyPropertyChanged(BR.rate1)
    }
  var currency2: Currency? = null
    @Bindable
    get() {
      return field
    }
    set(value) {
      field = value
      notifyPropertyChanged(BR.currency2)
    }
  var rate2: Double = 1.0
    @Bindable
    get() {
      return field
    }
    set(value) {
      field = value
      notifyPropertyChanged(BR.rate2)
    }
  var time: Long = 0

  constructor(currency1: Currency, currency2: Currency) : this() {
    this.currency1 = currency1
    this.currency2 = currency2
  }

  constructor(currency1: Currency, rate1: Double, currency2: Currency, rate2: Double,
      time: Long) : this() {
    this.currency1 = currency1
    this.rate1 = rate1
    this.currency2 = currency2
    this.rate2 = rate2
    this.time = time
  }

  @Bindable
  val formatter1 = DecimalFormat("###,###,###.####", DecimalFormatSymbols(Locale.ENGLISH))
}