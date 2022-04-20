package com.parallax.partyapp

import android.app.Application
import android.content.Context
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.util.Log
import com.akexorcist.localizationactivity.core.LocalizationApplicationDelegate
import com.facebook.FacebookSdk
import com.facebook.appevents.AppEventsLogger
import com.parallax.partyapp.Api.OkHttpClient
import net.gotev.uploadservice.UploadService
import net.gotev.uploadservice.okhttp.OkHttpStack
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.util.*


class PartyApp : Application() {

    private var localizationDelegate = LocalizationApplicationDelegate()

    companion object {
        lateinit var context: Context
    }

    override fun onCreate() {
        super.onCreate()
        context = applicationContext

        FacebookSdk.sdkInitialize(applicationContext)
        AppEventsLogger.activateApp(this)

        // Setup upload service
        UploadService.NAMESPACE = BuildConfig.APPLICATION_ID
        UploadService.HTTP_STACK = OkHttpStack(OkHttpClient().okHttpClient)

//        printHashKey(applicationContext)
    }

    fun printHashKey(pContext: Context) {
        try {
            val info = pContext.packageManager
                .getPackageInfo(pContext.packageName, PackageManager.GET_SIGNATURES)
            for (signature in info.signatures) {
                val md: MessageDigest = MessageDigest.getInstance("SHA")
                md.update(signature.toByteArray())
                val hashKey = String(android.util.Base64.encode(md.digest(), 0))
                Log.i("Hash Key", "printHashKey() Hash Key: $hashKey")
            }
        } catch (e: NoSuchAlgorithmException) {
            Log.e("Hash Key", "printHashKey()", e)
        } catch (e: Exception) {
            Log.e("Hash Key", "printHashKey()", e)
        }
    }

    override fun attachBaseContext(base: Context) {
//        if (SPreferences.islanguageBurmese()) {
//            localizationDelegate.setDefaultLanguage(base, "my")
//        } else {
        localizationDelegate.setDefaultLanguage(base, Locale.ENGLISH)
//        }
        super.attachBaseContext(localizationDelegate.attachBaseContext(base))
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        localizationDelegate.onConfigurationChanged(this)
    }

    override fun getApplicationContext(): Context {
        return localizationDelegate.getApplicationContext(super.getApplicationContext())
    }
}