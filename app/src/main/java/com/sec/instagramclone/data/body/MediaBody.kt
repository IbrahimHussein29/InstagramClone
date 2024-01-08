package com.sec.instagramclone.data.body

import java.io.Serializable

data class MediaBody(
    var mediaUsername: String = "",
    var mediaUserImage: String? = null,
    var time: Long = 0,
    var postUrl: String = "",
    var videoUrl:String="",
    var caption: String = ""

) : Serializable

