package com.parallax.partyapp.views.Fragments

import android.app.Activity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.parallax.partyapp.Api.WebService
import com.parallax.partyapp.Models.*
import com.parallax.partyapp.R
import com.parallax.partyapp.interfaces.DataListener
import com.parallax.partyapp.interfaces.OnListItemClickListener
import com.parallax.partyapp.views.Adapter.FollowerAdapter
import com.parallax.partyapp.views.Adapter.FollowningAdapter
import hideProgressBar
import kotlinx.android.synthetic.main.fragment_followrs.view.*
import showProgressBar


class FollowrsFragment : androidx.fragment.app.Fragment() {

    companion object {
        val KEY_USER_ID = "userId"
        val KEY_CHECKED_TAB = "checkedTab"

        val VALUE_FOLLOWER_TAB = "followerTab"
        val VALUE_FOLLOWING_TAB = "followingTab"
    }

    private lateinit var viewGroup: ViewGroup
    var followerList = arrayListOf<ResponseFollowers.Response.FollowLists>()
    var followingList = arrayListOf<ResponseFollowing.Response.FollowLists>()

    var followerAdapter: FollowerAdapter? = null
    var followingAdapter: FollowningAdapter? = null

    lateinit var userId: String
    lateinit var checkedTab: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        viewGroup = inflater.inflate(R.layout.fragment_followrs, container, false) as ViewGroup


        userId = arguments?.getString(KEY_USER_ID, "")!!
        checkedTab = arguments?.getString(KEY_CHECKED_TAB, VALUE_FOLLOWER_TAB)!!


        followerAdapter = FollowerAdapter(context, followerList,
            object : OnListItemClickListener<ResponseFollowers.Response.FollowLists> {
                override fun onListItemClick(obj: ResponseFollowers.Response.FollowLists) {
                    callFollowUser(obj.following.id)
                }

            })
        followingAdapter = FollowningAdapter(context, followingList,
            object : OnListItemClickListener<ResponseFollowing.Response.FollowLists> {
                override fun onListItemClick(obj: ResponseFollowing.Response.FollowLists) {
                    callUnFollowUser(obj.follow.id)
                }

            })

        viewGroup.recyclerView.layoutManager =
            androidx.recyclerview.widget.LinearLayoutManager(
                context,
                androidx.recyclerview.widget.LinearLayoutManager.VERTICAL,
                false
            )
        viewGroup.recyclerView.adapter = followerAdapter

        viewGroup.segmented2.setOnCheckedChangeListener { _, checkedId ->

            when (checkedId) {
                R.id.btnFollower -> {
                    viewGroup.recyclerView.adapter = followerAdapter
                    if (followerList.isNotEmpty()) {
                        viewGroup.tvNumberOfFollowers.text =
                            getString(
                                R.string.you_have_xx_follower,
                                followerList.size.toString()
                            )
                    } else {
                        viewGroup.tvNumberOfFollowers.text = "You have no follower"

                    }
                    viewGroup.tvTitle.text = getString(R.string.followers)


                }
                R.id.btnFollowing -> {
                    viewGroup.recyclerView.adapter = followingAdapter
                    if (followingList.isNotEmpty()) {
                        viewGroup.tvNumberOfFollowers.text =
                            getString(
                                R.string.you_are_following_xx,
                                followingList.size.toString()
                            )
                    } else {
                        viewGroup.tvNumberOfFollowers.text = "You are not following anyone"
                    }
                    viewGroup.tvTitle.text = getString(R.string.following)
                }
            }
        }


        when (checkedTab) {
            VALUE_FOLLOWER_TAB -> viewGroup.segmented2.check(R.id.btnFollower)
            VALUE_FOLLOWING_TAB -> viewGroup.segmented2.check(R.id.btnFollowing)
        }


        getFollowers()
        getFollowings()

        viewGroup.btnBack.setOnClickListener {
            activity?.onBackPressed()
        }


        return viewGroup

    }

    fun getFollowers() {
        WebService.getFollowers(userId, object : DataListener<ResponseFollowers> {
            override fun onStartCall() {

            }

            override fun onResponse(responseObj: ResponseFollowers?) {
                if (responseObj?.meta?.status == 200) {
                    if (responseObj?.response.follow_lists.isNotEmpty()) {
                        followerList.clear()
                        followerList.addAll(responseObj.response.follow_lists)
                        followerAdapter?.notifyDataSetChanged()

                        if (checkedTab == VALUE_FOLLOWER_TAB) {

                            if (followerList.isNotEmpty()) {
                                viewGroup.tvNumberOfFollowers.text =
                                    getString(
                                        R.string.you_have_xx_follower,
                                        followerList.size.toString()
                                    )
                            } else {
                                viewGroup.tvNumberOfFollowers.text = "You have no follower"

                            }

                        }

                    }
                }
            }

            override fun onError(errorMessage: String) {

            }

        })
    }

    fun getFollowings() {
        WebService.getFollowing(userId, object : DataListener<ResponseFollowing> {
            override fun onStartCall() {

            }

            override fun onResponse(responseObj: ResponseFollowing?) {
                if (responseObj?.meta?.status == 200) {
                    if (responseObj?.response.follow_lists != null) {
                        followingList.clear()
                        followingList.addAll(responseObj.response.follow_lists)
                        followingAdapter?.notifyDataSetChanged()

                        if (checkedTab == VALUE_FOLLOWING_TAB) {

                            if (followingList.isNotEmpty()) {
                                viewGroup.tvNumberOfFollowers.text =
                                    getString(
                                        R.string.you_are_following_xx,
                                        followingList.size.toString()
                                    )
                            } else {
                                viewGroup.tvNumberOfFollowers.text = "You are not following anyone"
                            }

                        }

                    }
                }
            }

            override fun onError(errorMessage: String) {

            }

        })
    }

    private fun callFollowUser(userId: Int) {
        val followUserBodyModel = FollowUserBodyModel()
        followUserBodyModel.follower_id = userId
        WebService.callFollowUser(followUserBodyModel, object : DataListener<ResponseFollowUser> {
            override fun onStartCall() {
                showProgressBar(activity as Activity)
            }

            override fun onResponse(responseObj: ResponseFollowUser?) {
                hideProgressBar()
                responseObj?.let {
                    if (responseObj.meta.status == 200) {
                        getFollowings()
                        getFollowers()
                        Log.d("callFollowUser", "200")
                    } else {
                        Log.d("callFollowUser", "100")
                    }
                }
            }

            override fun onError(errorMessage: String) {
                hideProgressBar()
            }
        })
    }

    private fun callUnFollowUser(userId: Int) {
        val unfollowUserBodyModel = UnfollowUserBodyModel()
        unfollowUserBodyModel.follower_id = userId
        WebService.callUnfollowUser(
            unfollowUserBodyModel,
            object : DataListener<ResponseUnfollowUser> {
                override fun onStartCall() {
                    showProgressBar(activity as Activity)
                }

                override fun onResponse(responseObj: ResponseUnfollowUser?) {
                    hideProgressBar()
                    responseObj?.let {
                        if (responseObj.meta.status == 200) {
                            getFollowings()
                            getFollowers()
                            Log.d("callFollowUser", "200")
                        } else {
                            Log.d("callFollowUser", "100")
                        }
                    }
                }

                override fun onError(errorMessage: String) {
                    hideProgressBar()
                }
            })
    }

}
