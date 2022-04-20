package com.parallax.partyapp.views.AppFlow.optional

import android.os.Bundle
import com.parallax.partyapp.R
import com.parallax.partyapp.views.BaseActivity

class EditProfileActivity : BaseActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)

//        btnSignIn.setOnClickListener {
////            when {
////                et_name.text.toString().isNullOrBlank() -> Toast.makeText(
////                    applicationContext,
////                    "Name filed is empty",
////                    Toast.LENGTH_LONG
////                ).show()
////                else -> {
////                    val setProfileBodyModel = SetProfileBodyModel()
////                    setProfileBodyModel.name
////                    setProfileBodyModel.gender
////                    setProfileBodyModel.phone
////                    setProfileBodyModel.bio
////                    setProfileBodyModel.date_of_birth
////                    getUpdateProfile(setProfileBodyModel)
////                }
////            }
//        }


    }

//    private fun getUpdateProfile(setProfileBodyModel: SetProfileBodyModel) {
//        WebService.callSetProfile(setProfileBodyModel, object : DataListener<ResponseSetProfile> {
//            override fun onStartCall() {
//                showProgressBar(this@EditProfileActivity)
//            }
//
//            override fun onResponse(responseObj: ResponseSetProfile?) {
//                hideProgressBar()
//                responseObj?.let {
//                    if (it.meta.status == 200) {
//                        startActivity(Intent(applicationContext, ProfileFragment::class.java))
//                        Toast.makeText(applicationContext, "" + it.response.message, Toast.LENGTH_SHORT).show()
//
//                    } else {
//                        hideProgressBar()
//                    }
//                }
//            }
//
//            override fun onError(errorMassage: String) {
////                hideProgressBar()
//                Toast.makeText(applicationContext, "" + errorMassage, Toast.LENGTH_LONG).show()
//            }
//
//
//        })
//    }
}
