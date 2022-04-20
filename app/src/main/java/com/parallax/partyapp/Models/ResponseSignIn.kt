package com.parallax.partyapp.Models


data class ResponseSignIn(
    val meta: Meta,
    val response: Response
) {
    data class Meta(
        val status: Int
    )

    data class Response(
        val token: String,
        val user: User,
        val message: String
    ) {

        data class User(
            val email: String,
            val id: Int
        )
    }
}