package com.parallax.partyapp.views.Adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.parallax.partyapp.Models.ResponseLoyalty
import com.parallax.partyapp.Models.ResponsePosts
import com.parallax.partyapp.R
import kotlinx.android.synthetic.main.item_reward.view.*
import kotlinx.android.synthetic.main.items_loyality.view.*
import loadImage
import java.util.*

class RewardAdapter(
    var context: Context?,
    var onClick: (ResponsePosts.Response.PromoData) -> Unit
) :
    androidx.recyclerview.widget.RecyclerView.Adapter<RewardAdapter.CustomViewHolder>() {

    val items = ArrayList<ResponsePosts.Response.PromoData>()

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): CustomViewHolder {
        return CustomViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.item_reward,
                null,
                false
            )
        )
    }

    fun setItems(newItems: ArrayList<ResponsePosts.Response.PromoData>) {
        items.clear()
        items.addAll(newItems)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: CustomViewHolder, positon: Int) {

        Log.d("aaaaaaaa", positon.toString())

        loadImage(context!!, items[positon].promo_image_logo, holder.itemView.iv)
        holder.itemView.tv_promo_name.text = items[positon].promo_image_title

        holder.itemView.setOnClickListener { onClick(items[positon]) }

    }

    class CustomViewHolder(view: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(view)

}