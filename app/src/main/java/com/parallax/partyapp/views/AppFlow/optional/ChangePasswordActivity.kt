package com.parallax.partyapp.views.AppFlow.optional

import android.app.Dialog
import android.os.Bundle
import android.view.MotionEvent
import com.parallax.partyapp.R
import com.parallax.partyapp.views.BaseActivity
import hideKeyboard
import kotlinx.android.synthetic.main.activity_change_password.*
import kotlinx.android.synthetic.main.dialog_pass_not_match.*

class ChangePasswordActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_password)


        btnchangePassword.setOnClickListener {
            dialogChangePassword()
        }

        btnBack.setOnClickListener {
            onBackPressed()
        }
    }

    private fun dialogChangePassword() {

        val dialog = Dialog(this)
        dialog.setContentView(R.layout.dialog_change_password)

        dialog.btnTryAgain.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        hideKeyboard(this)
        return super.dispatchTouchEvent(ev)
    }
}
