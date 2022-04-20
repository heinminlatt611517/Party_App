package com.parallax.partyapp.views.Adapter

import android.app.Activity
import androidx.recyclerview.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.parallax.partyapp.Models.ResponseGetProfile
import com.parallax.partyapp.R
import com.parallax.partyapp.Utils.MEDIA_TYPE_LIVE
import com.parallax.partyapp.Utils.MEDIA_TYPE_PHOTO
import com.parallax.partyapp.Utils.MEDIA_TYPE_VIDEO
import com.parallax.partyapp.interfaces.OnListItemClickListener
import kotlinx.android.synthetic.main.item_profile.view.*
import loadImage

class ProfileAdapter(
    var context: Activity?,
    var items: List<ResponseGetProfile.Response.UserData.Post>,
    var onListItemClickListener: OnListItemClickListener<ResponseGetProfile.Response.UserData.Post>
) :
    androidx.recyclerview.widget.RecyclerView.Adapter<ProfileAdapter.CustomViewHolder>() {


    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): CustomViewHolder {
        return CustomViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.item_profile,
                null,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: CustomViewHolder, positon: Int) {

        when (items[positon].type.toInt()) {
            MEDIA_TYPE_PHOTO -> {
                Log.d("imageUrl", items[positon].reference_url + "")
                loadImage(
                    context?.applicationContext!!,
                    items[positon].reference_url,
                    holder.ivPhoto
                )
            }
            MEDIA_TYPE_VIDEO -> {
                Log.d("imageUrl", items[positon].thumbnail_url + "")
                loadImage(
                    context?.applicationContext!!,
                    items[positon].thumbnail_url,
                    holder.ivPhoto
                )
            }
            MEDIA_TYPE_LIVE -> {
                Log.d("imageUrl", items[positon].thumbnail_url + "")
                loadImage(
                    context?.applicationContext!!,
                    items[positon].thumbnail_url,
                    holder.ivPhoto
                )
            }
        }

        holder.view.setOnClickListener { onListItemClickListener.onListItemClick(items[positon]) }

    }

    class CustomViewHolder(var view1: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(view1) {
        var view = view1
        var ivPhoto = view.iv_photo


    }

}