package com.parallax.partyapp.views.Adapter

import android.app.Activity
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.parallax.partyapp.Models.ResponseFollowers
import com.parallax.partyapp.Models.ResponseFollowing
import com.parallax.partyapp.R
import com.parallax.partyapp.interfaces.FollowerFollowingInterface
import com.parallax.partyapp.interfaces.OnListItemClickListener
import com.parallax.partyapp.views.AppFlow.TagFriendsActivity
import kotlinx.android.synthetic.main.item_tag_friend.view.*
import loadImage

class TagFriendsAdapter(
    var context: Activity?,
    var items: List<FollowerFollowingInterface>,
    var onListItemClickListener: OnListItemClickListener<FollowerFollowingInterface>
) :
    androidx.recyclerview.widget.RecyclerView.Adapter<TagFriendsAdapter.CustomViewHolder>() {


    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): CustomViewHolder {
        return CustomViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.item_tag_friend,
                null,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: CustomViewHolder, positon: Int) {

        if (items[positon] is ResponseFollowing.Response.FollowLists) {
            val item = items[positon] as ResponseFollowing.Response.FollowLists

            holder.tvName.text = item.follow.name

            loadImage(context?.applicationContext!!, item.follow.image_url, holder.ivPhoto, true)

            if (TagFriendsActivity.userIdForTag.contains(item.follow.id)) {
                holder.checkBox.isChecked = true
            }


        } else if (items[positon] is ResponseFollowers.Response.FollowLists) {
            val item = items[positon] as ResponseFollowers.Response.FollowLists

            holder.tvName.text = item.following.name

            loadImage(context?.applicationContext!!, item.following.image_url, holder.ivPhoto, true)
        }


        holder.checkBox.setOnCheckedChangeListener { buttonView, isChecked ->
            onListItemClickListener.onListItemClick(items[positon])
        }


    }

    class CustomViewHolder(view: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(view) {
        var ivPhoto = view.ic_profile!!
        var tvName = view.tvName!!
        var checkBox = view.check_box!!

    }

}