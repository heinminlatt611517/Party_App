package com.parallax.partyapp.views.Adapter

import android.app.Activity
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.parallax.partyapp.R
import kotlinx.android.synthetic.main.item_multi_image.view.*
import loadImage

class MultiImageAdapter(
    var context: Activity?,
    var items: ArrayList<String>
) :
    androidx.recyclerview.widget.RecyclerView.Adapter<MultiImageAdapter.CustomViewHolder>() {

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): CustomViewHolder {
        return CustomViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.item_multi_image,
                null,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: CustomViewHolder, positon: Int) {

        loadImage(context!!, items[positon], holder.ivPhoto)

    }

    class CustomViewHolder(view: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(view) {
        var ivPhoto = view.iv_image

    }

}