package com.sec.instagramclone.data.body

import java.io.Serializable

data class ReelBody(
    var reelUrl: String = "",
    var reelCaption: String = "",
    var profileLink: String? = null,
    var time:Long=0
) : Serializable
