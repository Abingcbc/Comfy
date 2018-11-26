package com.kha.cbc.comfy.data.network.providers

import com.kha.cbc.comfy.BuildConfig
import okhttp3.logging.HttpLoggingInterceptor


fun makeLoggingInterceptor() = HttpLoggingInterceptor().apply {
    level = if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY
    else HttpLoggingInterceptor.Level.NONE
}