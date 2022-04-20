package com.parallax.partyapp.interfaces

interface DataListener<T> {

    fun onStartCall()

    fun onResponse(responseObj: T?)

    fun onError(errorMessage: String)
}