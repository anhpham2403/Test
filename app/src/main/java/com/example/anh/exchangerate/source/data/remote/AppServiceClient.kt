package com.example.anh.exchangerate.source.data.remote

import android.app.Application
import com.example.anh.exchangerate.utils.Constant
import okhttp3.Cache
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.security.cert.CertificateException
import java.util.concurrent.TimeUnit
import javax.net.ssl.*


class AppServiceClient {
    companion object {

        private var sInstance: AppApi? = null
        private const val CONNECTION_TIMEOUT = 60

        @JvmStatic
        fun initialize(application: Application) {
            val trustAllCerts = arrayOf<TrustManager>(object : X509TrustManager {

              override fun getAcceptedIssuers(): Array<java.security.cert.X509Certificate> {
                return arrayOf()
              }
                @Throws(CertificateException::class)
                override fun checkClientTrusted(chain: Array<java.security.cert.X509Certificate>,
                    authType: String) {
                }

                @Throws(CertificateException::class)
                override fun checkServerTrusted(chain: Array<java.security.cert.X509Certificate>,
                    authType: String) {
                }
            })

            // Install the all-trusting trust manager
            val sslContext = SSLContext.getInstance("SSL")
            sslContext.init(null, trustAllCerts, java.security.SecureRandom())

            // Create an ssl socket factory with our all-trusting manager
            val sslSocketFactory = sslContext.socketFactory
            val httpClientBuilder = OkHttpClient.Builder()
            val cacheSize: Long = 10 * 1024 * 1024 // 10 MiB
            httpClientBuilder.cache(Cache(application.cacheDir, cacheSize))
            httpClientBuilder.readTimeout(CONNECTION_TIMEOUT.toLong(), TimeUnit.SECONDS)
            httpClientBuilder.connectTimeout(CONNECTION_TIMEOUT.toLong(), TimeUnit.SECONDS)
            httpClientBuilder.sslSocketFactory(sslSocketFactory, trustAllCerts[0] as X509TrustManager)
            httpClientBuilder.hostnameVerifier { _, _ -> true }
          val builder = Retrofit.Builder().baseUrl(Constant.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
            val retrofit = builder.client(httpClientBuilder.build())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build()
            sInstance =
                    retrofit.create(AppApi::class.java)
        }

        val instance: AppApi
            get() {
                if (sInstance == null) {
                    throw RuntimeException("Need call method AppServiceClient#initialize() first")
                }
                return sInstance as AppApi
            }
    }
}