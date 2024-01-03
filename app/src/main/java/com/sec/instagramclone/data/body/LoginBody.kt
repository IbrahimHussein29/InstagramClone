package com.sec.instagramclone.data.body

import java.io.Serializable

data class LoginBody(

    var email: String,
    var password: String
):Serializable
