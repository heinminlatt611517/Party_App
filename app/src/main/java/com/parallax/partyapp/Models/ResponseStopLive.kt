package com.parallax.partyapp.Models

data class ResponseStopLive(
    val meta: Meta,
    val response: Response
) {
    data class Meta(
        val status: Int
    )

    data class Response(
        val message: String,
        val post: Post
    ) {
        data class Post(
            val created_at: String,
            val crowd_count: Any,
            val feel: Any,
            val file_path: Any,
            val fun_count: Any,
            val id: Int,
            val latitude: Any,
            val location: String,
            val log_message: Any,
            val longitudes: Any,
            val opentok_s3: String,
            val opentok_session: String,
            val opentok_token: String,
            val post_text: String,
            val reference_url: Any,
            val status: Int,
            val thumbnail_url: Any,
            val type: String,
            val updated_at: String,
            val user_id: Int
        )
    }
}