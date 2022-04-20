package com.parallax.partyapp.views.RegistrationFlow

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginResult
import com.google.gson.Gson
import com.parallax.partyapp.Api.BodyFbLogin
import com.parallax.partyapp.Api.ResponseFbLogin
import com.parallax.partyapp.Api.WebService
import com.parallax.partyapp.Data.SPreferences
import com.parallax.partyapp.Models.ResponseGetProfile
import com.parallax.partyapp.R
import com.parallax.partyapp.interfaces.DataListener
import com.parallax.partyapp.views.AppFlow.MainActivity
import com.parallax.partyapp.views.BaseActivity
import hideProgressBar
import kotlinx.android.synthetic.main.activity_welcome.*
import pub.devrel.easypermissions.AfterPermissionGranted
import pub.devrel.easypermissions.EasyPermissions
import showProgressBar
import java.util.*

class WelcomeActivity : BaseActivity() {

    val EMAIL = "email"
    val PHOTOS = "public_profile"

    private var callbackManager: CallbackManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)

        if (SPreferences.getUserToken() != null) {

            startActivity(Intent(applicationContext, MainActivity::class.java))
            finish()

        }

        askPermission()

        layout_fb.setOnClickListener {
            login_button.performClick()
        }

        button2.setOnClickListener {
            startActivity(Intent(applicationContext, LoginActivity::class.java))
        }

        fbLoginCallback()
    }

    fun fbLoginCallback() {
        callbackManager = CallbackManager.Factory.create()

        login_button.setPermissions(listOf(EMAIL, PHOTOS))
        // If you are using in a fragment, call loginButton.setFragment(this);

        // Callback registration
        login_button.registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
            override fun onSuccess(loginResult: LoginResult) {
                Log.d("facebookLogin", "onSuccess")
                Log.d("facebookLogin", loginResult.accessToken.token)
                Log.d("facebookLogin", loginResult.accessToken.userId)
                // App code

                WebService.fbLogin(
                    BodyFbLogin(fb_token = loginResult.accessToken.token),
                    object : DataListener<ResponseFbLogin> {
                        override fun onStartCall() {
                            showProgressBar(this@WelcomeActivity)
                        }

                        override fun onResponse(responseObj: ResponseFbLogin?) {
                            hideProgressBar()
                            SPreferences.setUserToken("Bearer ${responseObj?.access_token}")
//                            SPreferences.setUserFacebookId(loginResult.accessToken.userId)
//                            startActivity(Intent(applicationContext, MainActivity::class.java))
                            getProfileApi()
                        }

                        override fun onError(errorMassage: String) {
                            hideProgressBar()
                        }
                    })


//                 App code
//                                val request = GraphRequest.newMeRequest(
//                                    loginResult.accessToken
//                                ) { jsonObject, response ->
//                                    Log.v("LoginActivity", response.toString())
//
//                                     Application code
//                                    val email = jsonObject.getString("email")
//                                    val photos = jsonObject.getString("photos")
//                                    val birthday = `object`.getString("birthday") // 01/31/1980 format
//
//                                    Log.d("facebookLogin", email + "")
//                                }
//                                val parameters = Bundle()
//                                parameters.putString("fields", "id,name,email,photos")
//                                request.parameters = parameters
//                                request.executeAsync()
            }

            override fun onCancel() {
                Log.d("facebookLogin", "cancelled")
                // App code
            }

            override fun onError(exception: FacebookException) {
                Log.d("facebookLogin", "onError")
                // App code
            }
        })
    }


    private fun getProfileApi() {
        WebService.callGetProfile(object : DataListener<ResponseGetProfile> {
            override fun onStartCall() {}

            override fun onResponse(responseObj: ResponseGetProfile?) {

                if (responseObj?.meta?.status == 200) {
                    Log.d("getProfileResponse", "" + responseObj)

                    SPreferences.setUserObj(Gson().toJson(responseObj))

                    Log.d("RESPONSE_STRING", "" + SPreferences.getUserObj())

                    startActivity(Intent(applicationContext, MainActivity::class.java))
//                    Toast.makeText(applicationContext, "" + responseObj.response.message, Toast.LENGTH_SHORT).show()

                } else {

                    Log.d("getProfileResponse", "" + responseObj)
                }
            }

            override fun onError(errorMessage: String) {
                Log.d("onGetProfileErrror", "" + errorMessage)
            }

        })
    }

    @AfterPermissionGranted(1234)
    private fun askPermission() {
        val perms = arrayOf(
            Manifest.permission.CAMERA,
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
        if (EasyPermissions.hasPermissions(this, *perms)) {
            // Already have permission, do the thing


        } else {
            // Do not have permissions, request them now
            EasyPermissions.requestPermissions(
                this@WelcomeActivity,
                getString(R.string.please_give_all_permission),
                1234,
                *perms
            )
        }
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        // Forward results to EasyPermissions
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        callbackManager?.onActivityResult(requestCode, resultCode, data)
        super.onActivityResult(requestCode, resultCode, data)
    }
}
