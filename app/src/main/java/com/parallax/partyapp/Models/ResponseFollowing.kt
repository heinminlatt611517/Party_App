package com.parallax.partyapp.Models

import com.parallax.partyapp.interfaces.FollowerFollowingInterface

data class ResponseFollowing(
    val meta: Meta,
    val response: Response
) {
    data class Response(
        val follow_lists: List<FollowLists>,
        val message: String
    ) {
        data class FollowLists(
            val created_at: String,
            val follow: Follow,
            val follower_id: Int,
            val id: Int,
            val updated_at: String,
            val user_id: Int
        ) : FollowerFollowingInterface {
            data class Follow(
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

    data class Meta(
        val status: Int
    )
}