package com.example.anh.exchangerate.utils

import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.*

class Constant {
  companion object {
    const val BASE_URL = "https://currencyserver240395.herokuapp.com/rest/"
    /*
            const val BASE_URL = "https://currency-server-test.herokuapp.com/rest/"
    */
    @JvmStatic
    var FORMATER = DecimalFormat("###,###,###.##", DecimalFormatSymbols(Locale.ENGLISH))
  }
}
