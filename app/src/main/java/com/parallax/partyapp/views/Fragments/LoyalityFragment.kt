package com.parallax.partyapp.views.Fragments

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.DisplayMetrics
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView
import com.bekawestberg.loopinglayout.library.LoopingLayoutManager
import com.google.gson.Gson
import com.parallax.partyapp.Api.WebService
import com.parallax.partyapp.Models.ResponseLoyalty
import com.parallax.partyapp.Models.ResponsePosts
import com.parallax.partyapp.R
import com.parallax.partyapp.Utils.*
import com.parallax.partyapp.interfaces.DataListener
import com.parallax.partyapp.views.Adapter.RewardAdapter
import com.parallax.partyapp.views.AppFlow.HowEarnPointActivity
import com.parallax.partyapp.views.AppFlow.MainActivity
import com.parallax.partyapp.views.AppFlow.RewardDetailsActivity
import getMatColor
import hideProgressBar
import kotlinx.android.synthetic.main.fragment_loyality.*
import kotlinx.android.synthetic.main.fragment_loyality.view.*
import rubikstudio.library.LuckyWheelView
import rubikstudio.library.model.LuckyItem
import showAlertDialog
import showProgressBar
import java.lang.Exception


class LoyalityFragment : androidx.fragment.app.Fragment() {

    private var scrollRunnable: Runnable? = null
    var scrollHandler: Handler? = null

    lateinit var viewGroup: ViewGroup

    var responseLoyalty: ResponseLoyalty? = null

    private var rewardAdapter: RewardAdapter? = null

    val items = ArrayList<ResponseLoyalty.Response.LoyalityLists>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        viewGroup =
            inflater.inflate(R.layout.fragment_loyality, container, false) as ViewGroup


        rewardAdapter = RewardAdapter(context) { obj ->
            Intent(context, RewardDetailsActivity::class.java).let { intent ->
                intent.putExtra(RewardDetailsActivity.KEY_REWARD_DETAILS, Gson().toJson(obj))
                startActivity(intent)
            }
        }

        val lm = object : LoopingLayoutManager(context!!, LoopingLayoutManager.HORIZONTAL, false) {
            override fun smoothScrollToPosition(
                recyclerView: RecyclerView,
                state: RecyclerView.State,
                position: Int
            ) {
                try {
                    val smoothScroller: LinearSmoothScroller =
                        object : LinearSmoothScroller(context) {
                            private val SPEED = 4000f // Change this value (default=25f)
                            override fun calculateSpeedPerPixel(displayMetrics: DisplayMetrics): Float {
                                return SPEED / displayMetrics.densityDpi
                            }
                        }
                    smoothScroller.targetPosition = position
                    startSmoothScroll(smoothScroller)
                } catch (e: Exception) {

                }
            }
        }

        viewGroup.rv_reward.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            var delayHandler = Handler()
            var runnable: Runnable = Runnable {
                autoScroll()
            }

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)

                if (newState == RecyclerView.SCROLL_STATE_DRAGGING) {
                    delayHandler.removeCallbacks(runnable)
                    scrollHandler?.removeCallbacks(scrollRunnable)
                    delayHandler.postDelayed(runnable, 3000)
                }
            }
        })


        viewGroup.rv_reward.layoutManager = lm
        viewGroup.rv_reward.adapter = rewardAdapter

        autoScroll()


        (activity as MainActivity).responsePosts.observe(this, androidx.lifecycle.Observer {
            if (it.response.promo_data.isEmpty()) {
                viewGroup.layout_reward.hide()
            } else {
                rewardAdapter?.setItems(it.response.promo_data)
            }

            tv_current_status.text = it.response.customer_type
            tv_current_point.text = it.response.current_om_points.toString()
            tv_next_tier_text.text =
                "Unlock ${it.response.next_tier} only ${it.response.points_to_next_tier} pts"

            when (it.response?.customer_type) {
                TIER_SOCIALIZER -> {
                    iv_current_status_logo.setImageResource(R.drawable.ic_star_socializer)

                    iv_indicator_1.show()
                    iv_indicator_2.invisible()
                    iv_indicator_3.invisible()
                    iv_indicator_4.invisible()
                }
                TIER_PARTYER -> {
                    iv_current_status_logo.setImageResource(R.drawable.ic_star_partyer)

                    iv_indicator_1.invisible()
                    iv_indicator_2.show()
                    iv_indicator_3.invisible()
                    iv_indicator_4.invisible()
                }
                TIER_RAGER -> {
                    iv_current_status_logo.setImageResource(R.drawable.ic_star_rager)

                    iv_indicator_1.invisible()
                    iv_indicator_2.invisible()
                    iv_indicator_3.show()
                    iv_indicator_4.invisible()
                }
                TIER_VIP -> {
                    iv_current_status_logo.setImageResource(R.drawable.ic_star_vip)

                    iv_indicator_1.invisible()
                    iv_indicator_2.invisible()
                    iv_indicator_3.invisible()
                    iv_indicator_4.show()
                }
            }

            initWheel(viewGroup, it.response.game_promo_data)


            viewGroup.bbtn_how_earn_points.setOnClickListener { view ->

                it.response

                Intent(activity, HowEarnPointActivity::class.java).let { intent ->
                    HowEarnPointActivity.responsePost = it.response
                    startActivity(intent)
                }
            }

        })


        viewGroup.btn_spin.setOnClickListener {
            viewGroup.luckyWheel.startLuckyWheelWithTargetIndex(0)
        }


        return viewGroup
    }

    fun autoScroll() {
        val speedScroll = 0
        scrollHandler = Handler()
        scrollRunnable = object : Runnable {
            var count = 0
            override fun run() {
                if (count == Int.MAX_VALUE) count = 0
                if (count < Int.MAX_VALUE ?: 0) {
                    viewGroup.rv_reward.smoothScrollToPosition(count++)
                    scrollHandler?.postDelayed(this, speedScroll.toLong())
                }
            }
        }
        scrollHandler?.postDelayed(scrollRunnable, speedScroll.toLong())
    }

    private fun initWheel(
        viewGroup: ViewGroup,
        gamePromoData: ArrayList<ResponsePosts.Response.GamePromoData>
    ) {

        val luckyWheelView = viewGroup.findViewById(R.id.luckyWheel) as LuckyWheelView
        val data: MutableList<LuckyItem> = ArrayList()

        gamePromoData.forEach {
            val luckyItem = LuckyItem()
            luckyItem.secondaryText = it.promo_name
            luckyItem.color = getMatColor(context!!, "500")
            data.add(luckyItem)
        }

        luckyWheelView.setData(data)
        luckyWheelView.setRound(10)

        luckyWheelView.setLuckyRoundItemSelectedListener {

        }
    }


    fun getLoyalty() {
        WebService.getLoyalty(object : DataListener<ResponseLoyalty> {
            override fun onStartCall() {
                if (responseLoyalty == null) showProgressBar(activity as Activity)
            }

            override fun onResponse(responseObj: ResponseLoyalty?) {
                hideProgressBar()

                responseObj?.let {
                    processResponse(responseObj)
                }
            }

            override fun onError(errorMessage: String) {
                hideProgressBar()
            }
        })
    }

    private fun processResponse(responseObj: ResponseLoyalty) {
        if (responseObj.meta.status == 200) {

            responseLoyalty = responseObj

            items.clear()
            items.addAll(responseObj.response.loyality_lists)

        } else {
            showAlertDialog(activity as Activity, getString(R.string.layalty_not_found))
        }
    }


}
