package com.sec.instagramclone.data.body

import java.io.Serializable

data class PostBody(
    var postUsername: String = "",
    var postUserImage: String = "",
    var time: Long = 0,
    var postUrl: String = "",
    var caption: String = ""

) : Serializable

