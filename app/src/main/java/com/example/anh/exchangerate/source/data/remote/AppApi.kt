package com.example.anh.exchangerate.source.data.remote

import com.google.gson.JsonElement
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query


interface AppApi {
    @GET("convert")
    fun getExchangeRate(@Query("q") q: String, @Query("compact") compact: String, @Query("date") date: String): Call<JsonElement>
}