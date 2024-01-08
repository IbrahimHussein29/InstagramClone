package com.sec.instagramclone.data.repository

import android.net.Uri
import com.google.firebase.auth.FirebaseUser
import com.sec.instagramclone.data.body.MediaBody
import com.sec.instagramclone.data.body.PostBody
import com.sec.instagramclone.data.body.ReelBody
import com.sec.instagramclone.data.body.UserBody
import com.sec.instagramclone.data.common.Resource
import kotlinx.coroutines.flow.Flow

interface AppRepository {

    fun login(email: String, password: String): Flow<Resource<FirebaseUser>>
    fun register(email: String, password: String, user: UserBody): Flow<Resource<FirebaseUser>>
    fun logout()

    fun getLoggedUser(): Flow<Resource<FirebaseUser>>
    fun getUserData(): Flow<Resource<UserBody>>
   fun updateUserData(user: UserBody):Flow<Resource<UserBody>>


    fun uploadImage(uri: Uri, folderName: String, callBack: (String?) -> Unit): Flow<Resource<Unit>>
    fun postImage(post: PostBody, media:MediaBody):Flow<Resource<PostBody>>
    fun addPostToProfile(post:PostBody):Flow<Resource<ArrayList<PostBody>>>

    fun addReelToProfile(reel:ReelBody):Flow<Resource<ArrayList<ReelBody>>>

    fun uploadReel(uri: Uri, folderName: String, callBack: (String?) -> Unit): Flow<Resource<Unit>>


    fun postReel(reel: ReelBody, media:MediaBody):Flow<Resource<ReelBody>>
    fun getReel(reel:ReelBody):Flow<Resource<ArrayList<ReelBody>>>

    fun getMedia(media:MediaBody):Flow<Resource<ArrayList<MediaBody>>>
    fun getAllUsers(user:UserBody):Flow<Resource<ArrayList<UserBody>>>
    fun searchUsers(name:String, user:UserBody):Flow<Resource<ArrayList<UserBody>>>
    fun followUsers(isFollowed:Boolean, user: UserBody):Flow<Resource<UserBody>>


}