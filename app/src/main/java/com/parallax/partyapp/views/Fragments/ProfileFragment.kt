package com.parallax.partyapp.views.Fragments


import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.gson.Gson
import com.parallax.partyapp.Api.WebService
import com.parallax.partyapp.Data.SPreferences
import com.parallax.partyapp.Models.ResponseGetProfile
import com.parallax.partyapp.R
import com.parallax.partyapp.Utils.MEDIA_TYPE_PHOTO
import com.parallax.partyapp.Utils.ToastUtils
import com.parallax.partyapp.Utils.hide
import com.parallax.partyapp.Utils.show
import com.parallax.partyapp.interfaces.DataListener
import com.parallax.partyapp.interfaces.OnListItemClickListener
import com.parallax.partyapp.views.Adapter.ProfileAdapter
import com.parallax.partyapp.views.AppFlow.MediaDetailsActivity
import com.parallax.partyapp.views.AppFlow.optional.FollowersActivity
import com.parallax.partyapp.views.RegistrationFlow.EditeProfleScreenActivity
import com.vincent.filepicker.ToastUtil
import hideProgressBar
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.android.synthetic.main.fragment_profile.view.*
import showProgressBar


class ProfileFragment : androidx.fragment.app.Fragment(),
    OnListItemClickListener<ResponseGetProfile.Response.UserData.Post> {
    companion object {
        val KEY_USER_ID = "userId"
    }

    private var responseObj: ResponseGetProfile? = null

    private var items = arrayListOf<ResponseGetProfile.Response.UserData.Post>()

    lateinit var profileAdapter: ProfileAdapter

    var userId = -1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val viewGroup: ViewGroup =
            inflater.inflate(R.layout.fragment_profile, container, false) as ViewGroup

        userId = arguments?.getInt(KEY_USER_ID, -1)!!

        val id = SPreferences.getUserProfileObj()?.response?.user_data?.id

        if (userId == id) {
            viewGroup.btn_profile.show()
        } else {
            viewGroup.btn_profile.hide()
        }

        profileAdapter = ProfileAdapter(activity, items, this)

        viewGroup.recyclerView.layoutManager =
            androidx.recyclerview.widget.GridLayoutManager(
                context,
                3,
                androidx.recyclerview.widget.GridLayoutManager.VERTICAL,
                false
            )
        viewGroup.recyclerView.adapter = profileAdapter



        initClickListeners(viewGroup)

        getUserProfile()

        return viewGroup

    }

    private fun initClickListeners(viewGroup: ViewGroup) {
        viewGroup.layoutFollowers.setOnClickListener {

            Intent(context, FollowersActivity::class.java).let {
                it.putExtra(FollowersActivity.KEY_CHECKED_TAB, FollowrsFragment.VALUE_FOLLOWER_TAB)
                it.putExtra(
                    FollowersActivity.KEY_USER_ID,
                    this.responseObj?.response?.user_data?.id.toString()
                )
                startActivity(it)
            }
        }

        viewGroup.layoutFollowing.setOnClickListener {

            Intent(context, FollowersActivity::class.java).let {
                it.putExtra(FollowersActivity.KEY_CHECKED_TAB, FollowrsFragment.VALUE_FOLLOWING_TAB)
                it.putExtra(
                    FollowersActivity.KEY_USER_ID,
                    this.responseObj?.response?.user_data?.id.toString()
                )
                startActivity(it)
            }

        }

        viewGroup.btn_profile.setOnClickListener {
            startActivity(Intent(context, EditeProfleScreenActivity::class.java))
        }

        viewGroup.ic_back.setOnClickListener { activity?.onBackPressed() }
    }

    override fun onListItemClick(obj: ResponseGetProfile.Response.UserData.Post) {
        Intent(context, MediaDetailsActivity::class.java).let {
            it.putExtra(MediaDetailsActivity.KEY_ITEM, Gson().toJson(obj))
            startActivity(it)
        }
    }

    private fun getUserProfile() {
        WebService.getUserProfile(userId, object : DataListener<ResponseGetProfile> {
            override fun onStartCall() {
                showProgressBar(activity as Activity)
            }

            override fun onResponse(responseObj: ResponseGetProfile?) {
                hideProgressBar()

                if (responseObj?.meta?.status == 200) {
                    this@ProfileFragment.responseObj = responseObj
                    Log.d("getProfileResponse", "" + responseObj)

                    tv_user_name.text = responseObj?.response?.user_data?.name
                    tv_user_mail.text = responseObj?.response?.user_data?.email

                    tv_post_count.text = responseObj?.response?.user_data.posts_count.toString()
                    tv_follower_count.text =
                        responseObj?.response?.user_data.follow_count.toString()
                    tv_following_count.text =
                        responseObj?.response?.user_data.following_count.toString()

                    Glide.with(this@ProfileFragment)
                        .load(responseObj?.response?.user_data?.image_url)
                        .apply(RequestOptions.circleCropTransform())
                        .placeholder(resources.getDrawable(R.drawable.person_logo))
                        .error(resources.getDrawable(R.drawable.person_logo))
                        .into(et_profile_img)

                    for (i in 0 until responseObj.response.user_data.posts.size) {
                        val post = responseObj.response.user_data.posts[i]
                        if (post.type.toInt() == MEDIA_TYPE_PHOTO) {
                            val firstImgeObj =
                                ResponseGetProfile.Response.UserData.Post.Image().apply {
                                    reference_url = post.reference_url
                                }
                            responseObj.response.user_data.posts[i].images.add(0, firstImgeObj)
                        }
                    }


                    if (responseObj?.response?.user_data.posts.isNotEmpty()) {
                        items.clear()
                        items.addAll(responseObj?.response.user_data.posts)

                        profileAdapter.notifyDataSetChanged()

                        tv_empty.hide()
                    } else {
                        tv_empty.show()
                    }

//                    Toast.makeText(context, "" + responseObj.response.message, Toast.LENGTH_SHORT).show()
                } else {

                    Log.d("getProfileResponse", "" + responseObj)

                    ToastUtils.getInstance(context!!)?.showToast(responseObj?.response?.message)
                }

            }

            override fun onError(errorMessage: String) {
                hideProgressBar()
            }


        })
    }


}
