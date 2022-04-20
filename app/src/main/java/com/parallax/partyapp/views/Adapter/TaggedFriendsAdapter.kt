package com.parallax.partyapp.views.Adapter

import android.app.Activity
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.parallax.partyapp.Models.ResponsePosts
import com.parallax.partyapp.R
import kotlinx.android.synthetic.main.item_tagged_friend.view.*
import loadImage

class TaggedFriendsAdapter(
    var context: Activity?,
    var items: List<ResponsePosts.Response.Post.Tag>
) :
    androidx.recyclerview.widget.RecyclerView.Adapter<TaggedFriendsAdapter.CustomViewHolder>() {

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): CustomViewHolder {
        return CustomViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.item_tagged_friend,
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

        loadImage(context!!, items[positon].users.image_url, holder.ivProfile)


    }

    class CustomViewHolder(view: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(view) {
        var ivProfile = view.ic_profile
        var tvName = view.tv_name

    }

}