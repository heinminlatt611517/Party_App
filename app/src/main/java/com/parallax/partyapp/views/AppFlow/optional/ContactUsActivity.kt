package com.parallax.partyapp.views.AppFlow.optional

import android.app.Dialog
import android.os.Bundle
import com.parallax.partyapp.R
import com.parallax.partyapp.views.BaseActivity
import kotlinx.android.synthetic.main.activity_contact_us.*
import kotlinx.android.synthetic.main.dialog_pass_not_match.*

class ContactUsActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contact_us)

        btnBack.setOnClickListener {
            onBackPressed()
        }

        btnSend.setOnClickListener {
            dialogContactUs()
        }
    }

    private fun dialogContactUs() {

        val dialog = Dialog(this)
        dialog.setContentView(R.layout.dialog_contact_us)

        dialog.btnTryAgain.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }
}
