/*
 * Developed by Md Rayhan Uddin (rayhanuddin55@gmail.com)
 */

package com.parallax.partyapp.views.Adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.parallax.partyapp.views.RegistrationFlow.IntroFragment.IntroFourFragment
import com.parallax.partyapp.views.RegistrationFlow.IntroFragment.IntroOneFragment
import com.parallax.partyapp.views.RegistrationFlow.IntroFragment.IntroThreeFragment
import com.parallax.partyapp.views.RegistrationFlow.IntroFragment.IntroTwoFragment


class PagerAdapter(fm: androidx.fragment.app.FragmentManager, var mNoOfTabs: Int) : androidx.fragment.app.FragmentPagerAdapter(fm) {
    override fun getItem(position: Int): androidx.fragment.app.Fragment {
        return when (position) {
            0 -> {
                IntroOneFragment()
            }
            1 -> {
                IntroTwoFragment()
            }
            2 -> {
                IntroThreeFragment()
            }
            3 -> {
                IntroFourFragment()
            }
            else -> androidx.fragment.app.Fragment()
        }

    }

    override fun getCount(): Int {
        return mNoOfTabs
    }
}