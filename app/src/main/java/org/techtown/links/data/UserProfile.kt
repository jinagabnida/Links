package org.techtown.links.data

data class UserProfile(
    var email : String,
    var password : String,
    val count : Int,
    var uid : String
)
