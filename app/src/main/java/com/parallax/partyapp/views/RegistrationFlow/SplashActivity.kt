package com.parallax.partyapp.views.RegistrationFlow

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import com.parallax.partyapp.Data.SPreferences
import com.parallax.partyapp.R
import com.parallax.partyapp.views.AppFlow.MainActivity
import com.parallax.partyapp.views.BaseActivity

class SplashActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(android.R.style.Theme_Material_Light_NoActionBar_Fullscreen)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)


    }

    override fun onResume() {
        super.onResume()

        goWelcomeActivityWithDelay()


    }

    private fun goWelcomeActivityWithDelay() {
        Handler().postDelayed({

            if (SPreferences.isFirstTime()) {
                SPreferences.setFirstTimeFalse()
                startActivity(Intent(applicationContext, IntroActivity::class.java))
            } else {
                if (SPreferences.getUserToken() != null) {
                    startActivity(Intent(applicationContext, MainActivity::class.java))
                    finish()
                } else {
                    startActivity(Intent(applicationContext, WelcomeActivity::class.java))
                }
            }


            finish()
        }, 1300)
    }
}
