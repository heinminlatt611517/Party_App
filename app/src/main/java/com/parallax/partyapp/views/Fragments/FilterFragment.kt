package com.parallax.partyapp.views.Fragments


import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.parallax.partyapp.Api.WebService
import com.parallax.partyapp.Models.PostFilter
import com.parallax.partyapp.Models.ResponseSetFilter
import com.parallax.partyapp.R
import com.parallax.partyapp.Utils.RuntimeData
import com.parallax.partyapp.Utils.hide
import com.parallax.partyapp.Utils.isVisible
import com.parallax.partyapp.Utils.show
import com.parallax.partyapp.interfaces.DataListener
import com.parallax.partyapp.views.AppFlow.MainActivity
import hideProgressBar
import kotlinx.android.synthetic.main.dialog_message.*
import kotlinx.android.synthetic.main.fragment_filter.*
import kotlinx.android.synthetic.main.fragment_filter.view.*
import showProgressBar


class FilterFragment : androidx.fragment.app.Fragment() {

    private lateinit var viewGroup: View

    var responseFilter: ResponseSetFilter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        viewGroup = inflater.inflate(R.layout.fragment_filter, container, false)


        initClickListener()

        responseFilter?.let {
            processResponse(it)
        }

        getFilter()

        return viewGroup
    }


    fun getFilter() {
        WebService.getFilter(object : DataListener<ResponseSetFilter> {
            override fun onStartCall() {
                if (responseFilter == null) showProgressBar(activity as MainActivity)
            }

            override fun onResponse(responseObj: ResponseSetFilter?) {
                hideProgressBar()

                responseObj?.let {
                    if (responseObj.meta.status == 200) {

                        processResponse(it)
                    }
                }
            }

            override fun onError(errorMessage: String) {
                hideProgressBar()
            }
        })
    }

    private fun processResponse(it: ResponseSetFilter) {

        responseFilter = it

        if (it.response.filter_setting != null) {

            val filter_setting = it.response.filter_setting

            if (filter_setting.most_recent == 1) viewGroup.iv_most_recent.show() else viewGroup.iv_most_recent.hide()
            if (filter_setting.following == 1) viewGroup.iv_following.show() else viewGroup.iv_following.hide()
            if (filter_setting.nearest == 1) viewGroup.iv_closest_location.show() else viewGroup.iv_closest_location.hide()
            if (filter_setting.liked == 1) viewGroup.iv_most_liked.show() else viewGroup.iv_most_liked.hide()
            if (filter_setting.comments == 1) viewGroup.iv_most_comment.show() else viewGroup.iv_most_comment.hide()
            if (filter_setting.crowded == 1) viewGroup.iv_crowded.show() else viewGroup.iv_crowded.hide()
            if (filter_setting.quiet == 1) viewGroup.iv_quite.show() else viewGroup.iv_quite.hide()
            if (filter_setting.fun_limit == 1) viewGroup.iv_fun.show() else viewGroup.iv_fun.hide()
            if (filter_setting.last_twenty_four == 1) viewGroup.iv_last_24.show() else viewGroup.iv_last_24.hide()
            if (filter_setting.last_forty_eight == 1) viewGroup.iv_last_48.show() else viewGroup.iv_last_48.hide()
        }
    }

    private fun initClickListener() {

        viewGroup.layout_most_recent.setOnClickListener {
            //            if (viewGroup.iv_most_recent.isVisible()) {
//                viewGroup.iv_following.hide()
//                viewGroup.iv_most_recent.hide()
//            } else {
//                viewGroup.iv_following.hide()
//                viewGroup.iv_most_recent.show()
//            }
            checkVisibility(iv_most_recent)
        }

        viewGroup.layout_following.setOnClickListener {

            if (viewGroup.iv_following.isVisible()) {
                viewGroup.iv_following.hide()
//                viewGroup.iv_most_recent.hide()
            } else {
                viewGroup.iv_following.show()
//                viewGroup.iv_most_recent.hide()
            }

        }

        viewGroup.layout_closest_location.setOnClickListener {
            checkVisibility(iv_closest_location)
        }

        viewGroup.layout_most_liked.setOnClickListener {
            checkVisibility(iv_most_liked)
        }

        viewGroup.layout_most_comment.setOnClickListener {
            checkVisibility(iv_most_comment)
        }

        viewGroup.layout_crowded.setOnClickListener {
            //            checkVisibility(iv_crowded)
            if (viewGroup.iv_crowded.isVisible()) {
                viewGroup.iv_crowded.hide()
                viewGroup.iv_quite.hide()
            } else {
                viewGroup.iv_crowded.show()
                viewGroup.iv_quite.hide()
            }
        }

        viewGroup.layout_quite.setOnClickListener {
            //            checkVisibility(iv_quite)
            if (viewGroup.iv_quite.isVisible()) {
                viewGroup.iv_crowded.hide()
                viewGroup.iv_quite.hide()
            } else {
                viewGroup.iv_crowded.hide()
                viewGroup.iv_quite.show()
            }
        }

        viewGroup.layout_fun.setOnClickListener {
            checkVisibility(iv_fun)
        }

        viewGroup.layout_last_24.setOnClickListener {

            if (viewGroup.iv_last_24.isVisible()) {
                viewGroup.iv_last_24.hide()
                viewGroup.iv_last_48.hide()
            } else {
                viewGroup.iv_last_24.show()
                viewGroup.iv_last_48.hide()
            }

        }

        viewGroup.layout_last_48.setOnClickListener {

            if (viewGroup.iv_last_48.isVisible()) {
                viewGroup.iv_last_24.hide()
                viewGroup.iv_last_48.hide()
            } else {
                viewGroup.iv_last_24.hide()
                viewGroup.iv_last_48.show()
            }

        }


        viewGroup.tv_done.setOnClickListener {

            val postFilter = PostFilter()
            postFilter.most_recent = if (viewGroup.iv_most_recent.isVisible()) 1 else 0
            postFilter.following = if (viewGroup.iv_following.isVisible()) 1 else 0
            postFilter.nearest = if (viewGroup.iv_closest_location.isVisible()) 1 else 0
            postFilter.liked = if (viewGroup.iv_most_liked.isVisible()) 1 else 0
            postFilter.comments = if (viewGroup.iv_most_comment.isVisible()) 1 else 0
            postFilter.crowded = if (viewGroup.iv_crowded.isVisible()) 1 else 0
            postFilter.quiet = if (viewGroup.iv_quite.isVisible()) 1 else 0
            postFilter.fun_limit = if (viewGroup.iv_fun.isVisible()) 1 else 0
            postFilter.last_twenty_four = if (viewGroup.iv_last_24.isVisible()) 1 else 0
            postFilter.last_forty_eight = if (viewGroup.iv_last_48.isVisible()) 1 else 0

            postFilter.latitude = RuntimeData.latLng2?.lat
            postFilter.longitudes = RuntimeData.latLng2?.lng

            setFilter(postFilter, true)

        }

        viewGroup.tv_clear.setOnClickListener {
            val postFilter = PostFilter()
            postFilter.most_recent = 0
            postFilter.following = 0
            postFilter.nearest = 0
            postFilter.liked = 0
            postFilter.comments = 0
            postFilter.crowded = 0
            postFilter.quiet = 0
            postFilter.fun_limit = 0
            postFilter.last_twenty_four = 0
            postFilter.last_forty_eight = 0

            postFilter.latitude = RuntimeData.latLng2?.lat
            postFilter.longitudes = RuntimeData.latLng2?.lng

            setFilter(postFilter, false)
        }

    }

    private fun setFilter(postFilter: PostFilter, isCheckPostSize: Boolean) {


        WebService.setFilter(postFilter, object : DataListener<ResponseSetFilter> {
            override fun onStartCall() {
                showProgressBar(activity as MainActivity)
            }

            override fun onResponse(responseObj: ResponseSetFilter?) {
                hideProgressBar()

                Log.d("post count", responseObj?.response?.filter_posts.toString())

                getFilter()

                if (isCheckPostSize) {

                    if (responseObj?.response?.filter_posts!! > 0) {
                        RuntimeData.isComeFromFilter = true
                        (activity as MainActivity).switchTab(R.id.home_menu)
                    } else {

                        val dialog = Dialog(activity)
                        dialog.setContentView(R.layout.dialog_message)

                        dialog.tv_message.text = "No post found"
                        dialog.btn_ok.setOnClickListener { dialog.dismiss() }

                        dialog.show()

                        val postNoFilter = PostFilter()
                        postNoFilter.most_recent = 0
                        postNoFilter.following = 0
                        postNoFilter.nearest = 0
                        postNoFilter.liked = 0
                        postNoFilter.comments = 0
                        postNoFilter.crowded = 0
                        postNoFilter.quiet = 0
                        postNoFilter.fun_limit = 0
                        postNoFilter.last_twenty_four = 0
                        postNoFilter.last_forty_eight = 0

                        postNoFilter.latitude = RuntimeData.latLng2?.lat
                        postNoFilter.longitudes = RuntimeData.latLng2?.lng

                        setFilter(postNoFilter, false)

                    }
                }
            }

            override fun onError(errorMessage: String) {
                hideProgressBar()
            }
        })
    }


    fun checkVisibility(iv: View) {

        if (iv.isVisible()) {
            iv.hide()
            return
        }

        if (iv == iv_crowded) {
            iv_quite.hide()
        }
        if (iv == iv_quite) {
            iv_crowded.hide()
        }
//        iv_closest_location.hide()
//        iv_most_recent.hide()
//        iv_most_liked.hide()
//        iv_most_comment.hide()
//        iv_crowded.hide()
//        iv_quite.hide()
//        iv_fun.hide()

        iv.show()
    }


}
