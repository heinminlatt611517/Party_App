package com.parallax.partyapp.views.AppFlow.optional

import android.os.Bundle
import com.parallax.partyapp.R
import com.parallax.partyapp.views.BaseActivity
import kotlinx.android.synthetic.main.activity_terms.*

class TermsActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_terms)

        btnBack.setOnClickListener {
            onBackPressed()
        }
    }
}
