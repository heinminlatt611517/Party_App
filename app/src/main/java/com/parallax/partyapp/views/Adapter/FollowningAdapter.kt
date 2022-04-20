package com.parallax.partyapp.views.Adapter

import android.content.Context
import android.content.Intent
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.parallax.partyapp.Models.ResponseFollowing
import com.parallax.partyapp.R
import com.parallax.partyapp.interfaces.OnListItemClickListener
import com.parallax.partyapp.views.AppFlow.ProfileActivity
import kotlinx.android.synthetic.main.item_followning.view.*

import loadImage

class FollowningAdapter(
    var context: Context?,
    var items: ArrayList<ResponseFollowing.Response.FollowLists>,
    val onListItemClickListener: OnListItemClickListener<ResponseFollowing.Response.FollowLists>
) : androidx.recyclerview.widget.RecyclerView.Adapter<FollowningAdapter.CustomViewHolder>() {


    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): CustomViewHolder {
        return CustomViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.item_followning,
                null,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: CustomViewHolder, positon: Int) {

        holder.tvName.text = items[positon].follow.name
        holder.tvDes.text = items[positon].follow.email

        loadImage(context!!, items[positon].follow.image_url, holder.ivProfile)

        holder.btnUnfollow.setOnClickListener {
            onListItemClickListener.onListItemClick(items[positon])
        }

        holder.tvName.setOnClickListener {
            goUserProfile(positon)
        }

        holder.ivProfile.setOnClickListener {
            goUserProfile(positon)
        }
    }

    private fun goUserProfile(positon: Int) {
        Intent(
            context,
            ProfileActivity::class.java
        ).let {
            it.putExtra(ProfileActivity.KEY_USER_ID, items[positon].follow.id)
            context?.startActivity(it)
        }
    }

    class CustomViewHolder(view: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(view) {

        var ivProfile = view.iv_profile

        var tvName = view.tv_name
        var tvDes = view.tv_des

        var btnUnfollow = view.btn_unfollow

    }

}