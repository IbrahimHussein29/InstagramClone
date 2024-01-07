package com.sec.instagramclone.ui.main.upload

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sec.instagramclone.data.body.PostBody
import com.sec.instagramclone.data.body.ReelBody
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
class UploadVM @Inject constructor(private val appRepositoryImpl: AppRepositoryImpl): ViewModel() {
    private var _imageData = MutableStateFlow<Resource<Unit>?>(null)
    val imageData: StateFlow<Resource<Unit>?> = _imageData
    private var _postImage= MutableStateFlow<Resource<PostBody>?>(null)
    val postImage: StateFlow<Resource<PostBody>?> = _postImage
    private var _postReel= MutableStateFlow<Resource<ReelBody>?>(null)
    val postReel: StateFlow<Resource<ReelBody>?> = _postReel
    private var _videoData = MutableStateFlow<Resource<Unit>?>(null)
    val videoData: StateFlow<Resource<Unit>?> = _videoData
    private val _userData = MutableStateFlow<Resource<UserBody>?>(null)
    val userData: StateFlow<Resource<UserBody>?> = _userData


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
                    _imageData.value = Resource.Loading()
                }

                is Resource.Error -> {
                    _imageData.value = Resource.Error(message = it.message ?: "")
                }

                is Resource.Success -> {
                    _imageData.value = Resource.Success(data = it.data)
                }
            }

        }.launchIn(viewModelScope)

    }

    fun postImage(post:PostBody){
        appRepositoryImpl.postImage(post).onEach {

            when (it) {
                is Resource.Loading -> {
                    _postImage.value = Resource.Loading()
                }

                is Resource.Error -> {
                    _postImage.value = Resource.Error(message = it.message ?: "")
                }

                is Resource.Success -> {
                    _postImage.value = Resource.Success(data = it.data)
                }
            }

        }.launchIn(viewModelScope)
    }

    fun uploadReel(uri: Uri, folder: String, callBack: (String?) -> Unit) {
        appRepositoryImpl.uploadReel(uri, folder, callBack).onEach {
            when (it) {
                is Resource.Loading -> {
                    _videoData.value = Resource.Loading()
                }

                is Resource.Error -> {
                    _videoData.value = Resource.Error(message = it.message ?: "")
                }

                is Resource.Success -> {
                    _videoData.value = Resource.Success(data = it.data)
                }
            }

        }.launchIn(viewModelScope)

    }

    fun postReel(reel: ReelBody){
        appRepositoryImpl.postReel(reel).onEach {

            when (it) {
                is Resource.Loading -> {
                    _postReel.value = Resource.Loading()
                }

                is Resource.Error -> {
                    _postReel.value = Resource.Error(message = it.message ?: "")
                }

                is Resource.Success -> {
                    _postReel.value = Resource.Success(data = it.data)
                }
            }

        }.launchIn(viewModelScope)
    }



}