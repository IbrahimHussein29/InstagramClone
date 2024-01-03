package com.sec.instagramclone.data.repository

import android.net.Uri
import com.google.firebase.auth.FirebaseUser
import com.sec.instagramclone.data.body.UserBody
import com.sec.instagramclone.data.common.Resource
import kotlinx.coroutines.flow.Flow

interface AppRepository {

    fun login(email: String, password: String): Flow<Resource<FirebaseUser>>
    fun register(email: String, password: String, user: UserBody): Flow<Resource<FirebaseUser>>
    fun logout()

    fun getLoggedUser(): Flow<Resource<FirebaseUser>>
    fun getUserData(): Flow<Resource<UserBody>>
    suspend fun updateUserData(user: UserBody)

    fun uploadImage(uri: Uri, folderName: String, callBack: (String?) -> Unit): Flow<Resource<Unit>>



}