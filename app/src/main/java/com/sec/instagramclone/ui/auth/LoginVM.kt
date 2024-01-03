package com.sec.instagramclone.ui.auth

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseUser
import com.sec.instagramclone.data.body.LoginBody
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

    private var _loginData = MutableStateFlow<Resource<FirebaseUser>?>(null)
    val loginData: StateFlow<Resource<FirebaseUser>?> = _loginData
    private var _registerData = MutableStateFlow<Resource<FirebaseUser>?>(null)
    val registerData: StateFlow<Resource<FirebaseUser>?> = _registerData
    private var _imageDate = MutableStateFlow<Resource<Unit>?>(null)
    val imageDate: StateFlow<Resource<Unit>?> = _imageDate


    fun register(body: UserBody) {
        appRepositoryImpl.register(body).onEach {
            when (it) {
                is Resource.Loading -> {
                    _registerData.value = Resource.Loading()
                }

                is Resource.Error -> {
                    _registerData.value = Resource.Error(message = it.message ?: "")
                }

                is Resource.Success -> {
                    _registerData.value = Resource.Success(data = it.data)
                }
            }
        }.launchIn(viewModelScope)
    }

    fun login(body: LoginBody) {
        appRepositoryImpl.login(body).onEach {
            when (it) {
                is Resource.Loading -> {
                    _loginData.value = Resource.Loading()
                }

                is Resource.Error -> {
                    _loginData.value = Resource.Error(message = it.message ?: "")
                }

                is Resource.Success -> {
                    _loginData.value = Resource.Success(data = it.data)
                }
            }
        }.launchIn(viewModelScope)

    }

    fun logout() {
        appRepositoryImpl.logout()
        _loginData.value = null

    }

    val currentUser: FirebaseUser?
        get() = appRepositoryImpl.currentUser

    init {
        if (appRepositoryImpl.currentUser != null) {
            _loginData.value = Resource.Success(appRepositoryImpl.currentUser!!)
        }
    }

    fun loggedUser() {

        appRepositoryImpl.getLoggedUser().onEach {
            when (it) {
                is Resource.Loading -> {
                    _loginData.value = Resource.Loading()
                }

                is Resource.Error -> {
                    _loginData.value = Resource.Error(message = it.message ?: "")
                }

                is Resource.Success -> {
                    _loginData.value = Resource.Success(data = it.data)
                }
            }
        }.launchIn(viewModelScope)
    }

    fun getUserData(body: UserBody) {
        appRepositoryImpl.getUserData(body).onEach {
            when (it) {
                is Resource.Loading -> {
                    _loginData.value = Resource.Loading()
                }

                is Resource.Error -> {
                    _loginData.value = Resource.Error(message = it.message ?: "")
                }

                is Resource.Success -> {
                    _loginData.value = Resource.Success(data = it.data)
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




