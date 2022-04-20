package com.parallax.partyapp.Models

data class ResponseGetProfile(
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
            val facebook_id: Any,
            val follow_count: Int,
            val following_count: Int,
            val gender: String,
            val id: Int,
            val image_url: String,
            val name: String,
            val owner_name: Any,
            val phone: String,
            val posts: List<Post>,
            val posts_count: Int,
            val role_type: Int,
            val status: Int,
            val token: Any,
            val updated_at: String
        ) {
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
            ) {

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

    data class Meta(
        val status: Int
    )
}