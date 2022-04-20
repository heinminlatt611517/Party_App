package com.parallax.partyapp.views.AppFlow

import android.os.Bundle
import androidx.lifecycle.MutableLiveData
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.parallax.partyapp.Data.SPreferences
import com.parallax.partyapp.Models.ResponsePosts
import com.parallax.partyapp.R
import com.parallax.partyapp.views.BaseActivity
import com.parallax.partyapp.views.Fragments.FilterFragment
import com.parallax.partyapp.views.Fragments.HomeFragment
import com.parallax.partyapp.views.Fragments.LoyalityFragment
import com.parallax.partyapp.views.Fragments.SettingsFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity() {

    val homeFragment = HomeFragment()
    val loyaltyFraggment = LoyalityFragment()
    val settingsFragment = SettingsFragment()
    val filterFragment = FilterFragment()


    val responsePosts  = MutableLiveData<ResponsePosts>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bottomNavigationView.setOnNavigationItemSelectedListener(navigation)

        if (savedInstanceState?.getInt("selectedTab", -1) == R.id.setting_menu) {
            supportFragmentManager.beginTransaction().replace(R.id.frame, SettingsFragment())
                .commit()
        } else {
            supportFragmentManager.beginTransaction().replace(R.id.frame, HomeFragment()).commit()
        }

    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt("selectedTab", R.id.setting_menu)
    }

    override fun onBackPressed() {
        if (SPreferences.getUserToken() != null) {
            finishAffinity()
        }
    }

    fun switchTab(menuId: Int) {
        bottomNavigationView.selectedItemId = menuId
    }

    private val navigation =
        BottomNavigationView.OnNavigationItemSelectedListener { menuItem ->
            var seletctedFragment: androidx.fragment.app.Fragment? = null

            when (menuItem.itemId) {
                R.id.home_menu -> seletctedFragment = homeFragment
                R.id.setting_menu -> seletctedFragment = loyaltyFraggment
                R.id.follows_menu -> seletctedFragment = settingsFragment
                R.id.filter_menu -> seletctedFragment = filterFragment
            }

            supportFragmentManager.beginTransaction().replace(R.id.frame, seletctedFragment!!)
                .commit()

            true
        }
}
