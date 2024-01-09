package com.sec.instagramclone.data.body

import java.io.Serializable

data class UserBody(
    var userImage: String? = null,
    var name: String = "",
    var email: String = "",
    var password: String = "",
    var isFollowed: Boolean = true,
    var posts:ArrayList<PostBody>?=null,
    var reels:ArrayList<ReelBody>?=null,
    var media:ArrayList<MediaBody>?=null
) : Serializable
