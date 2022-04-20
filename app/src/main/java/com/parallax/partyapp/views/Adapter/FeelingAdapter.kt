package com.parallax.partyapp.views.Adapter

import android.app.Activity
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import com.parallax.partyapp.R
import com.parallax.partyapp.views.AppFlow.MediaActivity
import kotlinx.android.synthetic.main.item_feeling.view.*
import pxToDp


class FeelingAdapter(var context: Activity?, var items: List<String>, var imageList: List<Int>) :
    androidx.recyclerview.widget.RecyclerView.Adapter<FeelingAdapter.CustomViewHolder>() {


    var checkBox: CheckBox? = null

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): CustomViewHolder {
        return CustomViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.item_feeling,
                null,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: CustomViewHolder, positon: Int) {


        holder.itemView.ic_glass.setImageDrawable(context?.resources?.getDrawable(imageList[positon]))
        holder.tvComment.text = items[positon]

        if (MediaActivity.postActivity != null && MediaActivity.postActivity == items[positon]) {
            holder.checkBox.isChecked = true
            checkBox = holder.checkBox
        }


        holder.checkBox.setOnCheckedChangeListener { buttonView, isChecked ->

            if (checkBox == null) {
                checkBox = holder.checkBox

                MediaActivity.postActivity = items[positon]

            } else if (checkBox != null && checkBox == holder.checkBox) {
                checkBox = null

                MediaActivity.postActivity = null

            } else if (checkBox != null && checkBox != holder.checkBox) {
                checkBox?.isChecked = false
                checkBox = holder.checkBox

                MediaActivity.postActivity = items[positon]

            }
        }
    }

    class CustomViewHolder(view: View) :
        androidx.recyclerview.widget.RecyclerView.ViewHolder(view) {
        var tvComment = view.tvTitle
        var checkBox = view.check_box

    }

}