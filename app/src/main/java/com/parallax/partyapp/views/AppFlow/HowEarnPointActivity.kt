package com.parallax.partyapp.views.AppFlow

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.parallax.partyapp.Models.ResponsePosts
import com.parallax.partyapp.R
import com.parallax.partyapp.Utils.*
import com.parallax.partyapp.views.Adapter.HowEarnPointsAdapter
import kotlinx.android.synthetic.main.activity_how_earn_point.*
import kotlinx.android.synthetic.main.activity_how_earn_point.iv_current_status_logo
import kotlinx.android.synthetic.main.activity_how_earn_point.iv_indicator_1
import kotlinx.android.synthetic.main.activity_how_earn_point.iv_indicator_2
import kotlinx.android.synthetic.main.activity_how_earn_point.iv_indicator_3
import kotlinx.android.synthetic.main.activity_how_earn_point.iv_indicator_4
import kotlinx.android.synthetic.main.activity_how_earn_point.tv_current_point
import kotlinx.android.synthetic.main.activity_how_earn_point.tv_current_status
import kotlinx.android.synthetic.main.activity_how_earn_point.tv_next_tier_text

class HowEarnPointActivity : AppCompatActivity() {
    companion object {
        var responsePost: ResponsePosts.Response? = null
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_how_earn_point)


        tv_current_status.text = responsePost?.customer_type
        tv_current_point.text = responsePost?.current_om_points.toString()
        tv_next_tier_text.text =
            "Unlock ${responsePost?.next_tier} only ${responsePost?.points_to_next_tier} pts"

        when (responsePost?.customer_type) {
            TIER_SOCIALIZER -> {
                iv_current_status_logo.setImageResource(R.drawable.ic_star_socializer)

                iv_indicator_1.show()
                iv_indicator_2.invisible()
                iv_indicator_3.invisible()
                iv_indicator_4.invisible()
            }
            TIER_PARTYER -> {
                iv_current_status_logo.setImageResource(R.drawable.ic_star_partyer)

                iv_indicator_1.invisible()
                iv_indicator_2.show()
                iv_indicator_3.invisible()
                iv_indicator_4.invisible()
            }
            TIER_RAGER -> {
                iv_current_status_logo.setImageResource(R.drawable.ic_star_rager)

                iv_indicator_1.invisible()
                iv_indicator_2.invisible()
                iv_indicator_3.show()
                iv_indicator_4.invisible()
            }
            TIER_VIP -> {
                iv_current_status_logo.setImageResource(R.drawable.ic_star_vip)

                iv_indicator_1.invisible()
                iv_indicator_2.invisible()
                iv_indicator_3.invisible()
                iv_indicator_4.show()
            }
        }

        rv.layoutManager = LinearLayoutManager(applicationContext, RecyclerView.VERTICAL, false)
        rv.adapter = HowEarnPointsAdapter(applicationContext, responsePost?.tier_details!!)

        btn_back2.setOnClickListener { onBackPressed() }
    }
}