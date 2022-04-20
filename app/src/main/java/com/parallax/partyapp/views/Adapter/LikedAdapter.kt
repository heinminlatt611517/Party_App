package com.parallax.partyapp.views.Adapter

import android.app.Activity
import android.content.Intent
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.parallax.partyapp.Api.WebService
import com.parallax.partyapp.Data.SPreferences
import com.parallax.partyapp.Models.FollowUserBodyModel
import com.parallax.partyapp.Models.ResponseFollowUser
import com.parallax.partyapp.Models.ResponseLikes
import com.parallax.partyapp.R
import com.parallax.partyapp.Utils.hide
import com.parallax.partyapp.Utils.show
import com.parallax.partyapp.interfaces.DataListener
import com.parallax.partyapp.views.AppFlow.ProfileActivity
import hideProgressBar
import kotlinx.android.synthetic.main.item_liked_list.view.*
import loadImage
import showProgressBar

class LikedAdapter(var context: Activity?, var items: List<ResponseLikes.Response.Post.Like>) :
    androidx.recyclerview.widget.RecyclerView.Adapter<LikedAdapter.CustomViewHolder>() {

    val userObj = SPreferences.getUserProfileObj()

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): CustomViewHolder {
        return CustomViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.item_liked_list,
                null,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: CustomViewHolder, positon: Int) {

        holder.tvName.text = items[positon].users.name

        loadImage(context!!, items[positon].users.image_url, holder.ivProfile, true)

        if (items[positon].follow_status == 1) {
            holder.btnFollow.hide()
        } else {
            holder.btnFollow.show()
        }


        if (userObj?.response?.user_data?.id == items[positon].user_id) {
            holder.btnFollow.hide()
            holder.tvFollowing.hide()
        }


        holder.btnFollow.setOnClickListener {

            val followuser = FollowUserBodyModel()
            followuser.follower_id = items[positon].user_id

            WebService.callFollowUser(followuser, object : DataListener<ResponseFollowUser> {
                override fun onStartCall() {
                    showProgressBar(context as Activity)
                }

                override fun onResponse(responseObj: ResponseFollowUser?) {
                    hideProgressBar()
                    responseObj?.let {
                        if (it.meta.status == 200) {
                            holder.btnFollow.hide()
                        }
                    }
                }

                override fun onError(errorMessage: String) {
                    hideProgressBar()

                }

            })
        }
        
        holder.ivProfile.setOnClickListener {
            goUserProfile(positon)
        }

        holder.tvName.setOnClickListener {
            goUserProfile(positon)
        }

    }

    private fun goUserProfile(positon: Int) {
        Intent(
            context,
            ProfileActivity::class.java
        ).let {
            it.putExtra(ProfileActivity.KEY_USER_ID, items[positon].user_id)
            context?.startActivity(it)
        }
    }

    class CustomViewHolder(view: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(view) {
        var ivProfile = view.ic_profile
        var tvName = view.tv_name
        var btnFollow = view.btn_follow
        var tvFollowing = view.tv_following

    }

}