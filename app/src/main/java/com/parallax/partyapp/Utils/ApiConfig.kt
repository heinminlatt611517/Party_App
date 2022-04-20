package com.parallax.partyapp.Utils

import android.view.View
import com.parallax.partyapp.Api.ApiServices
import com.parallax.partyapp.Api.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

var apiServices = ApiConfig().apiServices

class ApiConfig {

    var retrofit: Retrofit = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .client(OkHttpClient().okHttpClient)
        .baseUrl(API_BASE_URL)
        .build()

    var apiServices: ApiServices = retrofit.create(ApiServices::class.java)
}


fun View.isVisible(): Boolean = visibility == View.VISIBLE


fun View.invisible() {
    visibility = View.INVISIBLE
}

fun View.hide() {
    visibility = View.GONE
}

fun View.show() {
    visibility = View.VISIBLE
}