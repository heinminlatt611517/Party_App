package com.parallax.partyapp.views.Adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.gson.Gson
import com.parallax.partyapp.R
import com.parallax.partyapp.views.AppFlow.AdsActivity
import com.smarteist.autoimageslider.SliderViewAdapter
import kotlinx.android.synthetic.main.home_carousel_view.view.*
import loadImage


class SliderAdapterExample(var context: Context) :
    SliderViewAdapter<SliderAdapterExample.SliderAdapterVH>() {

    private var mSliderItems: MutableList<String> = ArrayList()
    fun renewItems(sliderItems: MutableList<String>) {
        mSliderItems = sliderItems
        notifyDataSetChanged()
    }

    fun deleteItem(position: Int) {
        mSliderItems.removeAt(position)
        notifyDataSetChanged()
    }

    fun addItem(sliderItem: String) {
        mSliderItems.add(sliderItem)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup): SliderAdapterVH {
        val inflate: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.home_carousel_view, null)
        return SliderAdapterVH(inflate)
    }

    override fun onBindViewHolder(
        viewHolder: SliderAdapterVH,
        position: Int
    ) {

        loadImage(
            context!!,
            HomeAdapter.addsList[position].ads_banner,
            viewHolder.itemView.iv_banner
        )

        viewHolder.itemView.btn_details.setOnClickListener {
            Intent(context, AdsActivity::class.java).let {
                it.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                it.putExtra(AdsActivity.KEY_REWARD_DETAILS, Gson().toJson(HomeAdapter.addsList[position]))
                context.startActivity(it)
            }
        }
    }

    override fun getCount(): Int {
        //slider view count could be dynamic size
        return mSliderItems.size
    }

    inner class SliderAdapterVH(itemView: View) : ViewHolder(itemView)
}