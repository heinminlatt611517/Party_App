package com.parallax.partyapp.views.AppFlow

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.parallax.partyapp.R
import com.parallax.partyapp.Utils.ITEMS_ACTIVITY
import com.parallax.partyapp.Utils.ToastUtils
import com.parallax.partyapp.views.Adapter.FeelingAdapter
import com.parallax.partyapp.views.BaseActivity
import com.vincent.filepicker.ToastUtil
import kotlinx.android.synthetic.main.activity_what_are_yo_doing.*
import java.util.*

class WhatAreYoDoingActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_what_are_yo_doing)


        val imageList = ArrayList<Int>()
        imageList.add(R.drawable.ic_drinking)
        imageList.add(R.drawable.ic_partying)
        imageList.add(R.drawable.ic_dancing)
        imageList.add(R.drawable.ic_listening_music)
        imageList.add(R.drawable.ic_celebrating)
        imageList.add(R.drawable.ic_singing)


        val feelingAdapter = FeelingAdapter(this, ITEMS_ACTIVITY, imageList)
        recyclerView.layoutManager =
            androidx.recyclerview.widget.LinearLayoutManager(
                applicationContext,
                androidx.recyclerview.widget.LinearLayoutManager.VERTICAL,
                false
            )
        recyclerView.adapter = feelingAdapter


        btnNext.setOnClickListener {
            if (MediaActivity.postActivity == null) {
                ToastUtils.getInstance(applicationContext)
                    ?.showToast(getString(R.string.please_enter_your_activity))
                return@setOnClickListener
            }
            onBackPressed()
        }


        btnBack.setOnClickListener {
            MediaActivity.postActivity = null
            onBackPressed()
        }

    }
}
