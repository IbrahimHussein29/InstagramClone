package com.sec.instagramclone.ui.main.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
class SearchVM@Inject constructor(
    private val appRepositoryImpl: AppRepositoryImpl
) : ViewModel() {
    private val _userData = MutableStateFlow<Resource<ArrayList<UserBody>>?>(null)
    val userData: StateFlow<Resource<ArrayList<UserBody>>?> = _userData
    private val _searchData = MutableStateFlow<Resource<ArrayList<UserBody>>?>(null)
    val searchData: StateFlow<Resource<ArrayList<UserBody>>?> = _searchData
    private val _isFollowedData = MutableStateFlow<Resource<UserBody>?>(null)
    val isFollowedData: StateFlow<Resource<UserBody>?> = _isFollowedData

    fun getAllUsers(user: UserBody) {
        appRepositoryImpl.getAllUsers(user).onEach {
            when (it) {
                is Resource.Loading -> {
                    _userData.value = Resource.Loading()
                }

                is Resource.Error -> {
                    _userData.value  = Resource.Error(message = it.message ?: "")
                }

                is Resource.Success -> {
                    _userData.value  = Resource.Success(data = it.data)
                }
            }

        }.launchIn(viewModelScope)

    }
    fun searchUsers(name:String, user: UserBody) {
        appRepositoryImpl.searchUsers(name, user).onEach {
            when (it) {
                is Resource.Loading -> {
                    _userData.value = Resource.Loading()
                }

                is Resource.Error -> {
                    _userData.value  = Resource.Error(message = it.message ?: "")
                }

                is Resource.Success -> {
                    _userData.value  = Resource.Success(data = it.data)
                }
            }

        }.launchIn(viewModelScope)

    }
    fun isFollowedUser(isFollowed:Boolean, user: UserBody) {
        appRepositoryImpl.followUsers(isFollowed, user).onEach {
            when (it) {
                is Resource.Loading -> {
                    _isFollowedData.value = Resource.Loading()
                }

                is Resource.Error -> {
                    _isFollowedData.value = Resource.Error(message = it.message ?: "")
                }

                is Resource.Success -> {
                    _isFollowedData.value  = Resource.Success(data = it.data)
                }
            }

        }.launchIn(viewModelScope)

    }

}