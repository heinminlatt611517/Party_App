package com.parallax.partyapp.views.RegistrationFlow

import android.content.Intent
import android.os.Bundle
import androidx.viewpager.widget.ViewPager
import com.parallax.partyapp.R
import com.parallax.partyapp.views.Adapter.PagerAdapter
import com.parallax.partyapp.views.BaseActivity
import kotlinx.android.synthetic.main.activity_intro.*

class IntroActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(android.R.style.Theme_Material_Light_NoActionBar_Fullscreen)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intro)

        val adaper = PagerAdapter(supportFragmentManager, 4)
        view_pager.adapter = adaper

        tab_layout.setupWithViewPager(view_pager, true)

        view_pager.addOnPageChangeListener(object : androidx.viewpager.widget.ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(p0: Int) {

            }

            override fun onPageScrolled(p0: Int, p1: Float, p2: Int) {

            }

            override fun onPageSelected(p0: Int) {
                when (p0) {
                    0 -> btn_continue.text = "Next"
                    1 -> btn_continue.text = "Next"
                    2 -> btn_continue.text = "Next"
                    3 -> btn_continue.text = "Continue"
                }
            }
        })


        btn_continue.setOnClickListener {
            when (view_pager.currentItem) {
                0 -> view_pager.currentItem = 1
                1 -> view_pager.currentItem = 2
                2 -> view_pager.currentItem = 3
                3 -> startActivity(Intent(applicationContext, WelcomeActivity::class.java))
            }
        }



        tv_skip.setOnClickListener {
            startActivity(Intent(applicationContext, WelcomeActivity::class.java))
        }
    }
}
