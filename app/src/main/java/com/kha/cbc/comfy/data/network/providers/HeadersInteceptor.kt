package com.kha.cbc.comfy.data.network.providers

import com.kha.cbc.comfy.BuildConfig
import com.sina.weibo.sdk.utils.MD5
import okhttp3.Interceptor

fun computeSign():String{
    val timestamp = System.currentTimeMillis()
    val timestamp_string = timestamp.toString()
    val original_string = timestamp_string + BuildConfig.LEANCLOUDAPPKEY
    val send = MD5.hexdigest(original_string) + ',' + timestamp_string
    return send
}

fun makeHeadersInterceptor() = Interceptor{ chain: Interceptor.Chain ->
    chain.proceed(
        chain.request().newBuilder()
            .addHeader("X-LC-Id", BuildConfig.LEANCLOUDAPPID)
            .addHeader("X-LC-Sign", computeSign())
            .addHeader("Content-Type", "application/json")
            .build())
}

//kotlin lambda with multiple lines

//fun magnitude100String() = { input : Int ->
//    val magnitude = input * 100
//    val new = "asdfasdfasdf"
//    magnitude.toString()
//}