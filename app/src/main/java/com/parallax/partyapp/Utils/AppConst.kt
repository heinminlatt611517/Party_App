package com.parallax.partyapp.Utils

import com.parallax.partyapp.R
import java.util.ArrayList


//const val BASE_URL = "http://100.20.205.168/partyapp_dev"
const val BASE_URL = "http://100.20.205.168/party_app"

const val API_BASE_URL = "$BASE_URL/api/v2/"
const val API_BASE_URL_IMAGE = "$API_BASE_URL/assets/no-image.jpg"
const val BASE_URL_FB = BASE_URL


const val API_KEY = "46254272"

//1=photo;2=video;3=live

const val MEDIA_TYPE_PHOTO = 1
const val MEDIA_TYPE_VIDEO = 2
const val MEDIA_TYPE_LIVE = 3


//1=popularity;2=croud;3=fun

const val SORT_BY_POPULARITY = 1
const val SORT_BY_CROWED = 2
const val SORT_BY_FUN = 3


//Supported locale
const val LOCALE_ENGLISH = "en"
const val LOCALE_BURMESE = "my"


const val TIER_SOCIALIZER = "Socializer"
const val TIER_PARTYER = "Partyer"
const val TIER_RAGER = "Rager"
const val TIER_VIP = "VIP"


enum class Activities(var text: String, var icon: Int) {
    DRINKING("Drinking", R.drawable.ic_drinking),
    PARTYING("Partying", R.drawable.ic_partying),
    DANCING("Dancing", R.drawable.ic_dancing),
    LISTING_MUSIC("Listening to music", R.drawable.ic_listening_music),
    CELEBRATING("Celebrating", R.drawable.ic_celebrating),
    SINGING("Singing", R.drawable.ic_singing)
}

val ITEMS_ACTIVITY = ArrayList<String>().apply {
    add("Drinking")
    add("Partying")
    add("Dancing")
    add("Listening to music")
    add("Celebrating")
    add("Singing")
}


enum class Feeling(var text: String, var icon: Int) {
    Drunk("Drunk", R.drawable.ic_drunk),
    Tipsy("Tipsy", R.drawable.ic_tipsy),
    Happy("Happy", R.drawable.ic_happy),
    Excited("Excited", R.drawable.ic_excited),
    Silly("Silly", R.drawable.ic_silly),
    Sexy("Sexy", R.drawable.ic_sexy),
    Awesome("Awesome", R.drawable.ic_awesome),
    Sober("Sober", R.drawable.ic_sober)
}

val ITEMS_FEELING = ArrayList<String>().apply {
    add("Drunk")
    add("Tipsy")
    add("Happy")
    add("Excited")
    add("Silly")
    add("Sexy")
    add("Awesome")
    add("Sober")
}


