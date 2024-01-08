package com.sec.instagramclone.ui.main.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sec.instagramclone.data.body.MediaBody

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
class HomeVM @Inject constructor(
    private val appRepositoryImpl: AppRepositoryImpl
) : ViewModel() {
    private val _mediaData = MutableStateFlow<Resource<ArrayList<MediaBody>>?>(null)
    val mediaData: StateFlow<Resource<ArrayList<MediaBody>>?> = _mediaData

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

    fun getAllMedia(media: MediaBody) {
        appRepositoryImpl.getMedia(media).onEach {
            when (it) {
                is Resource.Loading -> {
                    _mediaData.value = Resource.Loading()
                }

                is Resource.Error -> {
                    _mediaData.value = Resource.Error(message = it.message ?: "")
                }

                is Resource.Success -> {
                    _mediaData.value= Resource.Success(data = it.data)
                }
            }

        }.launchIn(viewModelScope)

    }

}
