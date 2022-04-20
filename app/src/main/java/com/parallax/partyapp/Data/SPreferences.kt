package com.parallax.partyapp.Data

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import com.google.gson.Gson
import com.parallax.partyapp.Models.ResponseGetProfile
import com.parallax.partyapp.PartyApp

object SPreferences {

    private val SP_USER_OBJ = "loginObj"
    private val SP_USER_TOKEN = "loginToken"
    private val SP_FIRST_TIME_APP_LAUNCH = "firstTimeLaunch"
    private val SP_IS_LANGIAGE_BURMSE = "SP_IS_LANGIAGE_BURMSE"
    private val USER_ID = "userId"

    private fun getSharedPreferences(context: Context): SharedPreferences {

        return PreferenceManager.getDefaultSharedPreferences(context)
    }

    fun isFirstTime(): Boolean {
        return getSharedPreferences(PartyApp.context).getBoolean(SP_FIRST_TIME_APP_LAUNCH, true)
    }

    fun setFirstTimeFalse() {
        getSharedPreferences(PartyApp.context).edit().putBoolean(SP_FIRST_TIME_APP_LAUNCH, false)
            .apply()
    }

    fun setUserObj(loginObj: String?) {
        getSharedPreferences(PartyApp.context).edit().putString(SP_USER_OBJ, loginObj).apply()
    }

    fun getUserObj(): String? {
        return getSharedPreferences(PartyApp.context).getString(SP_USER_OBJ, null)
    }

    fun setIslanguageBurmese(isLanguageBurmese: Boolean) {
        getSharedPreferences(PartyApp.context).edit().putBoolean(SP_IS_LANGIAGE_BURMSE, isLanguageBurmese).apply()
    }

    fun islanguageBurmese(): Boolean {
        return getSharedPreferences(PartyApp.context).getBoolean(SP_IS_LANGIAGE_BURMSE, false)
    }

    fun getUserProfileObj(): ResponseGetProfile? {
        return Gson().fromJson(
            getUserObj(),
            ResponseGetProfile::class.java
        )
    }

    fun setUserToken(loginToken: String?) {
        getSharedPreferences(PartyApp.context).edit().putString(SP_USER_TOKEN, loginToken).apply()
    }

    fun getUserToken(): String? {
        return getSharedPreferences(PartyApp.context).getString(SP_USER_TOKEN, null)
    }

    fun setUserFacebookId(ID: String?) {
        getSharedPreferences(PartyApp.context).edit().putString(USER_ID, ID).apply()
    }

    fun getUserFacebookId(): String? {
        return getSharedPreferences(PartyApp.context).getString(USER_ID, null)
    }
}