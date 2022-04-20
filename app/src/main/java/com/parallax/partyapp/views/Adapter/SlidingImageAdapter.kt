package com.parallax.partyapp.views.Adapter

import android.content.Context
import android.os.Parcelable
import androidx.viewpager.widget.PagerAdapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.parallax.partyapp.R
import kotlinx.android.synthetic.main.item_image_slide.view.*

class SlidingImageAdapter(
    private val context: Context,
    private val items: List<Int>
) :
    androidx.viewpager.widget.PagerAdapter() {
    private val inflater: LayoutInflater

    init {
        inflater = LayoutInflater.from(context)
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }

    override fun getCount(): Int {
        return items.size
    }

    override fun instantiateItem(view: ViewGroup, position: Int): Any {
        val imageLayout = inflater.inflate(R.layout.item_image_slide, view, false)


        Glide.with(context!!).load(context.resources.getDrawable(items.get(position)))
//            .apply(RequestOptions().error(R.drawable.ic_error_image).placeholder(R.drawable.ic_error_image))
            .into(imageLayout.image)

        view.addView(imageLayout, 0)

        return imageLayout
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }

    override fun restoreState(state: Parcelable?, loader: ClassLoader?) {}

    override fun saveState(): Parcelable? {
        return null
    }


}