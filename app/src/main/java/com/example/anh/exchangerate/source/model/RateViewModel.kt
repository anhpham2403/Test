package com.example.anh.exchangerate.source.model

class RateViewModel {
    var currency1: Currency? = null
    var rate1: Double = 1.0
    var currency2: Currency? = null
    var rate2: Double = 1.0
    var time: Long = 0

    constructor(currency1: Currency, currency2: Currency) {
        this.currency1 = currency1
        this.currency2 = currency2
    }

    constructor(currency1: Currency, rate1: Double, currency2: Currency, rate2: Double, time: Long) {
        this.currency1 = currency1
        this.rate1 = rate1
        this.currency2 = currency2
        this.rate2 = rate2
        this.time = time
    }
}
