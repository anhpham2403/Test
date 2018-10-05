package com.example.anh.exchangerate.source.data.remote

import com.example.anh.exchangerate.source.model.Rate
import com.google.gson.JsonElement
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.QueryMap


interface AppApi {
    @GET("rate/{id_base}")
    fun getExchangeRate(@Path("id_base") idBase: String, @QueryMap queries: Map<String, String>): Call<List<Rate>>
}