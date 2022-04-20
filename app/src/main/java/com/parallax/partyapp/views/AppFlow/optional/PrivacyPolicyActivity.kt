package com.parallax.partyapp.views.AppFlow.optional

import android.os.Bundle
import com.parallax.partyapp.R
import com.parallax.partyapp.views.BaseActivity
import kotlinx.android.synthetic.main.activity_privacy_policy.*

class PrivacyPolicyActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_privacy_policy)

        btnBack.setOnClickListener {
            onBackPressed()
        }
    }
}
