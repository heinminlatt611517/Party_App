package com.parallax.partyapp.views.AppFlow

import android.os.Bundle
import com.parallax.partyapp.Api.WebService
import com.parallax.partyapp.Models.ResponseGetProfile
import com.parallax.partyapp.R
import com.parallax.partyapp.interfaces.DataListener
import com.parallax.partyapp.views.BaseActivity
import hideProgressBar
import kotlinx.android.synthetic.main.activity_user_info.*
import loadImage
import showProgressBar

class UserInfoActivity : BaseActivity() {

    companion object {
        val KEY_USER_ID = "userId"
    }

    var userId = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_info)

        userId = intent?.getIntExtra(KEY_USER_ID, -1)!!


        getUserProfile()


        ic_back.setOnClickListener { onBackPressed() }
    }

    fun getUserProfile() {
        WebService.getUserProfile(userId, object : DataListener<ResponseGetProfile> {
            override fun onStartCall() {
                showProgressBar(this@UserInfoActivity)
            }

            override fun onResponse(responseObj: ResponseGetProfile?) {
                hideProgressBar()

                responseObj?.let {
                    if (it.meta.status == 200) {
                        tv_user_name.text = it.response.user_data.name
                        tv_user_dob.text = it.response.user_data.date_of_birth
                        tv_user_mail.text = it.response.user_data.email
                        tv_user_bio.text = it.response.user_data.bio

                        tv_post_count.text = it.response.user_data.posts_count.toString()
                        tv_follower_count.text = it.response.user_data.follow_count.toString()
                        tv_following_count.text = it.response.user_data.following_count.toString()

                        loadImage(
                            applicationContext,
                            it.response.user_data.image_url,
                            iv_profile_img
                        )
                    }
                }
            }

            override fun onError(errorMessage: String) {
                hideProgressBar()
            }


        })
    }
}
