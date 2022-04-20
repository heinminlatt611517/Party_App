package com.parallax.partyapp.Models

data class ResponseContactUs(
    val meta: Meta,
    val response: Response
) {
    data class Meta(
        val status: Int
    )

    data class Response(
        val message: String
    )
}