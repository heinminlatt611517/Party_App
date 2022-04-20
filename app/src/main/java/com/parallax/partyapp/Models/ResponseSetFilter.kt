package com.parallax.partyapp.Models

data class ResponseSetFilter(
    val meta: Meta,
    val response: Response
) {
    data class Response(
        val filter_setting: FilterSetting?,
        val filter_posts: Int?,
        val message: String
    ) {
        data class FilterSetting(
            val comments: Int,
            val created_at: String,
            val crowded: Int,
            val following: Int,
            val fun_limit: Int,
            val id: Int,
            val last_forty_eight: Int,
            val last_twenty_four: Int,
            val liked: Int,
            val most_recent: Int,
            val nearest: Int,
            val quiet: Int,
            val updated_at: String,
            val user_id: Int
        )
    }

    data class Meta(
        val status: Int
    )
}