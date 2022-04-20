package com.parallax.partyapp.Api

import com.parallax.partyapp.Utils.BASE_URL_FB
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST

var fbLoginAPi = FbLoginAPI().apiService

class FbLoginAPI {

    var retrofit: Retrofit = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .client(OkHttpClient().okHttpClient)
        .baseUrl("$BASE_URL_FB/oauth/")
        .build()

    var apiService: FbLogin = retrofit.create(FbLogin::class.java)
}

interface FbLogin {
    @POST("token")
    fun checkToken(@Body bodyFbLogin: BodyFbLogin): Call<ResponseFbLogin>

}


data class BodyFbLogin(
    var grant_type: String = "facebook_login",
    var fb_token: String? = null,
    var client_id: String = "2",
    var client_secret: String = "z57aTWDkJ2pUHFW4g4Mm3LAwxS2Pzs9tfRdunuJq"
)


data class ResponseFbLogin(
    val access_token: String,
    val expires_in: Int,
    val refresh_token: String,
    val token_type: String
)