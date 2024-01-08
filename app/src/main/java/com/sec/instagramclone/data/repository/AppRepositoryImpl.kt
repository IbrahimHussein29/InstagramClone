package com.sec.instagramclone.data.repository

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.HttpException
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import com.google.firebase.storage.FirebaseStorage
import com.sec.instagramclone.data.Constants
import com.sec.instagramclone.data.body.MediaBody
import com.sec.instagramclone.data.body.PostBody
import com.sec.instagramclone.data.body.ReelBody
import com.sec.instagramclone.data.body.UserBody
import com.sec.instagramclone.data.common.Resource
import com.sec.instagramclone.data.common.await
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import java.io.IOException
import java.util.UUID
import javax.inject.Inject

@Suppress("NAME_SHADOWING")
class AppRepositoryImpl @Inject constructor(

) : AppRepository {
    private var firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private var loggedOutLiveData: MutableLiveData<Boolean> = MutableLiveData()
    private val fireStoreDatabase: FirebaseFirestore = FirebaseFirestore.getInstance()

    init {
        if (firebaseAuth.currentUser != null) {
            loggedOutLiveData.postValue(false)
        }
    }

    override fun register(
        email: String,
        password: String,
        user: UserBody
    ): Flow<Resource<FirebaseUser>> {
        return flow {
            emit(Resource.Loading())

            try {
                val result = firebaseAuth.createUserWithEmailAndPassword(
                    email,
                    password
                ).await()
                fireStoreDatabase.collection(Constants.USER_NODE)
                    .document(firebaseAuth.currentUser!!.uid)
                    .set(user).await()

                emit((result.user?.let {
                    Resource.Success(data = it)
                }!!))
                loggedOutLiveData.postValue(false)
            } catch (e: HttpException) {
                emit(Resource.Error(message = e.localizedMessage ?: "Unknown Error"))
            } catch (e: IOException) {
                emit(
                    Resource.Error(
                        message = e.localizedMessage ?: "Check Your Internet Connection"
                    )
                )
            } catch (e: Exception) {
                emit(Resource.Error(message = e.localizedMessage ?: ""))
            }


        }.flowOn(Dispatchers.IO)
    }

    override fun login(email: String, password: String): Flow<Resource<FirebaseUser>> {
        return flow {

            emit(Resource.Loading())

            try {
                val result =
                    firebaseAuth.signInWithEmailAndPassword(email, password).await()
                emit((result.user?.let {
                    Resource.Success(data = it)
                }!!))
                loggedOutLiveData.postValue(false)
            } catch (e: HttpException) {
                emit(Resource.Error(message = e.localizedMessage ?: "Unknown Error"))
            } catch (e: IOException) {
                emit(
                    Resource.Error(
                        message = e.localizedMessage ?: "Check Your Internet Connection"
                    )
                )
            } catch (e: Exception) {
                emit(Resource.Error(message = e.localizedMessage ?: ""))
            }

        }.flowOn(Dispatchers.IO)
    }

    override fun logout() {
        firebaseAuth.signOut()
        loggedOutLiveData.postValue(true)
    }

    override fun getLoggedUser(): Flow<Resource<FirebaseUser>> {
        return flow {

            emit(Resource.Loading())

            if (firebaseAuth.currentUser != null) {
                loggedOutLiveData.postValue(false)
                emit(Resource.Success(data = firebaseAuth.currentUser!!))
            } else {
                emit(Resource.Error("Not Logged"))
            }

        }.flowOn(Dispatchers.IO)
    }

    override fun getUserData(): Flow<Resource<UserBody>> {
        return flow {
            emit(Resource.Loading())
            if (firebaseAuth.currentUser != null) {
                try {
                    val snapshot = FirebaseFirestore.getInstance().collection(Constants.USER_NODE)
                        .document(firebaseAuth.currentUser!!.uid).get().await()
                    if (snapshot.exists()) {
                        val user: UserBody? = snapshot.toObject(UserBody::class.java)
                        emit(Resource.Success(data = user!!))
                    }
                } catch (e: HttpException) {
                    emit(Resource.Error(message = e.localizedMessage ?: "Unknown Error"))
                } catch (e: IOException) {
                    emit(
                        Resource.Error(
                            message = e.localizedMessage ?: "Check Your Internet Connection"
                        )
                    )
                } catch (e: Exception) {
                    emit(Resource.Error(message = e.localizedMessage ?: ""))
                }
            }
        }.flowOn(Dispatchers.IO)

    }

    override fun updateUserData(user: UserBody): Flow<Resource<UserBody>> {

        return flow {
            emit(Resource.Loading())
            try {
                 fireStoreDatabase.collection(Constants.USER_NODE)
                    .document(firebaseAuth.currentUser!!.uid)
                    .set(user).await()
                emit(Resource.Success(data = user))
            } catch (e: HttpException) {
                emit(Resource.Error(message = e.localizedMessage ?: "Unknown Error"))
            } catch (e: IOException) {
                emit(
                    Resource.Error(
                        message = e.localizedMessage ?: "Check Your Internet Connection"
                    )
                )
            } catch (e: Exception) {
                emit(Resource.Error(message = e.localizedMessage ?: ""))
            }
        }.flowOn(Dispatchers.IO)

    }

    override fun uploadImage(
        uri: Uri,
        folderName: String,
        callBack: (String?) -> Unit
    ): Flow<Resource<Unit>> {
        return flow {
            emit(Resource.Loading())
            try {
                val snapshot = FirebaseStorage.getInstance().getReference(folderName)
                    .child(UUID.randomUUID().toString()).putFile(uri).await()
                val imageUrl = snapshot.storage.downloadUrl.await()
                emit(Resource.Success(callBack(imageUrl.toString())))
            } catch (e: HttpException) {
                emit(Resource.Error(message = e.localizedMessage ?: "Unknown Error"))
            } catch (e: IOException) {
                emit(
                    Resource.Error(
                        message = e.localizedMessage ?: "Check Your Internet Connection"
                    )
                )
            } catch (e: Exception) {
                emit(Resource.Error(message = e.localizedMessage ?: ""))
            }
        }
    }

    override fun postImage(post: PostBody, media: MediaBody): Flow<Resource<PostBody>> {
        return flow {
            emit(Resource.Loading())
            try {
            fireStoreDatabase.collection(Constants.POST)
                    .document()
                    .set(post).await()
                fireStoreDatabase.collection(firebaseAuth.currentUser!!.uid).document().set(post)
                    .await()
                fireStoreDatabase.collection(Constants.MEDIA).document().set(media).await()
                emit(Resource.Success(data = post))
            } catch (e: HttpException) {
                emit(Resource.Error(message = e.localizedMessage ?: "Unknown Error"))
            } catch (e: IOException) {
                emit(
                    Resource.Error(
                        message = e.localizedMessage ?: "Check Your Internet Connection"
                    )
                )
            } catch (e: Exception) {
                emit(Resource.Error(message = e.localizedMessage ?: ""))
            }
        }.flowOn(Dispatchers.IO)
    }


    override fun addPostToProfile(post: PostBody): Flow<Resource<ArrayList<PostBody>>> {
        return flow {
            emit(Resource.Loading())
            try {
                val result =
                    fireStoreDatabase.collection(firebaseAuth.currentUser!!.uid).get().await()
                val postList = arrayListOf<PostBody>()
                val tempList = arrayListOf<PostBody>()
                for (i in result.documents) {
                    val post: PostBody = i.toObject<PostBody>()!!
                    tempList.add(post)
                }
                postList.addAll(tempList)
                postList.reverse()
                emit(Resource.Success(data = postList))
            } catch (e: HttpException) {
                emit(Resource.Error(message = e.localizedMessage ?: "Unknown Error"))
            } catch (e: IOException) {
                emit(
                    Resource.Error(
                        message = e.localizedMessage ?: "Check Your Internet Connection"
                    )
                )
            } catch (e: Exception) {
                emit(Resource.Error(message = e.localizedMessage ?: ""))
            }
        }.flowOn(Dispatchers.IO)
    }

    override fun addReelToProfile(reel: ReelBody): Flow<Resource<ArrayList<ReelBody>>> {
        return flow {
            emit(Resource.Loading())
            try {
                val result =
                    fireStoreDatabase.collection(firebaseAuth.currentUser!!.uid + Constants.REEL)
                        .get().await()
                val reelList = arrayListOf<ReelBody>()
                val tempList = arrayListOf<ReelBody>()
                for (i in result.documents) {
                    val reel: ReelBody = i.toObject<ReelBody>()!!
                    tempList.add(reel)
                }
                reelList.addAll(tempList)
                reelList.reverse()
                emit(Resource.Success(data = reelList))
            } catch (e: HttpException) {
                emit(Resource.Error(message = e.localizedMessage ?: "Unknown Error"))
            } catch (e: IOException) {
                emit(
                    Resource.Error(
                        message = e.localizedMessage ?: "Check Your Internet Connection"
                    )
                )
            } catch (e: Exception) {
                emit(Resource.Error(message = e.localizedMessage ?: ""))
            }
        }.flowOn(Dispatchers.IO)
    }

    override fun uploadReel(
        uri: Uri,
        folderName: String,
        callBack: (String?) -> Unit
    ): Flow<Resource<Unit>> {
        return flow {
            emit(Resource.Loading())
            try {
//               var progressDialog: ProgressDialog= ProgressDialog(this)
//                var uploadedValue:Long= 0
                val snapshot = FirebaseStorage.getInstance().getReference(folderName)
                    .child(UUID.randomUUID().toString()).putFile(uri).await()
                val videoUrl = snapshot.storage.downloadUrl.await()
//                uploadedValue=(snapshot.bytesTransferred)/(snapshot.totalByteCount)
//                progressDialog.setMessage("Uploaded $uploadedValue")


                emit(Resource.Success(callBack(videoUrl.toString())))

            } catch (e: HttpException) {
                emit(Resource.Error(message = e.localizedMessage ?: "Unknown Error"))
            } catch (e: IOException) {
                emit(
                    Resource.Error(
                        message = e.localizedMessage ?: "Check Your Internet Connection"
                    )
                )
            } catch (e: Exception) {
                emit(Resource.Error(message = e.localizedMessage ?: ""))
            }
        }
    }


    override fun postReel( reel: ReelBody, media:MediaBody): Flow<Resource<ReelBody>> {
        return flow {
            emit(Resource.Loading())
            try {
                fireStoreDatabase.collection(Constants.REEL)
                    .document()
                    .set(reel).await()
                fireStoreDatabase.collection(firebaseAuth.currentUser!!.uid + Constants.REEL)
                    .document().set(reel)
                    .await()
                fireStoreDatabase.collection(Constants.MEDIA).document().set(media).await()

                emit(Resource.Success(data = reel))
            } catch (e: HttpException) {
                emit(Resource.Error(message = e.localizedMessage ?: "Unknown Error"))
            } catch (e: IOException) {
                emit(
                    Resource.Error(
                        message = e.localizedMessage ?: "Check Your Internet Connection"
                    )
                )
            } catch (e: Exception) {
                emit(Resource.Error(message = e.localizedMessage ?: ""))
            }
        }.flowOn(Dispatchers.IO)
    }

    override fun getReel(reel: ReelBody): Flow<Resource<ArrayList<ReelBody>>> {
        return flow {
            emit(Resource.Loading())
            try {
                val result =
                    fireStoreDatabase.collection(Constants.REEL).get().await()
                val reelList = arrayListOf<ReelBody>()
                val tempList = arrayListOf<ReelBody>()
                for (i in result.documents) {
                    val reel: ReelBody = i.toObject<ReelBody>()!!
                    tempList.add(reel)
                }
                reelList.addAll(tempList)
                reelList.reverse()

                emit(Resource.Success(data = reelList))
            } catch (e: HttpException) {
                emit(Resource.Error(message = e.localizedMessage ?: "Unknown Error"))
            } catch (e: IOException) {
                emit(
                    Resource.Error(
                        message = e.localizedMessage ?: "Check Your Internet Connection"
                    )
                )
            } catch (e: Exception) {
                emit(Resource.Error(message = e.localizedMessage ?: ""))
            }
        }.flowOn(Dispatchers.IO)
    }
    override fun getMedia(media: MediaBody): Flow<Resource<ArrayList<MediaBody>>> {
        return flow {
            emit(Resource.Loading())
            try {
                val result =
                    fireStoreDatabase.collection(Constants.MEDIA).get().await()
                val mediaList = arrayListOf<MediaBody>()
                val tempList = arrayListOf<MediaBody>()
                for (i in result.documents) {
                    val media: MediaBody = i.toObject<MediaBody>()!!
                    tempList.add(media)


                }

                mediaList.addAll(tempList)
           mediaList.reverse()


                emit(Resource.Success(data = mediaList))
            } catch (e: HttpException) {
                emit(Resource.Error(message = e.localizedMessage ?: "Unknown Error"))
            } catch (e: IOException) {
                emit(
                    Resource.Error(
                        message = e.localizedMessage ?: "Check Your Internet Connection"
                    )
                )
            } catch (e: Exception) {
                emit(Resource.Error(message = e.localizedMessage ?: ""))
            }
        }.flowOn(Dispatchers.IO)
    }


    override fun getAllUsers(user: UserBody): Flow<Resource<ArrayList<UserBody>>> {
        return flow {
            emit(Resource.Loading())
            try {
                val result =
                    fireStoreDatabase.collection(Constants.USER_NODE).get().await()
                val userList = arrayListOf<UserBody>()
                val tempList = arrayListOf<UserBody>()
                for (i in result.documents) {
                    if(i.id != firebaseAuth.currentUser!!.uid){
                        val user: UserBody = i.toObject<UserBody>()!!

                        tempList.add(user)
                    }

                    }
                userList.addAll(tempList)


                emit(Resource.Success(data = userList))
            } catch (e: HttpException) {
                emit(Resource.Error(message = e.localizedMessage ?: "Unknown Error"))
            } catch (e: IOException) {
                emit(
                    Resource.Error(
                        message = e.localizedMessage ?: "Check Your Internet Connection"
                    )
                )
            } catch (e: Exception) {
                emit(Resource.Error(message = e.localizedMessage ?: ""))
            }
        }.flowOn(Dispatchers.IO)
    }

    override fun searchUsers(name:String, user: UserBody): Flow<Resource<ArrayList<UserBody>>> {
        return flow {
            emit(Resource.Loading())
            try {
                val result =
                    fireStoreDatabase.collection(Constants.USER_NODE).whereEqualTo("name",name).get().await()
                if(!result.isEmpty) {
                    val userList = arrayListOf<UserBody>()
                    val tempList = arrayListOf<UserBody>()
                    for (i in result.documents) {
                        if(i.id != firebaseAuth.currentUser!!.uid){
                            val user: UserBody = i.toObject<UserBody>()!!

                            tempList.add(user)
                        }

                    }
                    userList.addAll(tempList)


                    emit(Resource.Success(data = userList))
                }

            } catch (e: HttpException) {
                emit(Resource.Error(message = e.localizedMessage ?: "Unknown Error"))
            } catch (e: IOException) {
                emit(
                    Resource.Error(
                        message = e.localizedMessage ?: "Check Your Internet Connection"
                    )
                )
            } catch (e: Exception) {
                emit(Resource.Error(message = e.localizedMessage ?: ""))
            }
        }.flowOn(Dispatchers.IO)
    }

    override fun followUsers(
        isFollowed: Boolean,
        user: UserBody
    ): Flow<Resource<UserBody>> {
        return flow {
            emit(Resource.Loading())
            try {
                if(isFollowed) {
                    val result =
                        fireStoreDatabase.collection(FirebaseAuth.getInstance().currentUser!!.uid +Constants.FOLLOW).document().set(user).await()

                    emit(Resource.Success(data = user))
                }else{
                    val result =
                    fireStoreDatabase.collection(FirebaseAuth.getInstance().currentUser!!.uid +Constants.FOLLOW).document().delete().await()
                    emit(Resource.Success(data = user))
                }


            } catch (e: HttpException) {
                emit(Resource.Error(message = e.localizedMessage ?: "Unknown Error"))
            } catch (e: IOException) {
                emit(
                    Resource.Error(
                        message = e.localizedMessage ?: "Check Your Internet Connection"
                    )
                )
            } catch (e: Exception) {
                emit(Resource.Error(message = e.localizedMessage ?: ""))
            }
        }.flowOn(Dispatchers.IO)
    }


}