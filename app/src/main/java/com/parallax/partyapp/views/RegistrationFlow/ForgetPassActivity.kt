package com.parallax.partyapp.views.RegistrationFlow

import android.content.Intent
import android.os.Bundle
import android.view.MotionEvent
import com.parallax.partyapp.Api.WebService
import com.parallax.partyapp.Models.PostForgotPass
import com.parallax.partyapp.Models.ResponseSetFilter
import com.parallax.partyapp.R
import com.parallax.partyapp.interfaces.DataListener
import com.parallax.partyapp.views.BaseActivity
import hideKeyboard
import hideProgressBar
import kotlinx.android.synthetic.main.activity_forget_pass.*
import showAlertDialog
import showProgressBar

class ForgetPassActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forget_pass)


        btn_forget_password.setOnClickListener {

            if (et_email.text.toString().isNullOrEmpty()) {
                showAlertDialog(this, getString(R.string.please_enter_your_email))

                return@setOnClickListener
            }

            val postForgotPass = PostForgotPass()
            postForgotPass.email = et_email.text.toString()

            WebService.forgotPassword(postForgotPass, object : DataListener<ResponseSetFilter> {
                override fun onStartCall() {
                    showProgressBar(this@ForgetPassActivity)
                }

                override fun onResponse(responseObj: ResponseSetFilter?) {
                    hideProgressBar()

                    responseObj?.let {
                        if (it.meta.status == 200) {
                            Intent(
                                applicationContext,
                                ForgetPassConfirmActivity::class.java
                            ).apply {
                                putExtra(
                                    ForgetPassConfirmActivity.KEY_EMAIL,
                                    et_email.text.toString()
                                )
                                startActivity(this)
                            }
                        } else {
                            showAlertDialog(this@ForgetPassActivity, it.response.message)
                        }
                    }
                }

                override fun onError(errorMessage: String) {
                    hideProgressBar()
                }
            })


        }

        btnBack.setOnClickListener {
            onBackPressed()
        }


    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        hideKeyboard(this)
        return super.dispatchTouchEvent(ev)
    }
}
