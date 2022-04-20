package com.parallax.partyapp.Models

data class ResponseLikes(
    val meta: Meta,
    val response: Response
) {
    data class Response(
        val post: Post
    ) {
        data class Post(
            val activity: Any,
            val comments_count: Int,
            val created_at: String,
            val crowd_count: Any,
            val feel: Any,
            val file_path: String,
            val fun_count: Any,
            val id: Int,
            val latitude: Any,
            val likes: List<Like>,
            val likes_count: Int,
            val location: String,
            val log_message: Any,
            val longitudes: Any,
            val opentok_archive_id: Any,
            val opentok_s3: Any,
            val opentok_session: Any,
            val opentok_token: Any,
            val post_text: Any,
            val reference_url: String,
            val status: Int,
            val thumbnail_url: Any,
            val type: String,
            val updated_at: String,
            val user_id: Int
        ) {
            data class Like(
                val created_at: String,
                val id: Int,
                val post_id: Int,
                val updated_at: String,
                val user_id: Int,
                val follow_status: Int,
                val users: Users
            ) {
                data class Users(
                    val bio: Any,
                    val created_at: String,
                    val date_of_birth: Any,
                    val email: String,
                    val facebook_id: Any,
                    val gender: Any,
                    val id: Int,
                    val image_url: String,
                    val name: String,
                    val owner_name: Any,
                    val phone: Any,
                    val role_type: Int,
                    val status: Int,
                    val token: Any,
                    val updated_at: String
                )
            }
        }
    }

    data class Meta(
        val status: Int
    )
}