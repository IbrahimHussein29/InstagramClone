package com.sec.instagramclone.data.repository

import android.net.Uri
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.HttpException
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.sec.instagramclone.data.Constants
import com.sec.instagramclone.data.body.LoginBody
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

class AppRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth
) : AppRepository {
    override val currentUser: FirebaseUser?
        get() = firebaseAuth.currentUser

    override fun login(body: LoginBody): Flow<Resource<FirebaseUser>> {
        return flow {

            emit(Resource.Loading())

            try {
                val result =
                    firebaseAuth.signInWithEmailAndPassword(body.email, body.password).await()
                emit((result.user?.let {
                    Resource.Success(data = it)
                }!!))

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

    override fun register(userBody: UserBody): Flow<Resource<FirebaseUser>> {
        return flow {
            emit(Resource.Loading())

            try {
                val result = firebaseAuth.createUserWithEmailAndPassword(
                    userBody.email,
                    userBody.password
                ).await()
                FirebaseFirestore.getInstance().collection(Constants.USER_NODE)
                    .document(firebaseAuth.currentUser!!.uid)
                    .set(userBody).await()

                emit((result.user?.let {
                    Resource.Success(data = it)
                }!!))

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
    }

    override fun getLoggedUser(): Flow<Resource<FirebaseUser>> {
        return flow {

            emit(Resource.Loading())

            if (firebaseAuth.currentUser != null) {
                emit(Resource.Success(data = firebaseAuth.currentUser!!))
            } else {
                emit(Resource.Error("Not Logged"))
            }

        }.flowOn(Dispatchers.IO)
    }

    override fun getUserData(body: UserBody): Flow<Resource<FirebaseUser>> {
        return flow {
            emit(Resource.Loading())
            if (firebaseAuth.currentUser != null) {
                try {
                    val snapshot = FirebaseFirestore.getInstance().collection(Constants.USER_NODE)
                        .document(firebaseAuth.currentUser!!.uid).get().await()
                    if (snapshot.exists()) {
                        val user: FirebaseUser? = snapshot.toObject(FirebaseUser::class.java)
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
}