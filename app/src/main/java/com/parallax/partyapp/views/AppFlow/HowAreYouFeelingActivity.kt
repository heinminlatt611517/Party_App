package com.parallax.partyapp.views.AppFlow

import android.content.Intent
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.parallax.partyapp.R
import com.parallax.partyapp.Utils.ITEMS_FEELING
import com.parallax.partyapp.Utils.ToastUtils
import com.parallax.partyapp.views.Adapter.Feeling2Adapter
import com.parallax.partyapp.views.BaseActivity
import com.vincent.filepicker.ToastUtil
import kotlinx.android.synthetic.main.activity_how_are_you_feeling.*
import java.util.*

class HowAreYouFeelingActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_how_are_you_feeling)


        var imageList = ArrayList<Int>()
        imageList.add(R.drawable.ic_drunk)
        imageList.add(R.drawable.ic_tipsy)
        imageList.add(R.drawable.ic_happy)
        imageList.add(R.drawable.ic_excited)
        imageList.add(R.drawable.ic_silly)
        imageList.add(R.drawable.ic_sexy)
        imageList.add(R.drawable.ic_awesome)
        imageList.add(R.drawable.ic_sober)



        val feeling2Adapter = Feeling2Adapter(this, ITEMS_FEELING, imageList)
        recyclerView.layoutManager =
            androidx.recyclerview.widget.LinearLayoutManager(
                applicationContext,
                androidx.recyclerview.widget.LinearLayoutManager.VERTICAL,
                false
            )
        recyclerView.adapter = feeling2Adapter



        btnBack.setOnClickListener {
            MediaActivity.postFeeligs = null
            onBackPressed()
        }

        btnNext.setOnClickListener {
            if (MediaActivity.postFeeligs == null) {
                ToastUtils.getInstance(applicationContext)
                    ?.showToast(getString(R.string.please_enter_your_feelings))
                return@setOnClickListener
            }
            startActivity(Intent(applicationContext, WhatAreYoDoingActivity::class.java))
            finish()
        }

    }
}
