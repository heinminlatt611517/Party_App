package com.parallax.partyapp.views.Adapter

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.parallax.partyapp.Models.ResponsePosts
import com.parallax.partyapp.R
import kotlinx.android.synthetic.main.item_image_home.view.*
import loadImage

class PostDetailsImageAdapter(
    var context: Context?,
    var items: ArrayList<ResponsePosts.Response.Post.Image>
) :
    androidx.recyclerview.widget.RecyclerView.Adapter<PostDetailsImageAdapter.CustomViewHolder>() {


    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): CustomViewHolder {
        return CustomViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.item_image_home,
                null,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: CustomViewHolder, positon: Int) {

        loadImage(context!!, items[positon].reference_url, holder.ivProfile)
    }

    class CustomViewHolder(view: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(view) {
        var ivProfile = view.iv_image
    }

}