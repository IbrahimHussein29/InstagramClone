package com.sec.instagramclone.data.repository

import android.net.Uri
import com.google.firebase.auth.FirebaseUser
import com.sec.instagramclone.data.body.LoginBody
import com.sec.instagramclone.data.body.UserBody
import com.sec.instagramclone.data.common.Resource
import kotlinx.coroutines.flow.Flow

interface AppRepository {
    val currentUser: FirebaseUser?
    fun login(body: LoginBody): Flow<Resource<FirebaseUser>>
    fun register(body: UserBody): Flow<Resource<FirebaseUser>>
    fun logout()

    fun getLoggedUser(): Flow<Resource<FirebaseUser>>
    fun getUserData(body: UserBody): Flow<Resource<FirebaseUser>>

    fun uploadImage(uri: Uri, folderName: String, callBack: (String?) -> Unit): Flow<Resource<Unit>>

}