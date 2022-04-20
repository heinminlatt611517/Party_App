package com.parallax.partyapp.Api

import com.parallax.partyapp.Models.*
import retrofit2.Call
import retrofit2.http.*

interface ApiServices {

    @POST("sign-up")
    fun registerUser(@Body signupBodyModel: SignupBodyModel): Call<ResponseSignIn>

    @POST("sign-in")
    fun loginUser(@Body loginBodyModel: LoginBodyModel): Call<ResponseSignIn>

    @POST("sign-out")
    fun logoutUser(): Call<ResponseLogOut>

    @POST("post")
    fun getPosts(@Body postsBody: PostsBody): Call<ResponsePosts>

    @POST("set-profle")
    fun setProfile(@Body setProfileBodyModel: SetProfileBodyModel): Call<ResponseSetProfile>

    @POST("send-feedback")
    fun sendFeedback(@Body sendFeedBackBodyModel: SetFeedBackBodyModel): Call<ResponseSendFeedback>

    @POST("follow-user")
    fun followUser(@Body followUserBodyModel: FollowUserBodyModel): Call<ResponseFollowUser>

    @POST("unfollow-user")
    fun unfollowUser(@Body unfollowUserBodyModel: UnfollowUserBodyModel): Call<ResponseUnfollowUser>


    @POST("create-post")
    fun createPost(@Body createPostBodyModel: CreatePostBodyModel): Call<ResponseCreatePost>

    @POST("post-comment/{id}")
    fun postComment(@Path("id") postId: Int, @Body postCommentBodyModel: PostCommentBodyModel): Call<ResponsePostComment>

    @POST("post-like/{id}")
    fun postLike(@Path("id") postId: Int): Call<ResponsePostLike>

    @POST("stop-live/{id}")
    fun stopLive(@Path("id") postId: Int): Call<ResponseStopLive>

    @POST("post")
    fun post(@Body postBodyModel: PostBodyModel): Call<ResponseFollowing>

    @POST("set-filter")
    fun setFilter(@Body postFilter: PostFilter): Call<ResponseSetFilter>

    @POST("forgot-password")
    fun forgotPassword(@Body postForgotPass: PostForgotPass): Call<ResponseSetFilter>

    @POST("password-reset")
    fun resetPass(@Body postResetPass: PostResetPass): Call<ResponseSetFilter>


    //==================================================================================================================

    @GET("get-filter")
    fun getFilter(): Call<ResponseSetFilter>

    @GET("get-post-likes/{id}")
    fun getLikes(@Path("id") id: Int): Call<ResponseLikes>

    @GET("get-profle")
    fun getProfile(): Call<ResponseGetProfile>

    @GET("get-post-comment/{id}")
    fun getPostComment(@Path("id") postId: Int): Call<ResponsePostComment>

    @GET("get-user-profle/{id}")
    fun getUserProfile(@Path("id") userId: Int): Call<ResponseGetProfile>

    @GET("contact-us")
    fun getContactUs(): Call<ResponseContactUs>

    @GET("followers/{userId}")
    fun getFollowers(@Path("userId") userId: String): Call<ResponseFollowers>

    @GET("followings/{userId}")
    fun getFollowings(@Path("userId") userId: String): Call<ResponseFollowing>

    @GET("loyality")
    fun getLoyalty(): Call<ResponseLoyalty>

    // todo dami model show has to change medol

    @GET("post/{id}")
    fun getPostById(@Path("id") postId: Int): Call<String> //todo


    //todo dami model
    @DELETE("remove/post/17")
    fun getDelete(): Call<ResponseFollowing>


}