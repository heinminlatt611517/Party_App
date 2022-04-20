package com.parallax.partyapp.Models

import com.parallax.partyapp.interfaces.FollowerFollowingInterface

data class ResponseFollowers(
    val meta: Meta,
    val response: Response
) {
    data class Response(
        val follow_lists: List<FollowLists>,
        val message: String
    ) {
        data class FollowLists(
            val created_at: Any,
            val follower_id: Int,
            val following: Following,
            val id: Int,
            val is_follow: Boolean,
            val updated_at: Any,
            val user_id: Int
        ) : FollowerFollowingInterface {
            data class Following(
                val bio: String,
                val created_at: String,
                val date_of_birth: String,
                val email: String,
                val gender: String,
                val id: Int,
                val image_url: String,
                val name: String,
                val owner_name: Any,
                val phone: String,
                val role_type: Int,
                val status: Int,
                val token: Any,
                val updated_at: String
            )
        }
    }

    data class Meta(
        val status: Int
    )
}
