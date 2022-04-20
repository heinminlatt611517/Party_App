package com.parallax.partyapp.views.AppFlow

import android.os.Bundle
import com.parallax.partyapp.R
import com.parallax.partyapp.views.BaseActivity
import kotlinx.android.synthetic.main.activity_invite_friends.*

class InviteFriendsActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_invite_friends)

        btnBack.setOnClickListener {
            onBackPressed()
        }
    }
}
