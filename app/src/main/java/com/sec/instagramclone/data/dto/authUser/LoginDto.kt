package com.sec.instagramclone.data.dto.authUser

import com.sec.instagramclone.data.dto.ApiBaseDto

data class LoginDto(
    val token: String,
    val user: UserDto
) : ApiBaseDto()
