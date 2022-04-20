package com.parallax.partyapp.Utils

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import com.parallax.partyapp.R
import kotlinx.android.synthetic.main.layout_toast.view.*


class ToastUtils {
    private var mToast: Toast? = null

    companion object {
        private var mInstance: ToastUtils? = null
        private var mContext: Context? = null
        fun getInstance(ctx: Context): ToastUtils? {
            mContext = ctx
            if (mInstance == null) {
                mInstance = ToastUtils()
            }
            return mInstance
        }
    }


    fun showToast(text: String?) {
        val layout: View = LayoutInflater.from(mContext).inflate(R.layout.layout_toast, null, false)
        layout.text.text = text

        val toast = Toast(mContext)
//        toast.setGravity(Gravity., 0, 0)
        toast.duration = Toast.LENGTH_SHORT
        toast.view = layout
        toast.show()

//        if (mToast == null) {
//            mToast = Toast.makeText(mContext, text, Toast.LENGTH_SHORT)
//        } else {
//            mToast!!.setText(text)
//            mToast!!.duration = Toast.LENGTH_SHORT
//        }
//        val toastLayout = mToast?.view as LinearLayout
//        val toastTV = toastLayout.getChildAt(0) as TextView
//        val font = Typeface.createFromAsset(mContext?.assets, "za.ttf")
//        toastTV.typeface = font
//        toastTV.text = text
//        mToast!!.show()
    }

    fun cancelToast() {
        if (mToast != null) {
            mToast!!.cancel()
        }
    }
}