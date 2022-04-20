package com.parallax.partyapp.Models

data class ResponsePostComment(
    val meta: Meta,
    val response: Response
) {
    data class Response(
        val message: String,
        val post: Post
    ) {
        data class Post(
            val comments: List<Comment>,
            val comments_count: Int,
            val created_at: String,
            val crowd_count: Any,
            val feel: Any,
            val file_path: String,
            val fun_count: Any,
            val id: Int,
            val latitude: Any,
            val likes_count: Int,
            val location: String,
            val log_message: Any,
            val longitudes: Any,
            val post_text: Any,
            val reference_url: String,
            val status: Int,
            val type: String,
            val updated_at: String,
            val user_id: Int
        ) {
            data class Comment(
                val comment: String,
                val created_at: String,
                val id: Int,
                val post_id: Int,
                val updated_at: String,
                val user_id: Int,
                val users: Users
            ) {
                data class Users(
                    val bio: Any,
                    val created_at: String,
                    val date_of_birth: Any,
                    val email: String,
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