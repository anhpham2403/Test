package com.example.anh.exchangerate.source.model

class Rate() {
    var currency: Currency? = null
    var rate: Double = 0.0
    var time: Long =0
    constructor(currency: Currency) : this() {
        this.currency = currency
    }

    constructor(currency: Currency, rate: Double) : this() {
        this.currency = currency
        this.rate = rate
    }
}