package com.parallax.partyapp.Models

import com.parallax.partyapp.interfaces.HomeItems
import com.parallax.partyapp.views.Adapter.HomeAdapter

data class ResponsePosts(
    val meta: Meta,
    val response: Response
) {
    data class Meta(
        val status: Int
    )

    data class Response(
        val set_filter: Boolean,
        val customer_type: String,
        val current_om_points: Int,
        val next_tier: String,
        val points_to_next_tier: Int,
        val add_lists: ArrayList<AddLists>,
        val game_promo_data: ArrayList<GamePromoData>,
        val tier_details: ArrayList<TireDetails>,
        val promo_data: ArrayList<PromoData>,
        val post: ArrayList<Post>
    ) {

        data class TireDetails(
            val created_at: String?,
            val earningpoints: Earningpoints?,
            val id: Int?,
            val point_from: Int?,
            val point_to: String?,
            val type_name: String?,
            val updated_at: String?
        ) {
            data class Earningpoints(
                val created_at: String?,
                val customer_type_id: Int?,
                val give_like_comment: Int?,
                val id: Int?,
                val post: Int?,
                val receive_like_comment: Int?,
                val updated_at: String?
            )
        }

        data class PromoData(
            val category_id: Any?,
            val created_at: String?,
            val id: Int?,
            val image: String?,
            val limit: Int?,
            val points: String?,
            val social_username: String?,
            val phone: String?,
            val promo_address_text_eng: String?,
            val promo_address_text_myn: String?,
            val promo_code: Any?,
            val promo_confirmation_msg_eng: String?,
            val promo_confirmation_msg_myn: String?,
            val promo_description: String?,
            val promo_description_burmese: String?,
            val promo_image_logo: String?,
            val promo_image_title: String?,
            val promo_image_title_burmese: String?,
            val promo_long_discount_text_eng: String?,
            val promo_long_discount_text_myn: String?,
            val promo_name: String?,
            val promo_name_burmese: String?,
            val latitude : String?,
            val longitude: String?,
            val promo_secret: Any?,
            val redem_duration: Any?,
            val status: Int?,
            val type_id: Int?,
            val updated_at: String?
        )

        data class GamePromoData(
            val category_id: Any?,
            val created_at: String?,
            val id: Int?,
            val image: String?,
            val limit: Int?,
            val points: String?,
            val promo_address_text_eng: String?,
            val promo_address_text_myn: String?,
            val promo_code: Any?,
            val promo_confirmation_msg_eng: String?,
            val promo_confirmation_msg_myn: String?,
            val promo_description: String?,
            val promo_description_burmese: String?,
            val promo_image_logo: String?,
            val promo_image_title: String?,
            val promo_image_title_burmese: String?,
            val promo_long_discount_text_eng: String?,
            val promo_long_discount_text_myn: String?,
            val promo_name: String?,
            val promo_name_burmese: String?,
            val promo_secret: Any?,
            val redem_duration: Any?,
            val status: Int?,
            val type_id: Int?,
            val updated_at: String?
        )

        data class AddLists(
            val ads_banner: String,
            val created_at: String,
            val description: String,
            val end_time: String,
            val id: Int,
            val location: String,
            val latitude : String,
            val longitude: String,
            val social_username: String,
            val phone: String,
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
                val facebook_id: Any,
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

        data class Post(
            val activity: Any,
            val comments: List<Comment>,
            val comments_count: Int,
            val created_at: String,
            val crowd_count: Double,
            val feel: Any,
            val file_path: Any,
            var follow_status: Int,
            val fun_count: Double,
            val id: Int,
            val latitude: String,
            val ShareUrl: String,
            var like_status: Int,
            val likes: List<Like>,
            var likes_count: Int,
            val location: String,
            val log_message: Any,
            val longitudes: String,
            val opentok_archive_id: Any,
            val opentok_s3: String,
            val opentok_session: String,
            val opentok_token: String,
            val post_text: String,
            val reference_url: String,
            val status: Any,
            val tags: List<Tag>,
            val thumbnail_url: String,
            val type: String,
            val updated_at: String,
            val user_id: Int,
            val users: Users,
            val images: ArrayList<Image>
        ) : HomeItems {
            override fun getItemType(): Int {
                return HomeAdapter.HOME_ITEM_TYPE_POSTS
            }

            class Image {
                var created_at: String? = null
                var file_path: String? = null
                var id: Int? = null
                var post_id: Int? = null
                var reference_url: String? = null
                var thumbnail_url: Any? = null
                var type: String? = null
                var updated_at: String? = null
            }

            data class Users(
                val bio: String,
                val created_at: String,
                val date_of_birth: String,
                val email: String,
                val facebook_id: Any,
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

            data class Like(
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

            data class Tag(
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
                    val facebook_id: Any,
                    val gender: Any,
                    val id: Int,
                    val image_url: String,
                    val name: String,
                    val owner_name: Any,
                    val phone: Any,
                    val role_type: Int,
                    val status: Int,
                    val token: String,
                    val updated_at: String
                )
            }

        }
    }
}


