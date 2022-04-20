package com.parallax.partyapp.views.AppFlow

import android.content.Intent
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.MediaController
import com.google.gson.Gson
import com.opentok.android.OpentokError
import com.opentok.android.Session
import com.opentok.android.Stream
import com.opentok.android.Subscriber
import com.parallax.partyapp.Api.WebService
import com.parallax.partyapp.Models.ResponsePostComment
import com.parallax.partyapp.Models.ResponsePosts
import com.parallax.partyapp.R
import com.parallax.partyapp.Utils.*
import com.parallax.partyapp.interfaces.DataListener
import com.parallax.partyapp.views.Adapter.PostDetailsImageAdapter
import com.parallax.partyapp.views.BaseActivity
import kotlinx.android.synthetic.main.activity_media_details.*
import kotlinx.android.synthetic.main.item_comment.view.*
import loadImage

class MediaDetailsActivity : BaseActivity() {

    companion object {
        val KEY_ITEM = "keyitem"
    }

    private val LOG_TAG = this::class.java.simpleName

    var objPost: ResponsePosts.Response.Post? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_media_details)

        objPost = Gson().fromJson(
            intent?.getStringExtra(KEY_ITEM),
            ResponsePosts.Response.Post::class.java
        )

        if (objPost?.post_text.isNullOrBlank()) {
            tv_post_text.visibility = View.GONE
        } else {
            tv_post_text.visibility = View.VISIBLE
            tv_post_text.text = objPost?.post_text
        }

        if (objPost?.location.isNullOrBlank()) {
            tv_location.hide()
        } else {
            tv_location.text = objPost?.location
        }

        if (objPost?.crowd_count == 0.toDouble()) layout_crowd.hide()
        if (objPost?.fun_count == 0.toDouble()) layout_fun.hide()

//        if (objPost?.latitude.isNullOrBlank()) iv_location.hide()

        ratingBarCrowed.rating = objPost?.crowd_count?.toFloat()!!
        ratingBarFun.rating = objPost?.fun_count?.toFloat()!!

        ratingBarCrowed.setIsIndicator(true)
        ratingBarFun.setIsIndicator(true)

        val taggedFriend = StringBuilder()

        if (objPost?.tags != null) {
            for (i in 0 until objPost?.tags?.size!!) {

                if (i < 2) {
                    if (i != 0) taggedFriend.append(", ")
                    taggedFriend.append(objPost?.tags!![i].users.name)
                } else {
                    if (i == 2) {
                        taggedFriend.append(", See more")

                        tv_post_name.setOnClickListener {
                            Intent(applicationContext, TaggedFriendListActivity::class.java).apply {
                                putExtra(TaggedFriendListActivity.KEY_POST, Gson().toJson(objPost))
                                startActivity(this)
                            }
                        }
                    }
                }
            }
        }

        tv_feeling.text = objPost?.feel?.toString() ?: ""
        tv_activity.text = objPost?.activity?.toString() ?: ""

        when (objPost?.activity?.toString()) {
            Activities.CELEBRATING.text -> {
                iv_activity.setImageResource(Activities.CELEBRATING.icon)
            }
            Activities.DANCING.text -> {
                iv_activity.setImageResource(Activities.DANCING.icon)
            }
            Activities.DRINKING.text -> {
                iv_activity.setImageResource(Activities.DRINKING.icon)
            }
            Activities.LISTING_MUSIC.text -> {
                iv_activity.setImageResource(Activities.LISTING_MUSIC.icon)
            }
            Activities.PARTYING.text -> {
                iv_activity.setImageResource(Activities.PARTYING.icon)
            }
            Activities.SINGING.text -> {
                iv_activity.setImageResource(Activities.SINGING.icon)
            }
        }


        when (objPost?.feel?.toString()) {
            Feeling.Awesome.text -> {
                iv_feeling.setImageResource(Feeling.Awesome.icon)
            }
            Feeling.Drunk.text -> {
                iv_feeling.setImageResource(Feeling.Drunk.icon)
            }
            Feeling.Excited.text -> {
                iv_feeling.setImageResource(Feeling.Excited.icon)
            }
            Feeling.Happy.text -> {
                iv_feeling.setImageResource(Feeling.Happy.icon)
            }
            Feeling.Sexy.text -> {
                iv_feeling.setImageResource(Feeling.Sexy.icon)
            }
            Feeling.Silly.text -> {
                iv_feeling.setImageResource(Feeling.Silly.icon)
            }
            Feeling.Sober.text -> {
                iv_feeling.setImageResource(Feeling.Sober.icon)
            }
            Feeling.Tipsy.text -> {
                iv_feeling.setImageResource(Feeling.Tipsy.icon)
            }
        }



        tv_post_name.text =
            "${objPost?.users?.name} ${if (taggedFriend.toString().isNullOrBlank()) {
                ""
            } else {
                "-With ${taggedFriend.toString()}"
            }}"


        loadImage(applicationContext, objPost?.users?.image_url, ic_profile2, true)

        when (objPost?.type?.toInt()) {
            MEDIA_TYPE_PHOTO -> {

                recycler_view.visibility = View.VISIBLE
                iv_post_image.visibility = View.GONE
                vv_post_vedio.visibility = View.GONE
                subscriber_container.visibility = View.GONE

                recycler_view.layoutManager =
                    androidx.recyclerview.widget.LinearLayoutManager(
                        applicationContext,
                        androidx.recyclerview.widget.RecyclerView.VERTICAL,
                        false
                    )
                recycler_view.adapter =
                    PostDetailsImageAdapter(applicationContext, objPost?.images!!)


//                iv_post_image.visibility = View.VISIBLE
//                vv_post_vedio.visibility = View.GONE
//                subscriber_container.visibility = View.GONE
//
//                loadImage(applicationContext!!, objPost?.reference_url, iv_post_image)
            }
            MEDIA_TYPE_VIDEO -> {
                recycler_view.visibility = View.GONE
                iv_post_image.visibility = View.GONE
                vv_post_vedio.visibility = View.VISIBLE
                subscriber_container.visibility = View.GONE

                objPost?.reference_url?.let {
                    vv_post_vedio.setVideoPath(objPost?.reference_url)
                    vv_post_vedio.seekTo(100)

                    val mediaController = MediaController(this)
                    mediaController.setAnchorView(vv_post_vedio)

                    vv_post_vedio.setMediaController(mediaController)
                }
            }
            MEDIA_TYPE_LIVE -> {

                layout_1.hide()

                if (objPost?.opentok_s3.isNullOrBlank()) {
                    recycler_view.visibility = View.GONE
                    iv_post_image.visibility = View.GONE
                    vv_post_vedio.visibility = View.GONE
                    subscriber_container.visibility = View.VISIBLE

                    if (!objPost?.opentok_session.isNullOrBlank() && !objPost?.opentok_token.isNullOrBlank()) {
                        initializeAndConnectToSession(
                            objPost?.opentok_session!!,
                            objPost?.opentok_token!!
                        )
                    }
                } else {
                    //process video
                    recycler_view.visibility = View.GONE
                    iv_post_image.visibility = View.GONE
                    vv_post_vedio.visibility = View.VISIBLE
                    subscriber_container.visibility = View.GONE

                    objPost?.opentok_s3?.let {
                        vv_post_vedio.setVideoPath(objPost!!.opentok_s3.split("?")[0])
                        vv_post_vedio.seekTo(100)

                        val mediaController = MediaController(this)
                        mediaController.setAnchorView(vv_post_vedio)

                        vv_post_vedio.setMediaController(mediaController)
                    }
                }

            }
        }




        btn_back.setOnClickListener { onBackPressed() }

        tv_see_all.setOnClickListener {
            Intent(applicationContext, CommentActivity::class.java).let {
                it.putExtra(CommentActivity.KEY_POST_ID, objPost?.id)
                it.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                applicationContext?.startActivity(it)
            }
            overridePendingTransition(R.anim.slide_from_bottom, android.R.anim.fade_out)
        }

        layout_location.setOnClickListener {
            Intent(applicationContext, LocationActivity::class.java).also {
                it.putExtra(LocationActivity.KEY_LAT, objPost?.latitude?.toDouble())
                it.putExtra(LocationActivity.KEY_LNG, objPost?.longitudes?.toDouble())
                startActivity(it)
            }
        }

        layout_user.setOnClickListener {
            Intent(
                applicationContext,
                ProfileActivity::class.java
            ).let {
                it.putExtra(ProfileActivity.KEY_USER_ID, objPost?.user_id)
                startActivity(it)
            }
        }

    }

    override fun onResume() {
        super.onResume()

        getComments()
    }

    private fun getComments() {
        WebService.getPostComment(objPost?.id!!, object : DataListener<ResponsePostComment> {
            override fun onStartCall() {
//                showProgressBar(this@MediaDetailsActivity)
            }

            override fun onResponse(responseObj: ResponsePostComment?) {
//                hideProgressBar()

                responseObj?.let {
                    if (it.meta.status == 200 && it.response.post.comments.isNotEmpty()) {


                        if (it?.response?.post?.comments != null && it?.response?.post?.comments?.isNotEmpty()) {

                            layout_comment.removeAllViews()

                            val reversedList = it?.response?.post?.comments.reversed()

                            for (i in 0 until 3) {

                                if (reversedList.size > i) {

                                    val viewComment =
                                        LayoutInflater.from(applicationContext)
                                            .inflate(R.layout.item_comment, null, false)

                                    viewComment.tvName.text = reversedList[i].users.name
                                    viewComment.tvComment.text = reversedList[i].comment
                                    viewComment.tv_time.text = reversedList[i].created_at

                                    loadImage(
                                        applicationContext!!,
                                        reversedList[i].users.image_url,
                                        viewComment.ivPhofile
                                    )


                                    layout_comment.addView(viewComment)
                                }

                            }
                        }
                    }
                }
            }

            override fun onError(errorMessage: String) {
//                hideProgressBar()
            }
        })
    }

    private fun initializeAndConnectToSession(sessionId: String, token: String) {

        val mSession = Session.Builder(applicationContext, API_KEY, sessionId).build()
        mSession?.setSessionListener(object : Session.SessionListener {
            var mSubscriber: Subscriber? = null

            override fun onConnected(session: Session) {
                Log.i(LOG_TAG, "Session Connected")
            }

            override fun onDisconnected(session: Session) {
                Log.i(LOG_TAG, "Session Disconnected")
            }

            override fun onStreamReceived(session: Session, stream: Stream) {
                Log.i(LOG_TAG, "Stream Received")

                if (mSubscriber == null) {
                    mSubscriber = Subscriber.Builder(applicationContext, stream).build()
                    mSession.subscribe(mSubscriber)
                    subscriber_container.addView(mSubscriber?.view)
                }
            }

            override fun onStreamDropped(session: Session, stream: Stream) {
                Log.i(LOG_TAG, "Stream Dropped")

                if (mSubscriber != null) {
                    mSubscriber = null
                    subscriber_container.removeAllViews()
                }
            }

            override fun onError(session: Session, opentokError: OpentokError) {
                Log.e(LOG_TAG, "Session error: " + opentokError.message)
            }
        })
        mSession?.connect(token)
    }
}
