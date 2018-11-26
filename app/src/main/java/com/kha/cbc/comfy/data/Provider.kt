package com.kha.cbc.comfy.data

abstract class Provider<T> {
    abstract fun creator(): T

    private val instance: T by lazy { creator() }

    private var testingInstance: T? = null

    fun get(): T = testingInstance ?: instance
}