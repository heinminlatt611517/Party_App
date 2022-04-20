package com.parallax.partyapp.views.RegistrationFlow

import android.content.Intent
import android.os.Bundle
import android.view.MotionEvent
import com.parallax.partyapp.Api.WebService
import com.parallax.partyapp.Models.PostResetPass
import com.parallax.partyapp.Models.ResponseSetFilter
import com.parallax.partyapp.R
import com.parallax.partyapp.interfaces.DataListener
import com.parallax.partyapp.views.BaseActivity
import hideKeyboard
import hideProgressBar
import kotlinx.android.synthetic.main.activity_forget_pass_confirm.*
import showAlertDialog
import showProgressBar

class ForgetPassConfirmActivity : BaseActivity() {

    companion object {
        val KEY_EMAIL = "key email"
    }

    var email: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forget_pass_confirm)

        email = intent?.getStringExtra(KEY_EMAIL)


        btnBack.setOnClickListener {
            onBackPressed()
        }

        btn_forget_password.setOnClickListener {
            if (et_code.text.toString().isNullOrBlank()) {
                showAlertDialog(this, getString(R.string.please_enter_code_from_your_email))
                return@setOnClickListener
            }
            if (et_pass.text.toString().isNullOrBlank()) {
                showAlertDialog(this, getString(R.string.please_enter_your_new_password))
                return@setOnClickListener
            }

            val postResetPass = PostResetPass()
            postResetPass.token = et_code.text.toString()
            postResetPass.email = email
            postResetPass.password = et_pass.text.toString()

            WebService.resetPass(postResetPass, object : DataListener<ResponseSetFilter> {
                override fun onStartCall() {
                    showProgressBar(this@ForgetPassConfirmActivity)
                }

                override fun onResponse(responseObj: ResponseSetFilter?) {
                    hideProgressBar()

                    responseObj?.let {
                        if (it.meta.status == 200) {
                            Intent(applicationContext, LoginActivity::class.java).apply {
                                flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                                startActivity(this)
                            }
                        } else {
                            showAlertDialog(this@ForgetPassConfirmActivity, it.response.message)
                        }
                    }
                }

                override fun onError(errorMessage: String) {
                    hideProgressBar()
                }
            })

        }
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        hideKeyboard(this)
        return super.dispatchTouchEvent(ev)
    }
}
