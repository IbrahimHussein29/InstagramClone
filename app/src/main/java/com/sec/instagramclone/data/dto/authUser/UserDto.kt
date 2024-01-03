package com.sec.instagramclone.data.dto.authUser


import java.io.Serializable

data class UserDto(
    val id:String,
    var userImage:String?=null,
    val name: String?=null,
    val email: String?=null,
    val password: String?=null
) : Serializable
