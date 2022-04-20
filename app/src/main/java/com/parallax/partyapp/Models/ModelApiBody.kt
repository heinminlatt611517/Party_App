package com.parallax.partyapp.Models

import java.io.File

class SignupBodyModel {

    var name: String? = null
    var email: String? = null
    var password: String? = null
}

class LoginBodyModel {

    var email: String? = null
    var password: String? = null
}

//name:atik update
//gender:male
//phone:123456789
//bio:test
//date_of_birth:05/13/2010

class SetProfileBodyModel {
    var name: String? = null
    var gender: String? = null
    var phone: String? = null
    var bio: String? = null
    var date_of_birth: String? = null
    var image: File? = null

}

//latitude,longitudes

class PostsBody(
    var latitude: String,
    var longitudes: String
)

//email:aa@a.com
//description:အောင်မြင်စွာ မှတ်ပုံတင်

class SetFeedBackBodyModel {
    var email: String? = null
    var description: String? = null
}

//follower_id:9

class FollowUserBodyModel {
    var follower_id: Int? = null
}

//follower_id:8

class UnfollowUserBodyModel {
    var follower_id: Int? = null
}

//type:3
//location:Confidence Mall Hall
//post_text:test title
//latitude:22.820000
//longitudes:89.550003
//feel:Feeling Fantastic
//crowd_count:3
//fun_count:2
//tag_location_address
//tag_location_lat
//tag_location_lng

class CreatePostBodyModel {
    var type: Int? = null
    var location: String? = null
    var post_files = arrayListOf<String>()
    var video_file: String? = null
    var post_text: String? = ""
    var latitude: String? = ""
    var longitudes: String? = ""
    var feel: String? = ""
    var activity: String? = ""
    var tag_friends: String? = ""
    var crowd_count: Double? = null
    var fun_count: Double? = null
}

//comment:Awesome Dj
//user_id:3

class PostCommentBodyModel {
    var comment: String? = null
}


//type:2
//location:Confidence Mall Hall

class PostBodyModel {
    var type: Int? = null
    var location: String? = null
}

//most_recent:0
//following:1
//nearest:0
//liked:1
//comments:0
//crowded:0
//quiet:0
//fun_limit:0
//last_twenty_four:0
//last_forty_eight:1

class PostFilter {
    var most_recent: Int? = null
    var following: Int? = null
    var nearest: Int? = null
    var liked: Int? = null
    var comments: Int? = null
    var crowded: Int? = null
    var quiet: Int? = null
    var fun_limit: Int? = null
    var last_twenty_four: Int? = null
    var last_forty_eight: Int? = null

    //    $request->latitude,$request->longitudes
    var latitude: String? = null
    var longitudes: String? = null
}


class PostForgotPass {
    var email: String? = null
}


//token:9960
//email:atik.parallax@gmail.com
//password:12345678Aaaa

class PostResetPass {
    var token: String? = null
    var email: String? = null
    var password: String? = null
}



