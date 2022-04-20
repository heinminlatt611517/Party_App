package com.parallax.partyapp.views.RegistrationFlow

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.widget.Toast
import com.google.gson.Gson
import com.parallax.partyapp.Api.WebService
import com.parallax.partyapp.Data.SPreferences
import com.parallax.partyapp.Models.ResponseGetProfile
import com.parallax.partyapp.Models.SignupBodyModel
import com.parallax.partyapp.R
import com.parallax.partyapp.Utils.ToastUtils
import com.parallax.partyapp.interfaces.DataListener
import com.parallax.partyapp.views.BaseActivity
import com.vincent.filepicker.ToastUtil
import hideKeyboard
import hideProgressBar
import kotlinx.android.synthetic.main.activity_registration.*
import kotlinx.android.synthetic.main.dialog_invalid_email.*
import kotlinx.android.synthetic.main.dialog_pass_not_match.*
import showAlertDialog
import showProgressBar

class RegistrationActivity : BaseActivity() {

    lateinit var signupbodyModel: SignupBodyModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)

        signupbodyModel = SignupBodyModel()
        initOnClickListener()

    }

    private fun initOnClickListener() {

        btnSignIn.setOnClickListener {

            if (et_signup_name.text.toString().isEmpty()) {
                ToastUtils.getInstance(applicationContext)?.showToast(getString(R.string.name_field_can_not_be_empty))
                et_signup_name.requestFocus()
            } else if (et_signup_email.text.toString().isEmpty()) {
                ToastUtils.getInstance(applicationContext)?.showToast(getString(R.string.email_field_can_not_be_empty))
                et_signup_email.requestFocus()
            } else if (et_signup_password.text.toString().isEmpty()) {
                ToastUtils.getInstance(applicationContext)?.showToast(getString(R.string.you_must_enter_a_password))
                et_signup_password.requestFocus()
            } else if (et_signup_name.text.toString().isEmpty() || et_signup_email.text.toString().isEmpty() || et_signup_password.text.toString().isEmpty()) {
                ToastUtils.getInstance(applicationContext)?.showToast(getString(R.string.name_field_can_not_be_empty))
            } else {

                signupbodyModel.name = et_signup_name.text.toString()
                signupbodyModel.email = et_signup_email.text.toString()
                signupbodyModel.password = et_signup_password.text.toString()

                doRegistration(signupbodyModel)

            }
        }


//        et_email.setOnClickListener {
//            dialogInvalidEmail()
//        }
//
//        et_password_repeat.setOnClickListener {
//            dialogPasswordNotMatch()
//        }

        btnBack.setOnClickListener {
            onBackPressed()
        }
    }

    private fun dialogInvalidEmail() {

        val dialog = Dialog(this)
        dialog.setContentView(R.layout.dialog_invalid_email)

        dialog.btnSure.setOnClickListener {
            dialog.dismiss()
        }

        dialog.btnCancel.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun dialogPasswordNotMatch() {

        val dialog = Dialog(this)
        dialog.setContentView(R.layout.dialog_pass_not_match)

        dialog.btnTryAgain.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        hideKeyboard(this)
        return super.dispatchTouchEvent(ev)
    }

    fun doRegistration(signupbodyModel: SignupBodyModel) {

        showProgressBar(this)

        WebService.callRegistrationAPI(signupbodyModel) { responseObj, error ->

            hideProgressBar()

            if (error == null) {

                if (responseObj?.meta?.status == 200) {
                    Log.d("onResponse", "" + responseObj)

                    //saving the token as a string in SharedPreference
                    SPreferences.setUserToken(responseObj.response.token)

                    Log.d("RESPONSE_TOKEN_STRINGS", "" + SPreferences.getUserToken())

                    getProfileApi()

                } else {
//                    showAlertDialog(this, "The password must be at least 8 characters and combination of  atleast one upper letter and lower letter")
                    showAlertDialog(this, "" + responseObj?.response?.message!!)
                    Log.d("onResponse1", "" + responseObj)
                }
            } else {
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


                    Intent(applicationContext, EditeProfleScreenActivity::class.java).also {
                        it.putExtra(EditeProfleScreenActivity.KEY_COME_FROM_REGISTRATION, true)
                        startActivity(it)
                    }

//                    Intent(applicationContext, EditeProfleScreenActivity::class.java).also {
//                        it.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
//                    }
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
