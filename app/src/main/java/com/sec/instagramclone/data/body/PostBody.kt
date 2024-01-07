package com.sec.instagramclone.data.body

import java.io.Serializable

data class PostBody(
    var postUsername:String="",
    var postUrl: String = "",
    var caption: String = ""
):Serializable
