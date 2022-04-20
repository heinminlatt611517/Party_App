package com.parallax.partyapp.views.AppFlow

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.parallax.partyapp.Models.ResponsePosts
import com.parallax.partyapp.R
import com.parallax.partyapp.views.Adapter.TaggedFriendsAdapter
import com.parallax.partyapp.views.BaseActivity
import kotlinx.android.synthetic.main.activity_tagged_friend_list.*

class TaggedFriendListActivity : BaseActivity() {

    companion object {
        val KEY_POST = "key post"
    }

    private lateinit var likedAdapter: TaggedFriendsAdapter
    var objPost: ResponsePosts.Response.Post? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tagged_friend_list)


        objPost = Gson().fromJson(
            intent?.getStringExtra(KEY_POST),
            ResponsePosts.Response.Post::class.java
        )


        likedAdapter = TaggedFriendsAdapter(this, objPost?.tags!!)

        recycler_view.layoutManager =
            androidx.recyclerview.widget.LinearLayoutManager(
                applicationContext,
                androidx.recyclerview.widget.RecyclerView.VERTICAL,
                false
            )
        recycler_view.adapter = likedAdapter


        btnBack.setOnClickListener {
            onBackPressed()
        }

    }
}
