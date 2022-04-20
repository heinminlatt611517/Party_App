package com.parallax.partyapp.views.AppFlow.optional

import android.os.Bundle
import android.view.MotionEvent
import com.parallax.partyapp.R
import com.parallax.partyapp.views.BaseActivity
import hideKeyboard
import kotlinx.android.synthetic.main.activity_send_feedback.*

class SendFeedbackActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_send_feedback)

        btnBack.setOnClickListener {
            onBackPressed()
        }
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        hideKeyboard(this)
        return super.dispatchTouchEvent(ev)
    }
}
