package com.parallax.partyapp.Models

data class ResponseCreatePost(
    val meta: Meta,
    val response: Response
) {
    data class Response(
        val message: String,
        val post: Post
    ) {
        data class Post(
            val created_at: String,
            val id: Int,
            val location: String,
            val post_text: String,
            val type: String,
            val updated_at: String,
            val user_id: Int,

            val feel: String,
            val fun_count: String,
            val latitude: String,
            val longitudes: String,
            val opentok_session: String,
            val opentok_token: String,
            val opentok_s3: String
        )
    }

    data class Meta(
        val status: Int
    )
}