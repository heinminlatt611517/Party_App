package com.parallax.partyapp.views.Adapter

import android.app.Activity
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.gson.Gson
import com.opentok.android.OpentokError
import com.opentok.android.Session
import com.opentok.android.Stream
import com.opentok.android.Subscriber
import com.parallax.partyapp.Api.WebService
import com.parallax.partyapp.Data.SPreferences
import com.parallax.partyapp.Models.*
import com.parallax.partyapp.R
import com.parallax.partyapp.Utils.*
import com.parallax.partyapp.interfaces.DataListener
import com.parallax.partyapp.interfaces.HomeItems
import com.parallax.partyapp.interfaces.OnListItemClickListener
import com.parallax.partyapp.views.AppFlow.*
import kotlinx.android.synthetic.main.home_carousel_view.view.*
import kotlinx.android.synthetic.main.item_home.view.*
import kotlinx.android.synthetic.main.view_ads.view.*
import loadImage
import loadImageHome


class HomeAdapter(
    var context: Activity?,
    var items: List<HomeItems>,
    var onListItemClickListener: OnListItemClickListener<HomeItems>
) : androidx.recyclerview.widget.RecyclerView.Adapter<androidx.recyclerview.widget.RecyclerView.ViewHolder>() {

    companion object {
        val HOME_ITEM_TYPE_ADS = 1
        val HOME_ITEM_TYPE_POSTS = 2

        var addsList = arrayListOf<ResponsePosts.Response.AddLists>()
    }


    private val followingIds = arrayListOf<Int>()
    private val unFollowIds = arrayListOf<Int>()

    private val LOG_TAG = this::class.java.simpleName


    override fun onCreateViewHolder(
        viewGroup: ViewGroup,
        viewType: Int
    ): androidx.recyclerview.widget.RecyclerView.ViewHolder {
        return when (viewType) {
            HOME_ITEM_TYPE_ADS -> AdsViewHolder(
                LayoutInflater.from(viewGroup.context).inflate(
                    R.layout.view_ads,
                    viewGroup,
                    false
                )
            )
            HOME_ITEM_TYPE_POSTS -> PostViewHolder(
                LayoutInflater.from(viewGroup.context).inflate(
                    R.layout.item_home,
                    viewGroup,
                    false
                )
            )
            else -> {
                PostViewHolder(
                    LayoutInflater.from(context).inflate(
                        R.layout.view_ads,
                        null,
                        false
                    )
                )
            }
        }

    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun getItemViewType(position: Int): Int {
        return items[position].getItemType()
    }

    override fun onBindViewHolder(
        holder: androidx.recyclerview.widget.RecyclerView.ViewHolder,
        positon: Int
    ) {

        when (items[positon].getItemType()) {
            HOME_ITEM_TYPE_ADS -> {
                holder as AdsViewHolder

//                val homeAdapter = AdsAdapter(context)
//                holder.recyclerView.layoutManager =
//                    androidx.recyclerview.widget.LinearLayoutManager(
//                        context,
//                        androidx.recyclerview.widget.LinearLayoutManager.HORIZONTAL,
//                        false
//                    )
//                holder.recyclerView.adapter = homeAdapter
//
//                homeAdapter.setItemList(addsList)

                val adapter = SliderAdapterExample(context?.applicationContext!!)

                holder.itemView.carousel_view.setSliderAdapter(adapter)

                addsList.forEach {
                    adapter.addItem(it.ads_banner)
                }


            }

            HOME_ITEM_TYPE_POSTS -> {
                holder as PostViewHolder
                val post = items[positon] as ResponsePosts.Response.Post

                val taggedFriend = StringBuilder()

                for (i in 0 until post.tags?.size!!) {
                    if (i == 0) taggedFriend.append(" - ")

                    if (i < 2) {
                        if (i != 0) taggedFriend.append(", ")
                        taggedFriend.append(post.tags!![i].users.name)
                    } else {
                        if (i == 2) {
                            taggedFriend.append(", See more")

                            holder.tvTitle.setOnClickListener {
                                Intent(context, TaggedFriendListActivity::class.java).apply {
                                    putExtra(TaggedFriendListActivity.KEY_POST, Gson().toJson(post))
                                    context?.startActivity(this)
                                }
                            }
                        }
                    }
                }

                val post_text = if (post.post_text.isNullOrBlank()) {
                    ""
                } else {
                    post.post_text
                }

                holder.layoutInfo.hide()

                holder.tvUserName.text = post.users.name
                holder.tvDate.text = post.created_at.split(" ")[0]
                holder.tvTime.text = post.created_at.split(" ")[1].removeRange(
                    IntRange(
                        post.created_at.split(" ")[1].length - 3,
                        post.created_at.split(" ")[1].length - 1
                    )
                )
                holder.tvTitle.text = post_text + taggedFriend
                holder.tvComment.text =
                    context?.getString(R.string.xx_comments, post.comments_count.toString())
//                holder.tvComment.text = "${post.comments_count} Comments"

                holder.tvLike.text =
                    context?.getString(R.string.xx_likes, post.likes_count.toString())


                loadImage(context?.applicationContext!!, post.users.image_url, holder.ivUserImage)


                if (SPreferences.getUserProfileObj()?.response?.user_data?.id == post.users?.id) {
                    holder.layoutFollow.visibility = View.GONE
                } else {
                    holder.layoutFollow.visibility = View.VISIBLE
                }

                if (post.like_status == 1) {
                    holder.ivLike.setImageResource(R.drawable.heart)
                } else {
                    holder.ivLike.setImageResource(R.drawable.ic_heart_line)
                }

                if (post.follow_status == 1 || followingIds.contains(post.users.id)) {
                    holder.ivFollow.setImageResource(R.drawable.ic_followed)
                } else {
                    holder.ivFollow.setImageResource(R.drawable.ic_not_follow)
                }

                if (unFollowIds.contains(post.users.id) && !followingIds.contains(post.users.id)) {
                    holder.ivFollow.setImageResource(R.drawable.ic_not_follow)
                }


                when (post.type.toInt()) {
                    MEDIA_TYPE_PHOTO -> {

                        when {
                            post.images.size == 1 -> {
                                holder.ivPostImage1.visibility = View.VISIBLE
                                holder.ivPostImage2.visibility = View.GONE
                                holder.layoutPostImage3.visibility = View.GONE
                                holder.subscriberContainer.visibility = View.GONE
                                holder.icPlay.visibility = View.GONE


                                Log.d("imageUrl", post.reference_url + "")
                                loadImageHome(
                                    post.images[0].reference_url,
                                    holder.ivPostImage1,
                                    holder.progressBar
                                )

                            }
                            post.images.size == 2 -> {
                                holder.ivPostImage1.visibility = View.VISIBLE
                                holder.ivPostImage2.visibility = View.VISIBLE
                                holder.layoutPostImage3.visibility = View.GONE
                                holder.subscriberContainer.visibility = View.GONE
                                holder.icPlay.visibility = View.GONE

                                Log.d("imageUrl", post.reference_url + "")
                                loadImageHome(
                                    post.images[0].reference_url,
                                    holder.ivPostImage1,
                                    holder.progressBar
                                )

                                Log.d("imageUrl", post.reference_url + "")
                                loadImageHome(
                                    post.images[1].reference_url,
                                    holder.ivPostImage2,
                                    holder.progressBar
                                )

                            }
                            post.images.size > 2 -> {
                                holder.ivPostImage1.visibility = View.VISIBLE
                                holder.ivPostImage2.visibility = View.VISIBLE
                                holder.layoutPostImage3.visibility = View.VISIBLE
                                holder.subscriberContainer.visibility = View.GONE
                                holder.icPlay.visibility = View.GONE

                                loadImageHome(
                                    post.images[0].reference_url,
                                    holder.ivPostImage1,
                                    holder.progressBar
                                )
                                loadImageHome(
                                    post.images[1].reference_url,
                                    holder.ivPostImage2,
                                    holder.progressBar
                                )
                                loadImageHome(
                                    post.images[2].reference_url,
                                    holder.ivPostImage3,
                                    holder.progressBar
                                )


                                if (post.images.size > 3) {
                                    holder.viewPostImage3.visibility = View.VISIBLE

                                    val restCount = post.images.size - 3
                                    holder.tvPostImage3.text = "+$restCount"
                                }
                            }
                        }


                    }
                    MEDIA_TYPE_VIDEO -> {
                        holder.ivPostImage1.visibility = View.VISIBLE
                        holder.ivPostImage2.visibility = View.GONE
                        holder.layoutPostImage3.visibility = View.GONE
                        holder.subscriberContainer.visibility = View.GONE
                        holder.icPlay.visibility = View.VISIBLE

                        Log.d("imageUrl", post.thumbnail_url + "")
                        loadImageHome(
                            post.thumbnail_url,
                            holder.ivPostImage1,
                            holder.progressBar
                        )


                    }
                    MEDIA_TYPE_LIVE -> {

                        if (post.opentok_s3.isNullOrBlank()) {
                            holder.ivPostImage1.visibility = View.GONE
                            holder.ivPostImage2.visibility = View.GONE
                            holder.layoutPostImage3.visibility = View.GONE
                            holder.subscriberContainer.visibility = View.VISIBLE
                            holder.icPlay.visibility = View.GONE
                            holder.progressBar.visibility = View.GONE

                            if (!post.opentok_session.isNullOrBlank() && !post.opentok_token.isNullOrBlank()) {
                                initializeAndConnectToSession(
                                    holder,
                                    post.opentok_session,
                                    post.opentok_token
                                )
                            }
                        } else {
                            holder.ivPostImage1.visibility = View.VISIBLE
                            holder.ivPostImage2.visibility = View.GONE
                            holder.layoutPostImage3.visibility = View.GONE
                            holder.subscriberContainer.visibility = View.GONE
                            holder.icPlay.visibility = View.VISIBLE

                            Log.d("imageUrl", post.thumbnail_url + "")
                            loadImageHome(
                                post.thumbnail_url,
                                holder.ivPostImage1,
                                holder.progressBar
                            )
                        }

                    }
                }

                holder.tvlike.setOnClickListener {
                    if (post.likes_count > 0) {
                        Intent(context, LikeActivity::class.java).let {
                            it.putExtra(LikeActivity.KEY_POST, Gson().toJson(post))
                            context?.startActivity(it)
                        }
                        context?.overridePendingTransition(
                            R.anim.slide_from_bottom,
                            android.R.anim.fade_out
                        )
                    }
                }

                holder.layoutLikes.setOnClickListener {

                    if (post.like_status == 1) {
                        holder.ivLike.setImageResource(R.drawable.ic_heart_line)
                        post.like_status = 0

                        post.likes_count--

                        holder.tvLike.text =
                            context?.getString(R.string.xx_likes, post.likes_count.toString())
//                        holder.tvLike.text = "${post.likes_count} Likes"

                    } else {
                        holder.ivLike.setImageResource(R.drawable.heart)
                        post.like_status = 1

                        post.likes_count++

                        holder.tvLike.text =
                            context?.getString(R.string.xx_likes, post.likes_count.toString())

                    }

                    callPostLike(post.id)
                }

                holder.layoutFollow.setOnClickListener {
                    //                    context?.startActivity(Intent(context, FollowersActivity::class.java))

                    if (post.follow_status == 1 || followingIds.contains(post.users.id)) {
                        callUnFollowUser(post.users.id)

                        followingIds.remove(post.users.id)
                        unFollowIds.add(post.users.id)

                        holder.ivFollow.setImageResource(R.drawable.ic_not_follow)
                        post.follow_status = 0

                    } else {
                        callFollowUser(post.users.id)

                        followingIds.add(post.users.id)
                        unFollowIds.remove(post.users.id)

                        holder.ivFollow.setImageResource(R.drawable.ic_followed)
                        post.follow_status = 1

                    }

                    notifyDataSetChanged()

                    for (i in followingIds) {
                        Log.d("ids", i.toString())
                    }
                }

                holder.layoutComment.setOnClickListener {
                    Intent(context, CommentActivity::class.java).let {
                        it.putExtra(CommentActivity.KEY_POST_ID, post.id)
                        context?.startActivity(it)
                    }
                    context?.overridePendingTransition(
                        R.anim.slide_from_bottom,
                        android.R.anim.fade_out
                    )

                }


                holder.ivInfo.setOnClickListener {

                    holder.ratingBarCrowed.rating = post?.crowd_count?.toFloat()!!
                    holder.ratingBarFun.rating = post?.fun_count?.toFloat()!!

                    if (post.type.toInt() == MEDIA_TYPE_LIVE) {
                        holder.layoutInfoCroudFun.hide()
                    }


                    holder.tvLocation.text = post?.location
                    if (holder.layoutInfo.visibility == View.VISIBLE) {
                        holder.layoutInfo.hide()
                    } else {
                        holder.layoutInfo.show()
                    }

                }

                holder.ivUserImage.setOnClickListener {
                    Intent(
                        context,
                        ProfileActivity::class.java
                    ).let {
                        it.putExtra(ProfileActivity.KEY_USER_ID, post.user_id)
                        context?.startActivity(it)
                    }
                }

                holder.layoutLocation.setOnClickListener {
                    Intent(context, LocationActivity::class.java).let { intent ->
                        post.latitude.let {
                            intent.putExtra(LocationActivity.KEY_LAT, post.latitude.toDouble())
                            intent.putExtra(LocationActivity.KEY_LNG, post.longitudes.toDouble())
                        }
                        context?.startActivity(intent)
                    }
                }

                if (post.type.toInt() != MEDIA_TYPE_LIVE ||
                    (post.type.toInt() == MEDIA_TYPE_LIVE && !post.opentok_s3.isNullOrBlank())
                ) {
                    holder.layoutMedia.setOnClickListener {
                        onListItemClickListener.onListItemClick(post)
                    }
                }
            }
        }

    }

//    var viewListener = ViewListener { position ->
//        val view = LayoutInflater.from(context).inflate(R.layout.home_carousel_view, null)
//
//        loadImage(context!!, addsList[position].ads_banner, view.iv_banner)
//
//        view.btn_details.setOnClickListener {
//            Intent(context, AdsActivity::class.java).let { intent ->
//                intent.putExtra(AdsActivity.KEY_REWARD_DETAILS, Gson().toJson(addsList[position]))
//                context?.startActivity(intent)
//
//            }
//        }
//
//        return@ViewListener view
//    }

    val sessionIdList = arrayListOf<String>()

    private fun initializeAndConnectToSession(
        holder: PostViewHolder,
        sessionId: String,
        token: String
    ) {

//        if (!sessionIdList.contains(sessionId)) {
//            sessionIdList.add(sessionId)
//        } else {
//            return
//        }

        Log.d(LOG_TAG, sessionId)
        Log.d(LOG_TAG, token)

        val mSession = Session.Builder(context, API_KEY, sessionId).build()
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
                    mSubscriber = Subscriber.Builder(context, stream).build()
                    mSession.subscribe(mSubscriber)
                    holder.subscriberContainer.addView(mSubscriber?.view)
                }
            }

            override fun onStreamDropped(session: Session, stream: Stream) {
                Log.i(LOG_TAG, "Stream Dropped")

                if (mSubscriber != null) {
                    mSubscriber = null
                    holder.subscriberContainer.removeAllViews()
                }
            }

            override fun onError(session: Session, opentokError: OpentokError) {
                Log.e(LOG_TAG, "Session error: " + opentokError.message)
            }
        })
        mSession?.connect(token)
    }


    private fun callFollowUser(userId: Int) {
        val followUserBodyModel = FollowUserBodyModel()
        followUserBodyModel.follower_id = userId
        WebService.callFollowUser(followUserBodyModel, object : DataListener<ResponseFollowUser> {
            override fun onStartCall() {

            }

            override fun onResponse(responseObj: ResponseFollowUser?) {
                responseObj?.let {
                    if (responseObj.meta.status == 200) {
//                        notifyDataSetChanged()
                        Log.d("callFollowUser", "200")
                    } else {
                        Log.d("callFollowUser", "100")
                    }
                }
            }

            override fun onError(errorMessage: String) {

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

                }

                override fun onResponse(responseObj: ResponseUnfollowUser?) {
                    responseObj?.let {
                        if (responseObj.meta.status == 200) {
//                            notifyDataSetChanged()
                            Log.d("callFollowUser", "200")
                        } else {
                            Log.d("callFollowUser", "100")
                        }
                    }
                }

                override fun onError(errorMessage: String) {

                }
            })
    }

    private fun callPostLike(postId: Int) {
        WebService.postLike(postId, object : DataListener<ResponsePostLike> {
            override fun onStartCall() {

            }

            override fun onResponse(responseObj: ResponsePostLike?) {
                responseObj?.let {
                    if (responseObj.meta.status == 200) {
                        Log.d("callPostLike", "200")
                    } else {
                        Log.d("callPostLike", "100")
                    }
                }
            }

            override fun onError(errorMessage: String) {

            }
        })
    }

    class PostViewHolder(view: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(view) {

        var ivUserImage = view.iv_user_image
        var ratingBarCrowed = view.ratingBarCrowed
        var ratingBarFun = view.ratingBarFun
        var tvLocation = view.tv_location
        var layoutInfo = view.layout_info
        var icPlay = view.ic_play
        var tvTitle = view.tv_title
        var tvDate = view.tv_post_date
        var tvTime = view.tv_time
        var tvLike = view.tv_like
        var tvComment = view.tv_comment
        var ivLike = view.iv_like
        var ivFollow = view.iv_follow
        var tvUserName = view.tv_user_name
        var ivPostImage1 = view.iv_post_image1
        var ivPostImage2 = view.iv_post_image2
        var ivPostImage3 = view.iv_post_image3
        var layoutPostImage3 = view.layout_third_image
        var viewPostImage3 = view.viewPostImage3
        var tvPostImage3 = view.tv_post_image3
        var subscriberContainer = view.subscriber_container
        var layoutComment = view.layout_comment
        var layoutLikes = view.layoutLikes
        var progressBar = view.progress_bar
        var tvlike = view.tv_like
        var ivInfo = view.iv_info
        var layoutFollow = view.layout_follow
        var layoutMedia = view.layout_media
        var layoutLocation = view.layout_location
        var layoutInfoCroudFun = view.layout_1


    }

    class AdsViewHolder(view: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(view)

}