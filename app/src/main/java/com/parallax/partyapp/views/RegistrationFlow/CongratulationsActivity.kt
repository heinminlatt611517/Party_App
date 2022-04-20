package com.parallax.partyapp.views.RegistrationFlow

import android.content.Intent
import android.os.Bundle
import com.parallax.partyapp.R
import com.parallax.partyapp.views.AppFlow.MainActivity
import com.parallax.partyapp.views.BaseActivity
import kotlinx.android.synthetic.main.activity_congratulations.*

class CongratulationsActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_congratulations)

        btnStart.setOnClickListener {
            startActivity(Intent(applicationContext, MainActivity::class.java))
        }

    }
}
