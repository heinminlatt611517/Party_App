package com.parallax.partyapp.Models

data class ResponseSendFeedback(
    val meta: Meta,
    val response: Response
) {
    data class Response(
        val message: String
    )

    data class Meta(
        val status: Int
    )
}

