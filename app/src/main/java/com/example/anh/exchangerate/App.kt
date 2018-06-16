package com.example.anh.exchangerate

import android.app.Application
import com.example.anh.exchangerate.source.data.remote.AppServiceClient

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        AppServiceClient.initialize(this)
    }
}