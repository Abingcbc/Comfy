package com.kha.cbc.comfy.data.network.providers

import com.google.gson.Gson
import com.kha.cbc.comfy.BuildConfig
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

val retrofit by lazy { makeRetrofit() }
val baseUrl by lazy {
    "https://" + BuildConfig.LEANCLOUDAPPID.substring(0, 7) + ".api.lncld.net/1.1/"
}

private fun makeRetrofit(): Retrofit = Retrofit.Builder()
    .baseUrl(baseUrl)
    .client(makeHttpClient())
    .addConverterFactory(GsonConverterFactory.create(Gson()))
    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
    .build()


private fun makeHttpClient() = OkHttpClient.Builder()
    .connectTimeout(60, TimeUnit.SECONDS)
    .readTimeout(60, TimeUnit.SECONDS)
    .addInterceptor(makeHeadersInterceptor())
    .addInterceptor(makeLoggingInterceptor())
    .build()