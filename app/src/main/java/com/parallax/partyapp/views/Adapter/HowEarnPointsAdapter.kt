package com.parallax.partyapp.views.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.parallax.partyapp.Models.ResponsePosts
import com.parallax.partyapp.R
import com.parallax.partyapp.Utils.*
import kotlinx.android.synthetic.main.activity_how_earn_point.*
import kotlinx.android.synthetic.main.item_how_earn_points.view.*
import kotlin.collections.ArrayList

class HowEarnPointsAdapter(
    var context: Context?,
    var items: ArrayList<ResponsePosts.Response.TireDetails>
) :
    androidx.recyclerview.widget.RecyclerView.Adapter<HowEarnPointsAdapter.CustomViewHolder>() {

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): CustomViewHolder {
        return CustomViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.item_how_earn_points,
                null,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: CustomViewHolder, positon: Int) {

        holder.itemView.tv_type_name.text = items[positon].type_name

        holder.itemView.tv_point_to_from.text =
            "${items[positon].point_from} pts - ${items[positon].point_to} pts"


        holder.itemView.tv_post_point.text = "${items[positon].earningpoints?.post} pts"
        holder.itemView.tv_receive_like.text =
            "${items[positon].earningpoints?.receive_like_comment} pts"
        holder.itemView.tv_give_like.text = "${items[positon].earningpoints?.give_like_comment} pts"

        when (items[positon].type_name) {
            TIER_SOCIALIZER -> {
                holder.itemView.iv_type_image.setImageResource(R.drawable.ic_star_socializer)
            }
            TIER_PARTYER -> {
                holder.itemView.iv_type_image.setImageResource(R.drawable.ic_star_partyer)
            }
            TIER_RAGER -> {
                holder.itemView.iv_type_image.setImageResource(R.drawable.ic_star_rager)
            }
            TIER_VIP -> {
                holder.itemView.iv_type_image.setImageResource(R.drawable.ic_star_vip)
            }
        }
    }

    class CustomViewHolder(view: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(view)

}