package com.parallax.partyapp.Api

import android.util.Log
import com.parallax.partyapp.Data.SPreferences
import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit

class OkHttpClient {
    // Create a trust manager that does not validate certificate chains
    // Install the all-trusting trust manager
    // Create an ssl socket factory with our all-trusting manager
    val okHttpClient: OkHttpClient
        get() {
            try {
                val builder = OkHttpClient.Builder()

                builder.connectTimeout(30, TimeUnit.SECONDS)
                builder.readTimeout(30, TimeUnit.SECONDS)
                builder.writeTimeout(30, TimeUnit.SECONDS)

                builder.addInterceptor { chain ->

                    val authorization = SPreferences.getUserToken()
                    Log.d("UserAUTH", "" + authorization)

                    val newRequest = chain.request().newBuilder()
                        .addHeader("X-localization", "en")
                        .addHeader("Authorization", authorization ?: "")
                        .addHeader("Accept", "application/json")
                        .build()

                    val response = chain.proceed(newRequest)

                    return@addInterceptor response
                }
                return builder.build()
            } catch (e: Exception) {
                throw RuntimeException(e)
            }
        }
}