package com.parallax.partyapp.views.RegistrationFlow

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.MotionEvent
import com.parallax.partyapp.R
import com.parallax.partyapp.views.BaseActivity
import hideKeyboard
import kotlinx.android.synthetic.main.activity_otp_confirmation.*

class OtpConfirmationActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_otp_confirmation)

        btnBack.setOnClickListener {
            onBackPressed()
        }

        btnOk.setOnClickListener {
            startActivity(Intent(applicationContext, CongratulationsActivity::class.java))
        }

        setEditTextWatcher()

    }

    private fun setEditTextWatcher() {
        editText1.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {

            }

            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {

            }

            override fun afterTextChanged(editable: Editable) {
                if (editText1.text.length == 1) {
                    editText2.requestFocus()
                }
            }
        })

        editText2.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {

            }

            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {

            }

            override fun afterTextChanged(editable: Editable) {
                if (editText2.text.length == 1) {
                    editText3.requestFocus()
                } else if (editText4.text.isEmpty()) {
                    editText1.requestFocus()
                }
            }
        })

        editText3.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {

            }

            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {

            }

            override fun afterTextChanged(editable: Editable) {
                if (editText3.text.length == 1) {
                    editText4.requestFocus()
                } else if (editText4.text.isEmpty()) {
                    editText2.requestFocus()
                }
            }
        })

        editText4.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {

            }

            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {

            }

            override fun afterTextChanged(editable: Editable) {
                if (editText4.text.length == 1) {
                    hideKeyboard(this@OtpConfirmationActivity)
                } else if (editText4.text.isEmpty()) {
                    editText3.requestFocus()
                }
            }
        })
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        hideKeyboard(this)
        return super.dispatchTouchEvent(ev)
    }
}
