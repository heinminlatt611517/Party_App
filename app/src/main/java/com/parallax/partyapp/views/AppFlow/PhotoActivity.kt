package com.parallax.partyapp.views.AppFlow

import android.content.Intent
import android.os.Bundle
import com.parallax.partyapp.R
import com.parallax.partyapp.views.BaseActivity
import kotlinx.android.synthetic.main.activity_photo.*

class PhotoActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_photo)

        btnBack.setOnClickListener {
            onBackPressed()
        }

        icPhoto.setOnClickListener {
            startActivity(Intent(applicationContext, MediaActivity::class.java))
        }


    }
}
