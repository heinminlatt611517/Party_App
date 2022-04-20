package com.parallax.partyapp.views.Adapter

import android.app.Activity
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.parallax.partyapp.R
import com.parallax.partyapp.interfaces.OnListItemClickListener
import kotlinx.android.synthetic.main.item_my_rewards.view.*

class MyRewardsAdapter(
    var context: Activity?, var itemsOne: List<String>,
    var onListItemClickListener: OnListItemClickListener<String>
) :
    androidx.recyclerview.widget.RecyclerView.Adapter<MyRewardsAdapter.CustomViewHolder>() {


    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): CustomViewHolder {
        return CustomViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.item_my_rewards,
                null,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return itemsOne.size
    }

    override fun onBindViewHolder(holder: CustomViewHolder, positon: Int) {

        holder.tvName.text = itemsOne[positon]

        holder.btnView.setOnClickListener { onListItemClickListener.onListItemClick(itemsOne[positon]) }


    }

    class CustomViewHolder(view1: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(view1) {
        var view = view1
        var tvName = view.tv_redeem!!
        var btnView = view.btn_view

    }

}