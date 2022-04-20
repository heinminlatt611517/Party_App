package com.parallax.partyapp.views.AppFlow

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.gson.Gson
import com.parallax.partyapp.Models.ResponsePosts
import com.parallax.partyapp.R
import kotlinx.android.synthetic.main.activity_reward_details.*
import kotlinx.android.synthetic.main.activity_reward_details.btn_back
import kotlinx.android.synthetic.main.activity_reward_details.iv_image
import kotlinx.android.synthetic.main.activity_reward_details.tv_des
import kotlinx.android.synthetic.main.activity_reward_details.tv_location
import kotlinx.android.synthetic.main.activity_reward_details.tv_phone
import kotlinx.android.synthetic.main.activity_reward_details.tv_title
import kotlinx.android.synthetic.main.activity_reward_details.tv_username
import loadImage

class RewardDetailsActivity : AppCompatActivity() {

    companion object {
        val KEY_REWARD_DETAILS = "reward_details"
    }

    lateinit var rewardDetails: ResponsePosts.Response.PromoData

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reward_details)

        rewardDetails = Gson().fromJson(
            intent?.getStringExtra(KEY_REWARD_DETAILS),
            ResponsePosts.Response.PromoData::class.java
        )

        loadImage(applicationContext, rewardDetails.image, iv_image)

        tv_title.text = rewardDetails.promo_name
        tv_des.text = rewardDetails.promo_description

        tv_location.text = rewardDetails.promo_address_text_eng
        tv_username.text = rewardDetails.social_username ?: "--"
        tv_phone.text = rewardDetails.phone ?: "--"

        iv_location.setOnClickListener {
            if (rewardDetails.latitude != null) {
                Intent(applicationContext, LocationActivity::class.java).let { intent ->
                    rewardDetails.latitude.let {
                        intent.putExtra(
                            LocationActivity.KEY_LAT,
                            rewardDetails.latitude?.toDouble()
                        )
                        intent.putExtra(
                            LocationActivity.KEY_LNG,
                            rewardDetails.longitude?.toDouble()
                        )
//                        intent.putExtra(LocationActivity.KEY_DRAW_POLY_OFF, true)
                    }
                    startActivity(intent)
                }
            }
        }

        iv_messanger.setOnClickListener {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(rewardDetails.social_username)))
        }

        iv_phone.setOnClickListener {
            val intent = Intent(Intent.ACTION_DIAL)
            intent.data = Uri.parse("tel:${rewardDetails.phone}")
            startActivity(intent)
        }

        btn_back.setOnClickListener { onBackPressed() }
    }
}