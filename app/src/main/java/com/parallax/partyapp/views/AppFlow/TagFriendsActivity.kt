package com.parallax.partyapp.views.AppFlow

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import android.util.Log
import com.google.gson.Gson
import com.parallax.partyapp.Api.WebService
import com.parallax.partyapp.Models.ResponseFollowers
import com.parallax.partyapp.Models.ResponseFollowing
import com.parallax.partyapp.R
import com.parallax.partyapp.Utils.hide
import com.parallax.partyapp.Utils.show
import com.parallax.partyapp.interfaces.DataListener
import com.parallax.partyapp.interfaces.FollowerFollowingInterface
import com.parallax.partyapp.interfaces.OnListItemClickListener
import com.parallax.partyapp.views.Adapter.TagFriendsAdapter
import com.parallax.partyapp.views.BaseActivity
import kotlinx.android.synthetic.main.activity_tag_friends.*

class TagFriendsActivity : BaseActivity() {

    companion object {
        var userIdForTag = arrayListOf<Int>()
    }

    private lateinit var tagFriendsAdapter: TagFriendsAdapter
    var peopleList = arrayListOf<FollowerFollowingInterface>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tag_friends)

        tagFriendsAdapter = TagFriendsAdapter(this, peopleList,
            object : OnListItemClickListener<FollowerFollowingInterface> {
                override fun onListItemClick(obj: FollowerFollowingInterface) {

                    if (obj is ResponseFollowers.Response.FollowLists) {

                        if (userIdForTag.contains(obj.following.id)) {
                            userIdForTag.remove(obj.following.id)
                        } else {
                            userIdForTag.add(obj.following.id)
                        }

                    } else if (obj is ResponseFollowing.Response.FollowLists) {

                        if (userIdForTag.contains(obj.follow.id)) {
                            userIdForTag.remove(obj.follow.id)
                        } else {
                            userIdForTag.add(obj.follow.id)
                        }

                    }

                    Log.d("ssssssss", Gson().toJson(userIdForTag))


                }
            })

        recyclerView.layoutManager =
            androidx.recyclerview.widget.LinearLayoutManager(
                applicationContext,
                androidx.recyclerview.widget.LinearLayoutManager.VERTICAL,
                false
            )
        recyclerView.adapter = tagFriendsAdapter

        getFollowings()

        btn_done.setOnClickListener { onBackPressed() }

        btnBack.setOnClickListener { onBackPressed() }
    }


    fun getFollowings() {
        WebService.getFollowing("", object : DataListener<ResponseFollowing> {
            override fun onStartCall() {

            }

            override fun onResponse(responseObj: ResponseFollowing?) {

                responseObj?.let {
                    if (it.meta.status == 200) {

                        if (it.response.follow_lists.isNotEmpty()) {

                            peopleList.clear()
                            peopleList.addAll(it.response.follow_lists)

                            tagFriendsAdapter.notifyDataSetChanged()

                            tv_empty.hide()

                        } else {
                            tv_empty.show()
                        }

//                        getFollower()
                    }
                }

            }

            override fun onError(errorMessage: String) {

            }
        })
    }

//    fun getFollower() {
//        WebService.getFollowers(object : DataListener<ResponseFollowers> {
//            override fun onStartCall() {
//
//            }
//
//            override fun onResponse(responseObj: ResponseFollowers?) {
//
//                responseObj?.let {
//                    if (it.meta.status == 200) {
//                        peopleList.addAll(it.response.follow_lists)
//
//                        tagFriendsAdapter.notifyDataSetChanged()
//                    }
//                }
//            }
//
//            override fun onError(errorMessage: String) {
//
//            }
//        })
//    }
}
