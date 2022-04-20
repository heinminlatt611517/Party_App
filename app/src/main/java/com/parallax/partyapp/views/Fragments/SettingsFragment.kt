package com.parallax.partyapp.views.Fragments


import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.facebook.login.LoginManager
import com.google.gson.Gson
import com.parallax.partyapp.Api.WebService
import com.parallax.partyapp.BuildConfig
import com.parallax.partyapp.Data.SPreferences
import com.parallax.partyapp.Models.ResponseGetProfile
import com.parallax.partyapp.Models.ResponseLogOut
import com.parallax.partyapp.R
import com.parallax.partyapp.Utils.ToastUtils
import com.parallax.partyapp.interfaces.DataListener
import com.parallax.partyapp.views.AppFlow.MainActivity
import com.parallax.partyapp.views.AppFlow.ProfileActivity
import com.parallax.partyapp.views.AppFlow.optional.FollowersActivity
import com.parallax.partyapp.views.AppFlow.optional.PrivacyPolicyActivity
import com.parallax.partyapp.views.RegistrationFlow.WelcomeActivity
import com.vincent.filepicker.ToastUtil
import hideProgressBar
import kotlinx.android.synthetic.main.fragment_settings.view.*
import showProgressBar
import java.util.*

class SettingsFragment : androidx.fragment.app.Fragment() {

    private lateinit var viewGroup: ViewGroup
    private var userObj: ResponseGetProfile? = null

    private var responseObj: ResponseGetProfile? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        viewGroup =
            inflater.inflate(R.layout.fragment_settings, container, false) as ViewGroup

        viewGroup.tv_version.text = "Version ${BuildConfig.VERSION_NAME}"

        viewGroup.switch_language.isChecked = !SPreferences.islanguageBurmese()


        initOnClickListener(viewGroup)

        getProfileApi()

        return viewGroup
    }

    private fun initOnClickListener(viewGroup: ViewGroup) {

        viewGroup.switch_language.setOnCheckedChangeListener { buttonView, isChecked ->
            when (isChecked) {
                true -> {
                    (activity as MainActivity).setLanguage(Locale.ENGLISH)
                    SPreferences.setIslanguageBurmese(false)
                }
                false -> {
                    (activity as MainActivity).setLanguage("my")
                    SPreferences.setIslanguageBurmese(true)
                }
            }
        }


        viewGroup.layoutPrivacyPolicy.setOnClickListener {
            startActivity(Intent(context, PrivacyPolicyActivity::class.java))
        }

        viewGroup.layout_profile.setOnClickListener {
            if (userObj == null) return@setOnClickListener
            Intent(context, ProfileActivity::class.java).let {
                it.putExtra(ProfileActivity.KEY_USER_ID, userObj?.response?.user_data?.id)
                startActivity(it)
            }
        }

        viewGroup.layout_log_out.setOnClickListener {
            doLogout()
        }

        viewGroup.layout_follower.setOnClickListener {

            Intent(context, FollowersActivity::class.java).let {
                it.putExtra(FollowersActivity.KEY_CHECKED_TAB, FollowrsFragment.VALUE_FOLLOWER_TAB)
                it.putExtra(
                    FollowersActivity.KEY_USER_ID,
                    this.responseObj?.response?.user_data?.id.toString()
                )
                startActivity(it)
            }

        }
    }

    private fun getProfileApi() {
        WebService.callGetProfile(object : DataListener<ResponseGetProfile> {
            override fun onStartCall() {}

            override fun onResponse(responseObj: ResponseGetProfile?) {

                userObj = responseObj

                if (responseObj?.meta?.status == 200) {
                    this@SettingsFragment.responseObj = responseObj
                    Log.d("getProfileResponse", "" + responseObj)

                    viewGroup.tv_name.text = responseObj?.response?.user_data?.name
                    viewGroup.tv_mail_address.text = responseObj?.response?.user_data?.email

                    SPreferences.setUserObj(Gson().toJson(responseObj))

//                    Toast.makeText(context,""+responseObj.response.message,Toast.LENGTH_SHORT).show()

                } else {

                    Log.d("getProfileResponse", "" + responseObj)
//                    Toast.makeText(context,""+responseObj?.response?.message,Toast.LENGTH_SHORT).show()
                }
            }

            override fun onError(errorMessage: String) {
                Log.d("onGetProfileErrror", "" + errorMessage)
//                Toast.makeText(context,""+errorMessage,Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun doLogout() {
        WebService.callLogoutAPI(object : DataListener<ResponseLogOut> {
            override fun onStartCall() {
                showProgressBar(activity as Activity)
            }

            override fun onResponse(responseObj: ResponseLogOut?) {
                hideProgressBar()

                if (responseObj?.meta?.status == 200) {
                    Log.d("onLogoutResponse", "" + responseObj)
                    LoginManager.getInstance().logOut()
                    SPreferences.setUserToken(null)
                    SPreferences.setUserFacebookId(null)
                    var intent = Intent(context, WelcomeActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    startActivity(intent)
                    activity?.finish()

                    ToastUtils.getInstance(context!!)?.showToast(responseObj.response.message)

                } else {

                    Log.d("onLogoutResponse1", "" + responseObj)

                    ToastUtils.getInstance(context!!)?.showToast(responseObj?.response?.message)

                }
            }

            override fun onError(errorMessage: String) {
                hideProgressBar()
                Log.d("onLogoutErrror", "" + errorMessage)
                ToastUtils.getInstance(context!!)?.showToast(errorMessage)
            }

        })
    }


}
