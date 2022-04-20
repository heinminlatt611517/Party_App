package com.parallax.partyapp.views.Adapter

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.parallax.partyapp.Models.ResponsePosts
import com.parallax.partyapp.R
import kotlinx.android.synthetic.main.item_ads.view.*
import loadImage


class AdsAdapter(var context: Context?) : androidx.recyclerview.widget.RecyclerView.Adapter<AdsAdapter.CustomViewHolder>() {

    var imageList = arrayListOf<ResponsePosts.Response.AddLists>()

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): CustomViewHolder {
        return CustomViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.item_ads,
                null,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return imageList.size
    }

    override fun onBindViewHolder(holder: CustomViewHolder, positon: Int) {

        loadImage(context!!, imageList[positon].ads_banner, holder.bgImg)

//        holder.bgImg.setImageDrawable(context?.resources?.getDrawable(imageList[positon]))
    }


    fun setItemList(items: ArrayList<ResponsePosts.Response.AddLists>) {
        imageList.clear()
        imageList.addAll(items)
        notifyDataSetChanged()
    }

    class CustomViewHolder(view: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(view) {


        var bgImg = view.imageView

    }

}