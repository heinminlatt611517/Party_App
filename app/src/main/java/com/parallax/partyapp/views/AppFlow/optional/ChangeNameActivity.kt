package com.parallax.partyapp.views.AppFlow.optional

import android.os.Bundle
import android.view.MotionEvent
import com.parallax.partyapp.R
import com.parallax.partyapp.views.BaseActivity
import hideKeyboard
import kotlinx.android.synthetic.main.activity_change_name.*

class ChangeNameActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_name)

        btnBack.setOnClickListener {
            onBackPressed()
        }
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        hideKeyboard(this)
        return super.dispatchTouchEvent(ev)
    }
}
