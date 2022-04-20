package com.parallax.partyapp.views.AppFlow

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.parallax.partyapp.Api.WebService
import com.parallax.partyapp.Models.ResponseLikes
import com.parallax.partyapp.Models.ResponsePosts
import com.parallax.partyapp.R
import com.parallax.partyapp.interfaces.DataListener
import com.parallax.partyapp.views.Adapter.LikedAdapter
import com.parallax.partyapp.views.BaseActivity
import hideProgressBar
import kotlinx.android.synthetic.main.activity_like.*
import showProgressBar

class LikeActivity : BaseActivity() {

    companion object {
        val KEY_POST = "key post"
    }

    private lateinit var likedAdapter: LikedAdapter
    var objPost: ResponsePosts.Response.Post? = null

    val items = arrayListOf<ResponseLikes.Response.Post.Like>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_like)

        objPost = Gson().fromJson(
            intent?.getStringExtra(KEY_POST),
            ResponsePosts.Response.Post::class.java
        )


        likedAdapter = LikedAdapter(this, items)

        recycler_view.layoutManager =
            androidx.recyclerview.widget.LinearLayoutManager(
                applicationContext,
                androidx.recyclerview.widget.RecyclerView.VERTICAL,
                false
            )
        recycler_view.adapter = likedAdapter

        getLikes()

        btnBack.setOnClickListener {
            onBackPressed()
        }
    }


    fun getLikes() {
        WebService.getLikes(objPost?.id!!, object : DataListener<ResponseLikes> {
            override fun onStartCall() {
                showProgressBar(this@LikeActivity)
            }

            override fun onResponse(responseObj: ResponseLikes?) {
                hideProgressBar()

                responseObj?.let {
                    if (it.meta.status == 200 && it.response.post.likes.isNotEmpty()) {
                        items.clear()
                        items.addAll(it.response.post.likes)
                        likedAdapter.notifyDataSetChanged()
                    }
                }
            }

            override fun onError(errorMessage: String) {
                hideProgressBar()
            }
        })
    }
}
