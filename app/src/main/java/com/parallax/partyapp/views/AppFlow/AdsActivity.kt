package com.parallax.partyapp.views.AppFlow

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.parallax.partyapp.Models.ResponsePosts
import com.parallax.partyapp.R
import kotlinx.android.synthetic.main.activity_ads.*
import kotlinx.android.synthetic.main.activity_reward_details.btn_back
import kotlinx.android.synthetic.main.activity_reward_details.iv_image
import kotlinx.android.synthetic.main.activity_reward_details.tv_des
import kotlinx.android.synthetic.main.activity_reward_details.tv_location
import kotlinx.android.synthetic.main.activity_reward_details.tv_phone
import kotlinx.android.synthetic.main.activity_reward_details.tv_title
import kotlinx.android.synthetic.main.activity_reward_details.tv_username
import loadImage

class AdsActivity : AppCompatActivity() {

    companion object{
        val KEY_REWARD_DETAILS = "reward_details"
    }

    lateinit var adDetails : ResponsePosts.Response.AddLists

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ads)

        adDetails = Gson().fromJson(intent?.getStringExtra(KEY_REWARD_DETAILS), ResponsePosts.Response.AddLists::class.java)

        loadImage(applicationContext, adDetails.ads_banner, iv_image)

        tv_title.text = adDetails.title
        tv_des.text = adDetails.description

        tv_location.text = adDetails.location
        tv_username.text = adDetails.social_username ?: "--"
        tv_phone.text = adDetails.phone ?: "--"

        iv_location.setOnClickListener {
            if(adDetails.latitude != null) {
                Intent(applicationContext, LocationActivity::class.java).let { intent ->
                    adDetails.latitude.let {
                        intent.putExtra(LocationActivity.KEY_LAT, adDetails.latitude.toDouble())
                        intent.putExtra(LocationActivity.KEY_LNG, adDetails.longitude.toDouble())
//                    intent.putExtra(LocationActivity.KEY_DRAW_POLY_OFF, true)
                    }
                    startActivity(intent)
                }
            }
        }

        iv_messanger.setOnClickListener {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(adDetails.social_username)))
        }

        iv_phone.setOnClickListener {
            val intent = Intent(Intent.ACTION_DIAL)
            intent.data = Uri.parse("tel:${adDetails.phone}")
            startActivity(intent)
        }

        btn_back.setOnClickListener { onBackPressed() }
    }
}