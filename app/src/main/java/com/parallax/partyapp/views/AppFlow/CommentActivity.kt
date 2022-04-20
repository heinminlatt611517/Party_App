package com.parallax.partyapp.views.AppFlow

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import android.widget.Toast
import com.parallax.partyapp.Api.WebService
import com.parallax.partyapp.Models.PostCommentBodyModel
import com.parallax.partyapp.Models.ResponsePostComment
import com.parallax.partyapp.R
import com.parallax.partyapp.Utils.ToastUtils
import com.parallax.partyapp.interfaces.DataListener
import com.parallax.partyapp.views.Adapter.CommentAdapter
import com.parallax.partyapp.views.BaseActivity
import com.vincent.filepicker.ToastUtil
import hideProgressBar
import kotlinx.android.synthetic.main.activity_comment.*
import showProgressBar

class CommentActivity : BaseActivity() {

    companion object {
        val KEY_POST_ID = "KEY_POST_ID"
    }

    private var commentAdapter: CommentAdapter? = null
    var postId: Int? = null

    var commentList = arrayListOf<ResponsePostComment.Response.Post.Comment>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_comment)

        postId = intent?.getIntExtra(KEY_POST_ID, -1)


        commentAdapter = CommentAdapter(this, commentList)
        recyclerView.layoutManager =
            androidx.recyclerview.widget.LinearLayoutManager(
                applicationContext,
                androidx.recyclerview.widget.LinearLayoutManager.VERTICAL,
                false
            )
        recyclerView.adapter = commentAdapter

        getComments()

        btn_send.setOnClickListener {
            when {
                et_comment.text.isNullOrBlank() -> ToastUtils.getInstance(applicationContext)?.showToast(getString(R.string.please_write_a_comment))
                else -> {
                    val postCommentBodyModel = PostCommentBodyModel()
                    postCommentBodyModel.comment = et_comment.text.toString()
                    postComment(postCommentBodyModel)

                    et_comment.setText("")
                }
            }
        }

        btnBack.setOnClickListener {
            onBackPressed()
        }
    }

    private fun getComments() {
        WebService.getPostComment(postId!!, object : DataListener<ResponsePostComment> {
            override fun onStartCall() {
                showProgressBar(this@CommentActivity)
            }

            override fun onResponse(responseObj: ResponsePostComment?) {
                hideProgressBar()

                responseObj?.let {
                    if (it.meta.status == 200 && it.response.post.comments.isNotEmpty()) {
                        commentList.clear()
                        commentList.addAll(it.response.post.comments)

                        commentAdapter?.notifyDataSetChanged()
                    }
                }
            }

            override fun onError(errorMessage: String) {
                hideProgressBar()
            }
        })
    }

    private fun postComment(postCommentBodyModel: PostCommentBodyModel) {
        WebService.postComment(
            postId!!,
            postCommentBodyModel,
            object : DataListener<ResponsePostComment> {
                override fun onStartCall() {
                    showProgressBar(this@CommentActivity)
                }

                override fun onResponse(responseObj: ResponsePostComment?) {
                    hideProgressBar()

                    responseObj?.let {
                        if (it.meta.status == 200 && it.response.post.comments.isNotEmpty()) {
                            commentList.clear()
                            commentList.addAll(it.response.post.comments)

                            commentAdapter?.notifyDataSetChanged()
                        }
                    }
                }

                override fun onError(errorMessage: String) {
                    hideProgressBar()
                }
            })
    }
}
