package com.parallax.partyapp.views.AppFlow.optional

import android.os.Bundle
import com.parallax.partyapp.R
import com.parallax.partyapp.views.BaseActivity
import com.parallax.partyapp.views.Fragments.FollowrsFragment

class FollowersActivity : BaseActivity() {

    companion object {
        val KEY_USER_ID = "userId"
        val KEY_CHECKED_TAB = "checkedTab"
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_followers)

        FollowrsFragment().let {

            val b = Bundle()
            b.putString(FollowrsFragment.KEY_CHECKED_TAB, intent?.getStringExtra(KEY_CHECKED_TAB))
            b.putString(FollowrsFragment.KEY_USER_ID, intent?.getStringExtra(KEY_USER_ID))

            it.arguments = b

            supportFragmentManager.beginTransaction()
                .add(R.id.container, it)
                .commit()
        }


    }
}
