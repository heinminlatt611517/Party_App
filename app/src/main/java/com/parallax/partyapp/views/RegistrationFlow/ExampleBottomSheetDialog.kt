package com.parallax.partyapp.views.RegistrationFlow

import android.os.Bundle
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import com.parallax.partyapp.R

class ExampleBottomSheetDialog : BottomSheetDialogFragment() {

    var mListener: BottomSheetListener? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v = inflater.inflate(R.layout.bottom_sheet, container, false)

        val cameraOpen = v.findViewById<LinearLayout>(R.id.button1)
        val photoFileOpen = v.findViewById<LinearLayout>(R.id.button2)

        cameraOpen.setOnClickListener {
            mListener!!.onCameraClicked()
            dismiss()
        }
        photoFileOpen.setOnClickListener {
            mListener!!.onFileClicked()
            dismiss()
        }

        return v
    }

    interface BottomSheetListener {
        fun onCameraClicked()
        fun onFileClicked()
    }
}
