package com.parallax.partyapp.Models

data class ResponseSetProfile(
    val meta: Meta,
    val response: Response
) {
    data class Response(
        val message: String,
        val user_data: UserData
    ) {
        data class UserData(
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

    data class Meta(
        val status: Int
    )
}