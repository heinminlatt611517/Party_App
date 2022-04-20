package com.parallax.partyapp.views.RegistrationFlow

import android.Manifest
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.view.MotionEvent
import android.widget.Toast
import com.google.gson.Gson
import com.parallax.partyapp.Api.WebService
import com.parallax.partyapp.Data.SPreferences
import com.parallax.partyapp.Models.LoginBodyModel
import com.parallax.partyapp.Models.ResponseGetProfile
import com.parallax.partyapp.R
import com.parallax.partyapp.Utils.ToastUtils
import com.parallax.partyapp.interfaces.DataListener
import com.parallax.partyapp.views.AppFlow.MainActivity
import com.parallax.partyapp.views.BaseActivity
import com.vincent.filepicker.Constant
import com.vincent.filepicker.ToastUtil
import com.vincent.filepicker.filter.entity.ImageFile
import com.vincent.filepicker.filter.entity.VideoFile
import hideKeyboard
import hideProgressBar
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.dialog_location.*
import pub.devrel.easypermissions.AfterPermissionGranted
import pub.devrel.easypermissions.EasyPermissions
import showProgressBar


class LoginActivity : BaseActivity() {

    lateinit var loginBodyModel: LoginBodyModel

    val TAG_SHOW = "show"
    val TAG_HIDE = "hide"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.parallax.partyapp.R.layout.activity_login)


        if (SPreferences.getUserToken() != null) {
            startActivity(Intent(applicationContext, MainActivity::class.java))
            finish()
        }

//        val intent1 = Intent(this, ImagePickActivity::class.java)
//        intent1.putExtra(IS_NEED_CAMERA, true)
//        intent1.putExtra(Constant.MAX_NUMBER, 9)
//        startActivityForResult(intent1, Constant.REQUEST_CODE_PICK_IMAGE)
//

        askPermission()

        et_login_password.tag = TAG_HIDE

        iv_eye_pass.setOnClickListener { onEyeClick(true) }




        loginBodyModel = LoginBodyModel()
        initClickListeners()
    }


    override fun onResume() {
        super.onResume()

//        dialogLocation()
    }

    private fun onEyeClick(isConfirmPass: Boolean) {

        if (isConfirmPass) {

            if (et_login_password.tag == TAG_HIDE) {
                et_login_password.tag = TAG_SHOW
                iv_eye_pass.setImageDrawable(resources.getDrawable(R.drawable.ic_invisible))
                et_login_password.transformationMethod =
                    HideReturnsTransformationMethod.getInstance()
            } else {
                et_login_password.tag = TAG_HIDE
                iv_eye_pass.setImageDrawable(resources.getDrawable(R.drawable.ic_visible))
                et_login_password.transformationMethod = PasswordTransformationMethod.getInstance()
            }

            et_login_password.setSelection(et_login_password.text.toString().length)
        }

    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            Constant.REQUEST_CODE_PICK_IMAGE ->
                if (resultCode == RESULT_OK) {
                    var list: ArrayList<ImageFile> =
                        data!!.getParcelableArrayListExtra(Constant.RESULT_PICK_IMAGE)

                    for (item in list) {
                        Log.d("imagePath", item.path)
                    }
                }

            Constant.REQUEST_CODE_PICK_VIDEO ->
                if (resultCode == RESULT_OK) {
                    var list: ArrayList<VideoFile> =
                        data!!.getParcelableArrayListExtra(Constant.RESULT_PICK_VIDEO)

                    for (item in list) {
                        Log.d("videoPath", item.path)
                    }
                }

        }
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        hideKeyboard(this)
        return super.dispatchTouchEvent(ev)
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

    private fun initClickListeners() {
        tv_register.setOnClickListener {
            startActivity(Intent(applicationContext, RegistrationActivity::class.java))
        }

        tv_forget_password.setOnClickListener {
            startActivity(Intent(applicationContext, ForgetPassActivity::class.java))
        }

        btn_login.setOnClickListener {
            //            startActivity(Intent(applicationContext, MainActivity::class.java))


            when {
                et_login_email.text.toString().isEmpty() ->
                    ToastUtils.getInstance(applicationContext)?.showToast(getString(R.string.please_provide_valid_username))
                et_login_password.text.toString().isEmpty() ->
                    ToastUtils.getInstance(applicationContext)?.showToast(getString(R.string.please_provide_a_vaid_password))
                et_login_email.text.toString().isEmpty() || et_login_password.text.toString().isEmpty() ->
                    ToastUtils.getInstance(applicationContext)?.showToast(getString(R.string.please_provide_valid_username))
                else -> {
                    loginBodyModel.email = et_login_email.text.toString()
                    loginBodyModel.password = et_login_password.text.toString()

                    doLogin(loginBodyModel)
                }
            }
        }
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

            initClickListeners()


        } else {
            // Do not have permissions, request them now
            EasyPermissions.requestPermissions(this@LoginActivity, "", 1234, *perms)
        }
    }


    private fun dialogLocation() {

        var dialog = Dialog(this)
        dialog.setContentView(com.parallax.partyapp.R.layout.dialog_location)

        dialog.btnYes.setOnClickListener {
            dialog.dismiss()
        }

        dialog.btnNo.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun doLogin(loginBodyModel: LoginBodyModel) {

        showProgressBar(this)

        WebService.callLoginAPI(loginBodyModel) { responseObj, error ->

            hideProgressBar()

            if (error == null) {

                if (responseObj?.meta?.status == 200) {
                    Log.d("onLoginResponse", "" + responseObj)

                    //saving the token as a string in SharedPreference
                    SPreferences.setUserToken(responseObj.response.token)

                    Log.d("RESPONSE_TOKEN_STRING", "" + SPreferences.getUserToken())

                    getProfileApi()


                } else {

                    Log.d("onLoginResponse1", "" + responseObj)
                    ToastUtils.getInstance(applicationContext)?.showToast(responseObj?.response?.message)


                }
            } else {
                ToastUtils.getInstance(applicationContext)?.showToast(error)
            }
        }


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
}



