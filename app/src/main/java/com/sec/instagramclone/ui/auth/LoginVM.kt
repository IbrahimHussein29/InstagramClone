package com.sec.instagramclone.ui.auth

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseUser
import com.sec.instagramclone.data.body.UserBody
import com.sec.instagramclone.data.common.Resource
import com.sec.instagramclone.data.repository.AppRepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject


@HiltViewModel
class LoginVM @Inject constructor(
    private val appRepositoryImpl: AppRepositoryImpl
) : ViewModel() {

    private val _user = MutableStateFlow<Resource<FirebaseUser>?>(null)
    val user: StateFlow<Resource<FirebaseUser>?> = _user
    private val _userData = MutableStateFlow<Resource<UserBody>?>(null)
    val userData: StateFlow<Resource<UserBody>?> = _userData
    private var _imageDate = MutableStateFlow<Resource<Unit>?>(null)
    val imageDate: StateFlow<Resource<Unit>?> = _imageDate


    fun register(email: String, password: String, user: UserBody) {
        appRepositoryImpl.register(email, password, user).onEach {
            when (it) {
                is Resource.Loading -> {
                    _user.value = Resource.Loading()
                }

                is Resource.Error -> {
                    _user.value = Resource.Error(message = it.message ?: "")
                }

                is Resource.Success -> {
                    _user.value = Resource.Success(data = it.data)
                }
            }
        }.launchIn(viewModelScope)
    }

    fun login(email: String, password: String) {
        appRepositoryImpl.login(email, password).onEach {
            when (it) {
                is Resource.Loading -> {
                    _user.value = Resource.Loading()
                }

                is Resource.Error -> {
                    _user.value = Resource.Error(message = it.message ?: "")
                }

                is Resource.Success -> {
                    _user.value = Resource.Success(data = it.data)
                }
            }
        }.launchIn(viewModelScope)

    }

    fun logout() {
        appRepositoryImpl.logout()
        _user.value = null
        _userData.value=null

    }

    fun loggedUser() {

        appRepositoryImpl.getLoggedUser().onEach {
            when (it) {
                is Resource.Loading -> {
                    _user.value = Resource.Loading()
                }

                is Resource.Error -> {
                    _user.value = Resource.Error(message = it.message ?: "")
                }

                is Resource.Success -> {
                    _user.value = Resource.Success(data = it.data)
                }
            }
        }.launchIn(viewModelScope)
    }

    fun getUserData() {
        appRepositoryImpl.getUserData().onEach {
            when (it) {
                is Resource.Loading -> {
                    _userData.value = Resource.Loading()
                }

                is Resource.Error -> {
                    _userData.value = Resource.Error(message = it.message ?: "")
                }

                is Resource.Success -> {
                    _userData.value = Resource.Success(data = it.data)
                }
            }

        }.launchIn(viewModelScope)
    }


    fun uploadImage(uri: Uri, folder: String, callBack: (String?) -> Unit) {
        appRepositoryImpl.uploadImage(uri, folder, callBack).onEach {
            when (it) {
                is Resource.Loading -> {
                    _imageDate.value = Resource.Loading()
                }

                is Resource.Error -> {
                    _imageDate.value = Resource.Error(message = it.message ?: "")
                }

                is Resource.Success -> {
                    _imageDate.value = Resource.Success(data = it.data)
                }
            }

        }.launchIn(viewModelScope)

    }
}




