package com.parallax.partyapp.views.AppFlow

import android.os.Bundle
import com.parallax.partyapp.R
import com.parallax.partyapp.views.BaseActivity
import com.parallax.partyapp.views.Fragments.ProfileFragment

class ProfileActivity : BaseActivity() {

    companion object {
        val KEY_USER_ID = "userId"
    }

    var userId = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        userId = intent?.getIntExtra(KEY_USER_ID, -1)!!


        val bundle = Bundle()
        bundle.putInt(ProfileFragment.KEY_USER_ID, userId)

        val profileFragment = ProfileFragment()
        profileFragment.arguments = bundle


        supportFragmentManager.beginTransaction().replace(R.id.container, profileFragment).commit()
    }
}
