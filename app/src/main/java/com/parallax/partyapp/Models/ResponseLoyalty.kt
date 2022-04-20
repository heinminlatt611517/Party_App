package com.parallax.partyapp.Models

data class ResponseLoyalty(
    val meta: Meta,
    val response: Response
) {
    data class Meta(
        val status: Int
    )

    data class Response(
        val add_lists: List<AddLists>,
        val loyality_lists: List<LoyalityLists>
    ) {
        data class LoyalityLists(
            val ads_banner: String,
            val created_at: String,
            val description: String,
            val end_time: String,
            val id: Int,
            val location: String,
            val merchant: Merchant,
            val merchant_id: Int,
            val start_time: String,
            val status: String,
            val title: String,
            val updated_at: String
        ) {
            data class Merchant(
                val bio: Any,
                val created_at: String,
                val date_of_birth: Any,
                val email: String,
                val gender: Any,
                val id: Int,
                val image_url: String,
                val name: String,
                val owner_name: String,
                val phone: String,
                val role_type: Int,
                val status: Int,
                val token: Any,
                val updated_at: String
            )
        }

        data class AddLists(
            val ads_banner: String,
            val created_at: String,
            val description: String,
            val end_time: String,
            val id: Int,
            val location: String,
            val merchant: Merchant,
            val merchant_id: Int,
            val start_time: String,
            val status: String,
            val title: String,
            val updated_at: String
        ) {
            data class Merchant(
                val bio: Any,
                val created_at: String,
                val date_of_birth: Any,
                val email: String,
                val gender: Any,
                val id: Int,
                val image_url: String,
                val name: String,
                val owner_name: String,
                val phone: String,
                val role_type: Int,
                val status: Int,
                val token: Any,
                val updated_at: String
            )
        }
    }
}