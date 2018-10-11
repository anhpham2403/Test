package com.example.anh.exchangerate.source.data.remote

import com.example.anh.exchangerate.source.model.Rate
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.QueryMap


interface AppApi {
  @GET("rate/{id_base}")
  fun getExchangeRate(@Path("id_base") idBase: String, @Query(
      "id") queries: List<String?>): Call<List<Rate>>

  @GET("history/{id_base}")
  fun getHistoryRate(@Path(
      "id_base") idBase: String?, @QueryMap queries: MutableMap<String, String?>): Call<List<Rate>>
}