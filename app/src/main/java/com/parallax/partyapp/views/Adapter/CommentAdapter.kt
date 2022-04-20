package com.parallax.partyapp.views.Adapter

import android.app.Activity
import android.content.Intent
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.parallax.partyapp.Models.ResponsePostComment
import com.parallax.partyapp.R
import com.parallax.partyapp.views.AppFlow.ProfileActivity
import kotlinx.android.synthetic.main.item_comment.view.*
import loadImage

class CommentAdapter(
    var context: Activity?,
    var items: List<ResponsePostComment.Response.Post.Comment>
) : androidx.recyclerview.widget.RecyclerView.Adapter<CommentAdapter.CustomViewHolder>() {


    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): CustomViewHolder {
        return CustomViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.item_comment,
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
        holder.tvComment.text = items[positon].comment
        holder.tvTime.text = items[positon].created_at

        loadImage(context!!, items[positon].users.image_url, holder.ivPhofile)

        holder.tvName.setOnClickListener {
            goUserProfile(positon)
        }

        holder.ivPhofile.setOnClickListener {
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
        var ivPhofile = view.ivPhofile
        var tvName = view.tvName
        var tvComment = view.tvComment
        var tvTime = view.tv_time

    }

}