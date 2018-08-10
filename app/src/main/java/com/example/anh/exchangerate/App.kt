package com.example.anh.exchangerate

import android.app.Application
import com.example.anh.exchangerate.source.data.remote.AppServiceClient
import com.example.anh.exchangerate.source.model.Rate

class App : Application() {
    var listRate: MutableList<Rate> = arrayListOf()

    override fun onCreate() {
        super.onCreate()
        AppServiceClient.initialize(this)
    }
}