package com.parallax.partyapp.Api

import android.util.Log
import com.google.gson.Gson
import com.parallax.partyapp.Models.*
import com.parallax.partyapp.Utils.apiServices
import com.parallax.partyapp.interfaces.DataListener
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object WebService {

    const val ON_FAILURE_MESSAGE = "Please Check your Internet Connection"

    fun callRegistrationAPI(
        signupBodyModel: SignupBodyModel,
        callBack: (ResponseSignIn?, String?) -> Unit
    ) {

//        dataListener.onStartCall()

        apiServices.registerUser(signupBodyModel).enqueue(object : Callback<ResponseSignIn> {
            override fun onFailure(call: Call<ResponseSignIn>, t: Throwable) {
                Log.d("registrationResponse", t.localizedMessage)
//                dataListener.onError(ON_FAILURE_MESSAGE)
                callBack(null, ON_FAILURE_MESSAGE)
            }

            override fun onResponse(
                call: Call<ResponseSignIn>,
                response: Response<ResponseSignIn>
            ) {
                Log.d("registrationResponse", Gson().toJson(response.body()))
                Log.d("registrationResponse", response.code().toString())

                if (response.isSuccessful) {
//                    dataListener.onResponse(response.body())
                    callBack(response.body(), null)
                } else {
//                    dataListener.onError("Connection Timeout ${response.code()}")
                    callBack(null, ON_FAILURE_MESSAGE)
                }

            }

        })
    }

    fun callLoginAPI(loginBodyModel: LoginBodyModel, callBack: (ResponseSignIn?, String?) -> Unit) {

//        dataListener.onStartCall()

        apiServices.loginUser(loginBodyModel).enqueue(object : Callback<ResponseSignIn> {
            override fun onFailure(call: Call<ResponseSignIn>, t: Throwable) {
                Log.d("loginResponse", t.localizedMessage)
//                dataListener.onError(ON_FAILURE_MESSAGE)
                callBack(null, ON_FAILURE_MESSAGE)
            }

            override fun onResponse(
                call: Call<ResponseSignIn>,
                response: Response<ResponseSignIn>
            ) {
                Log.d("loginResponse", Gson().toJson(response.body()))
                Log.d("loginResponse", response.code().toString())

                if (response.isSuccessful) {
//                    dataListener.onResponse(response.body())
                    callBack(response.body(), null)
                } else {
//                    dataListener.onError("Connection Timeout ${response.code()}")
                    callBack(null, "Connection Timeout ${response.code()}")
                    Log.d("loginResponse11", response.code().toString())

                }
            }

        })
    }

    fun callLogoutAPI(dataListener: DataListener<ResponseLogOut>) {

        dataListener.onStartCall()

        apiServices.logoutUser().enqueue(object : Callback<ResponseLogOut> {
            override fun onFailure(call: Call<ResponseLogOut>, t: Throwable) {
                Log.d("logoutResponse", t.localizedMessage)
                dataListener.onError(ON_FAILURE_MESSAGE)
            }

            override fun onResponse(
                call: Call<ResponseLogOut>,
                response: Response<ResponseLogOut>
            ) {
                Log.d("logoutResponse", Gson().toJson(response.body()))
                Log.d("logoutResponse", response.code().toString())

                if (response.isSuccessful) {
                    dataListener.onResponse(response.body())
                } else {
                    dataListener.onError("Connection Timeout ${response.code()}")
                }
            }

        })
    }

    fun callGetProfile(dataListener: DataListener<ResponseGetProfile>) {

        dataListener.onStartCall()

        apiServices.getProfile().enqueue(object : Callback<ResponseGetProfile> {
            override fun onFailure(call: Call<ResponseGetProfile>, t: Throwable) {
                Log.d("callGetProfile", t.localizedMessage)
                dataListener.onError(ON_FAILURE_MESSAGE)
            }

            override fun onResponse(
                call: Call<ResponseGetProfile>,
                response: Response<ResponseGetProfile>
            ) {
                Log.d("callGetProfile", Gson().toJson(response.body()))
                Log.d("callGetProfile", response.code().toString())

                if (response.isSuccessful) {
                    dataListener.onResponse(response.body())
                } else {
                    dataListener.onError("Connection Timeout ${response.code()}")
                }
            }

        })
    }

    fun callSetProfile(
        setProfilebodyModel: SetProfileBodyModel,
        dataListener: DataListener<ResponseSetProfile>
    ) {

        dataListener.onStartCall()

        apiServices.setProfile(setProfilebodyModel).enqueue(object : Callback<ResponseSetProfile> {
            override fun onFailure(call: Call<ResponseSetProfile>, t: Throwable) {
                Log.d("callSetProfile", t.localizedMessage)
                dataListener.onError(ON_FAILURE_MESSAGE)
            }

            override fun onResponse(
                call: Call<ResponseSetProfile>,
                response: Response<ResponseSetProfile>
            ) {
                Log.d("callSetProfile", Gson().toJson(response.body()))
                Log.d("callSetProfile", response.code().toString())

                if (response.isSuccessful) {
                    dataListener.onResponse(response.body())
                } else {
                    dataListener.onError("Connection Timeout ${response.code()}")
                }
            }

        })
    }

    fun callSendFeedBack(
        sendFeedBackBodyModel: SetFeedBackBodyModel,
        dataListener: DataListener<ResponseSendFeedback>
    ) {

        dataListener.onStartCall()

        apiServices.sendFeedback(sendFeedBackBodyModel)
            .enqueue(object : Callback<ResponseSendFeedback> {
                override fun onFailure(call: Call<ResponseSendFeedback>, t: Throwable) {
                    Log.d("callSendFeedBack", t.localizedMessage)
                    dataListener.onError(ON_FAILURE_MESSAGE)

                }

                override fun onResponse(
                    call: Call<ResponseSendFeedback>,
                    response: Response<ResponseSendFeedback>
                ) {
                    Log.d("callSendFeedBack", Gson().toJson(response.body()))
                    Log.d("callSendFeedBack", response.code().toString())

                    if (response.isSuccessful) {
                        dataListener.onResponse(response.body())
                    } else {
                        dataListener.onError("Connection Timeout ${response.code()}")
                    }
                }

            })
    }


    fun callCreatePost(
        createPostBodyModel: CreatePostBodyModel,
        dataListener: DataListener<ResponseCreatePost>
    ) {

        dataListener.onStartCall()

        apiServices.createPost(createPostBodyModel).enqueue(object : Callback<ResponseCreatePost> {
            override fun onFailure(call: Call<ResponseCreatePost>, t: Throwable) {
                Log.d("callCreatePost", t.localizedMessage)
                dataListener.onError(ON_FAILURE_MESSAGE)

            }

            override fun onResponse(
                call: Call<ResponseCreatePost>,
                response: Response<ResponseCreatePost>
            ) {
                Log.d("callCreatePost", Gson().toJson(response.body()))
                Log.d("callCreatePost", response.code().toString())

                if (response.isSuccessful) {
                    dataListener.onResponse(response.body())
                } else {
                    dataListener.onError("Connection Timeout ${response.code()}")
                }
            }

        })
    }


    fun postComment(
        postId: Int,
        postCommentBodyModel: PostCommentBodyModel,
        dataListener: DataListener<ResponsePostComment>
    ) {

        dataListener.onStartCall()

        apiServices.postComment(postId, postCommentBodyModel)
            .enqueue(object : Callback<ResponsePostComment> {
                override fun onFailure(call: Call<ResponsePostComment>, t: Throwable) {
                    Log.d("postComment", t.localizedMessage)
                    dataListener.onError(ON_FAILURE_MESSAGE)
                }

                override fun onResponse(
                    call: Call<ResponsePostComment>,
                    response: Response<ResponsePostComment>
                ) {
                    Log.d("postComment", Gson().toJson(response.body()))
                    Log.d("postComment", response.code().toString())

                    if (response.isSuccessful) {
                        dataListener.onResponse(response.body())
                    } else {
                        dataListener.onError("Connection Timeout ${response.code()}")
                    }
                }

            })
    }

    fun getPostComment(
        postId: Int,
        dataListener: DataListener<ResponsePostComment>
    ) {

        dataListener.onStartCall()

        apiServices.getPostComment(postId).enqueue(object : Callback<ResponsePostComment> {
            override fun onFailure(call: Call<ResponsePostComment>, t: Throwable) {
                Log.d("getPostComment", t.localizedMessage)
                dataListener.onError(ON_FAILURE_MESSAGE)
            }

            override fun onResponse(
                call: Call<ResponsePostComment>,
                response: Response<ResponsePostComment>
            ) {
                Log.d("getPostComment", Gson().toJson(response.body()))
                Log.d("getPostComment", response.code().toString())

                if (response.isSuccessful) {
                    dataListener.onResponse(response.body())
                } else {
                    dataListener.onError("Connection Timeout ${response.code()}")
                }
            }

        })
    }


    fun getLikes(
        postId: Int,
        dataListener: DataListener<ResponseLikes>
    ) {

        dataListener.onStartCall()

        apiServices.getLikes(postId).enqueue(object : Callback<ResponseLikes> {
            override fun onFailure(call: Call<ResponseLikes>, t: Throwable) {
                Log.d("getLikes", t.localizedMessage)
                dataListener.onError(ON_FAILURE_MESSAGE)
            }

            override fun onResponse(call: Call<ResponseLikes>, response: Response<ResponseLikes>) {
                Log.d("getLikes", Gson().toJson(response.body()))
                Log.d("getLikes", response.code().toString())

                if (response.isSuccessful) {
                    dataListener.onResponse(response.body())
                } else {
                    dataListener.onError("Connection Timeout ${response.code()}")
                }
            }

        })
    }


    fun postLike(postId: Int, dataListener: DataListener<ResponsePostLike>) {

        dataListener.onStartCall()

        apiServices.postLike(postId).enqueue(object : Callback<ResponsePostLike> {
            override fun onFailure(call: Call<ResponsePostLike>, t: Throwable) {
                Log.d("postLike", t.localizedMessage)
                dataListener.onError(ON_FAILURE_MESSAGE)

            }

            override fun onResponse(
                call: Call<ResponsePostLike>,
                response: Response<ResponsePostLike>
            ) {
                Log.d("postLike", Gson().toJson(response.body()))
                Log.d("postLike", response.code().toString())

                if (response.isSuccessful) {
                    dataListener.onResponse(response.body())
                } else {
                    dataListener.onError("Connection Timeout ${response.code()}")
                }
            }

        })
    }

    //todo dami model show

    fun post(postBodyModel: PostBodyModel, dataListener: DataListener<ResponseFollowing>) {

        dataListener.onStartCall()

        apiServices.post(postBodyModel).enqueue(object : Callback<ResponseFollowing> {
            override fun onFailure(call: Call<ResponseFollowing>, t: Throwable) {
                Log.d("post", t.localizedMessage)
                dataListener.onError(ON_FAILURE_MESSAGE)

            }

            override fun onResponse(
                call: Call<ResponseFollowing>,
                response: Response<ResponseFollowing>
            ) {
                Log.d("post", Gson().toJson(response.body()))
                Log.d("post", response.code().toString())

                if (response.isSuccessful) {
                    dataListener.onResponse(response.body())
                } else {
                    dataListener.onError("Connection Timeout ${response.code()}")
                }
            }

        })
    }

    fun setFilter(postFilter: PostFilter, dataListener: DataListener<ResponseSetFilter>) {

        dataListener.onStartCall()

        apiServices.setFilter(postFilter).enqueue(object : Callback<ResponseSetFilter> {
            override fun onFailure(call: Call<ResponseSetFilter>, t: Throwable) {
                Log.d("setFilter", t.localizedMessage)
                dataListener.onError(ON_FAILURE_MESSAGE)

            }

            override fun onResponse(
                call: Call<ResponseSetFilter>,
                response: Response<ResponseSetFilter>
            ) {
                Log.d("setFilter", Gson().toJson(response.body()))
                Log.d("setFilter", response.code().toString())

                if (response.isSuccessful) {
                    dataListener.onResponse(response.body())
                } else {
                    dataListener.onError("Connection Timeout ${response.code()}")
                }
            }

        })
    }

    fun getFilter(dataListener: DataListener<ResponseSetFilter>) {

        dataListener.onStartCall()

        apiServices.getFilter().enqueue(object : Callback<ResponseSetFilter> {
            override fun onFailure(call: Call<ResponseSetFilter>, t: Throwable) {
                Log.d("getFilter", t.localizedMessage)
                dataListener.onError(ON_FAILURE_MESSAGE)

            }

            override fun onResponse(
                call: Call<ResponseSetFilter>,
                response: Response<ResponseSetFilter>
            ) {
                Log.d("getFilter", Gson().toJson(response.body()))
                Log.d("getFilter", response.code().toString())

                if (response.isSuccessful) {
                    dataListener.onResponse(response.body())
                } else {
                    dataListener.onError("Connection Timeout ${response.code()}")
                }
            }

        })
    }

    fun callFollowUser(
        followUserBodyModel: FollowUserBodyModel,
        dataListener: DataListener<ResponseFollowUser>
    ) {

        dataListener.onStartCall()

        apiServices.followUser(followUserBodyModel).enqueue(object : Callback<ResponseFollowUser> {
            override fun onFailure(call: Call<ResponseFollowUser>, t: Throwable) {
                Log.d("callFollowUser", t.localizedMessage)
                dataListener.onError(ON_FAILURE_MESSAGE)
            }

            override fun onResponse(
                call: Call<ResponseFollowUser>,
                response: Response<ResponseFollowUser>
            ) {
                Log.d("callFollowUser", Gson().toJson(response.body()))
                Log.d("callFollowUser", response.code().toString())

                if (response.isSuccessful) {
                    dataListener.onResponse(response.body())
                } else {
                    dataListener.onError("Connection Timeout ${response.code()}")
                }
            }

        })
    }

    fun callUnfollowUser(
        unfollowUserBodyModel: UnfollowUserBodyModel,
        dataListener: DataListener<ResponseUnfollowUser>
    ) {

        dataListener.onStartCall()

        apiServices.unfollowUser(unfollowUserBodyModel)
            .enqueue(object : Callback<ResponseUnfollowUser> {
                override fun onFailure(call: Call<ResponseUnfollowUser>, t: Throwable) {
                    Log.d("callUnfollowUser", t.localizedMessage)
                    dataListener.onError(ON_FAILURE_MESSAGE)
                }

                override fun onResponse(
                    call: Call<ResponseUnfollowUser>,
                    response: Response<ResponseUnfollowUser>
                ) {
                    Log.d("callUnfollowUser", Gson().toJson(response.body()))
                    Log.d("callUnfollowUser", response.code().toString())

                    if (response.isSuccessful) {
                        dataListener.onResponse(response.body())
                    } else {
                        dataListener.onError("Connection Timeout ${response.code()}")
                    }
                }

            })
    }


    fun getPosts(postsBody: PostsBody, dataListener: DataListener<ResponsePosts>) {

        dataListener.onStartCall()

        apiServices.getPosts(postsBody).enqueue(object : Callback<ResponsePosts> {
            override fun onFailure(call: Call<ResponsePosts>, t: Throwable) {
                Log.d("getPosts", t.localizedMessage)
                dataListener.onError(ON_FAILURE_MESSAGE)
            }

            override fun onResponse(call: Call<ResponsePosts>, response: Response<ResponsePosts>) {
                Log.d("getPosts", Gson().toJson(response.body()))
                Log.d("getPosts", response.code().toString())

                if (response.isSuccessful) {
                    dataListener.onResponse(response.body())
                } else {
                    dataListener.onError("Connection Timeout ${response.code()}")
                }
            }

        })
    }


    fun forgotPassword(
        postForgotPass: PostForgotPass,
        dataListener: DataListener<ResponseSetFilter>
    ) {

        dataListener.onStartCall()

        apiServices.forgotPassword(postForgotPass).enqueue(object : Callback<ResponseSetFilter> {
            override fun onFailure(call: Call<ResponseSetFilter>, t: Throwable) {
                Log.d("forgotPassword", t.localizedMessage)
                dataListener.onError(ON_FAILURE_MESSAGE)
            }

            override fun onResponse(
                call: Call<ResponseSetFilter>,
                response: Response<ResponseSetFilter>
            ) {
                Log.d("forgotPassword", Gson().toJson(response.body()))
                Log.d("forgotPassword", response.code().toString())

                if (response.isSuccessful) {
                    dataListener.onResponse(response.body())
                } else {
                    dataListener.onError("Connection Timeout ${response.code()}")
                }
            }

        })

    }

    fun resetPass(postResetPass: PostResetPass, dataListener: DataListener<ResponseSetFilter>) {

        dataListener.onStartCall()

        apiServices.resetPass(postResetPass).enqueue(object : Callback<ResponseSetFilter> {
            override fun onFailure(call: Call<ResponseSetFilter>, t: Throwable) {
                Log.d("resetPass", t.localizedMessage)
                dataListener.onError(ON_FAILURE_MESSAGE)
            }

            override fun onResponse(
                call: Call<ResponseSetFilter>,
                response: Response<ResponseSetFilter>
            ) {
                Log.d("resetPass", Gson().toJson(response.body()))
                Log.d("resetPass", response.code().toString())

                if (response.isSuccessful) {
                    dataListener.onResponse(response.body())
                } else {
                    dataListener.onError("Connection Timeout ${response.code()}")
                }
            }

        })
    }

    fun stopLive(postId: Int, dataListener: DataListener<ResponseStopLive>) {

        dataListener.onStartCall()

        apiServices.stopLive(postId).enqueue(object : Callback<ResponseStopLive> {
            override fun onFailure(call: Call<ResponseStopLive>, t: Throwable) {
                Log.d("stopLive", t.localizedMessage)
                dataListener.onError(ON_FAILURE_MESSAGE)
            }

            override fun onResponse(
                call: Call<ResponseStopLive>,
                response: Response<ResponseStopLive>
            ) {
                Log.d("stopLive", Gson().toJson(response.body()))
                Log.d("stopLive", response.code().toString())

                if (response.isSuccessful) {
                    dataListener.onResponse(response.body())
                } else {
                    dataListener.onError("Connection Timeout ${response.code()}")
                }
            }

        })
    }

    fun getContactUs(dataListener: DataListener<ResponseContactUs>) {

        dataListener.onStartCall()

        apiServices.getContactUs().enqueue(object : Callback<ResponseContactUs> {
            override fun onFailure(call: Call<ResponseContactUs>, t: Throwable) {
                Log.d("getContactUs", t.localizedMessage)
                dataListener.onError(ON_FAILURE_MESSAGE)
            }

            override fun onResponse(
                call: Call<ResponseContactUs>,
                response: Response<ResponseContactUs>
            ) {
                Log.d("getContactUs", Gson().toJson(response.body()))
                Log.d("getContactUs", response.code().toString())

                if (response.isSuccessful) {
                    dataListener.onResponse(response.body())
                } else {
                    dataListener.onError("Connection Timeout ${response.code()}")
                }
            }

        })
    }

    fun getFollowers(userId: String, dataListener: DataListener<ResponseFollowers>) {

        dataListener.onStartCall()

        apiServices.getFollowers(userId).enqueue(object : Callback<ResponseFollowers> {
            override fun onFailure(call: Call<ResponseFollowers>, t: Throwable) {
                Log.d("getFollowers", t.localizedMessage)
                dataListener.onError(ON_FAILURE_MESSAGE)
            }

            override fun onResponse(
                call: Call<ResponseFollowers>,
                response: Response<ResponseFollowers>
            ) {
                Log.d("getFollowers", Gson().toJson(response.body()))
                Log.d("getFollowers", response.code().toString())

                if (response.isSuccessful) {
                    dataListener.onResponse(response.body())
                } else {
                    dataListener.onError("Connection Timeout ${response.code()}")
                }
            }

        })
    }

    fun getFollowing(userId: String, dataListener: DataListener<ResponseFollowing>) {

        dataListener.onStartCall()

        apiServices.getFollowings(userId).enqueue(object : Callback<ResponseFollowing> {
            override fun onFailure(call: Call<ResponseFollowing>, t: Throwable) {
                Log.d("getFollowing", t.localizedMessage)
                dataListener.onError(ON_FAILURE_MESSAGE)
            }

            override fun onResponse(
                call: Call<ResponseFollowing>,
                response: Response<ResponseFollowing>
            ) {
                Log.d("getFollowing", Gson().toJson(response.body()))
                Log.d("getFollowing", response.code().toString())

                if (response.isSuccessful) {
                    dataListener.onResponse(response.body())
                } else {
                    dataListener.onError("Connection Timeout ${response.code()}")
                }
            }

        })
    }


    fun getLoyalty(dataListener: DataListener<ResponseLoyalty>) {

        dataListener.onStartCall()

        apiServices.getLoyalty().enqueue(object : Callback<ResponseLoyalty> {
            override fun onFailure(call: Call<ResponseLoyalty>, t: Throwable) {
                Log.d("getLoyalty", t.localizedMessage)
                dataListener.onError(ON_FAILURE_MESSAGE)
            }

            override fun onResponse(
                call: Call<ResponseLoyalty>,
                response: Response<ResponseLoyalty>
            ) {
                Log.d("getLoyalty", Gson().toJson(response.body()))
                Log.d("getLoyalty", response.code().toString())

                if (response.isSuccessful) {
                    dataListener.onResponse(response.body())
                } else {
                    dataListener.onError("Connection Timeout ${response.code()}")
                }
            }

        })
    }

    fun getUserProfile(userId: Int, dataListener: DataListener<ResponseGetProfile>) {

        dataListener.onStartCall()

        apiServices.getUserProfile(userId).enqueue(object : Callback<ResponseGetProfile> {
            override fun onFailure(call: Call<ResponseGetProfile>, t: Throwable) {
                Log.d("getUserProfile", t.localizedMessage)
                dataListener.onError(ON_FAILURE_MESSAGE)
            }

            override fun onResponse(
                call: Call<ResponseGetProfile>,
                response: Response<ResponseGetProfile>
            ) {
                Log.d("getUserProfile", Gson().toJson(response.body()))
                Log.d("getUserProfile", response.code().toString())

                if (response.isSuccessful) {
                    dataListener.onResponse(response.body())
                } else {
                    dataListener.onError("Connection Timeout ${response.code()}")
                }
            }

        })
    }

    //todo dami medol

    fun getDelete(dataListener: DataListener<ResponseFollowing>) {

        dataListener.onStartCall()

        apiServices.getDelete().enqueue(object : Callback<ResponseFollowing> {
            override fun onFailure(call: Call<ResponseFollowing>, t: Throwable) {
                Log.d("getDelete", t.localizedMessage)
                dataListener.onError(ON_FAILURE_MESSAGE)
            }

            override fun onResponse(
                call: Call<ResponseFollowing>,
                response: Response<ResponseFollowing>
            ) {
                Log.d("getDelete", Gson().toJson(response.body()))
                Log.d("getDelete", response.code().toString())

                if (response.isSuccessful) {
                    dataListener.onResponse(response.body())
                } else {
                    dataListener.onError("Connection Timeout ${response.code()}")
                }
            }

        })
    }


    fun fbLogin(bodyFbLogin: BodyFbLogin, dataListener: DataListener<ResponseFbLogin>) {

        dataListener.onStartCall()

        fbLoginAPi.checkToken(bodyFbLogin).enqueue(object : Callback<ResponseFbLogin> {
            override fun onFailure(call: Call<ResponseFbLogin>, t: Throwable) {
                Log.d("checkFbLogIn", t.localizedMessage)
                dataListener.onError(ON_FAILURE_MESSAGE)
            }

            override fun onResponse(
                call: Call<ResponseFbLogin>,
                response: Response<ResponseFbLogin>
            ) {
                Log.d("checkFbLogIn", Gson().toJson(response.body()))
                Log.d("checkFbLogIn", response.code().toString())

                if (response.isSuccessful) dataListener.onResponse(response.body())
                else dataListener.onError("Connection timeout ${response.code()}")
            }
        })
    }
}