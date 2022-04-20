package com.parallax.partyapp.views.AppFlow.optional

import android.content.Intent
import android.os.Bundle
import com.parallax.partyapp.R
import com.parallax.partyapp.views.AppFlow.InviteFriendsActivity
import com.parallax.partyapp.views.BaseActivity
import kotlinx.android.synthetic.main.activity_settings.*
import java.util.*


class SettingsActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)


        btnBack.setOnClickListener {
            onBackPressed()
        }

        layoutPrivacyPolicy.setOnClickListener {
            startActivity(
                Intent(
                    applicationContext,
                    PrivacyPolicyActivity::class.java
                )
            )
        }

        layoutTrems.setOnClickListener {
            startActivity(Intent(applicationContext, TermsActivity::class.java))
        }

        layoutInviteFriend.setOnClickListener {
            startActivity(Intent(applicationContext, InviteFriendsActivity::class.java))
        }

        layoutSendFeedback.setOnClickListener {
            startActivity(Intent(applicationContext, SendFeedbackActivity::class.java))
        }

        layoutContactUs.setOnClickListener {
            startActivity(Intent(applicationContext, ContactUsActivity::class.java))
        }

        layoutContactUs.setOnClickListener {
            startActivity(Intent(applicationContext, ContactUsActivity::class.java))
        }


    }
}
