package com.sec.instagramclone.ui.main.profile.profilePosts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sec.instagramclone.data.body.PostBody
import com.sec.instagramclone.data.common.Resource
import com.sec.instagramclone.data.repository.AppRepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class ProfilePostsVM @Inject constructor(
    private val appRepositoryImpl: AppRepositoryImpl
) : ViewModel() {
    private val _postData = MutableStateFlow<Resource<ArrayList<PostBody>>?>(null)
    val post: StateFlow<Resource<ArrayList<PostBody>>?> = _postData



    fun addPostToProfile(post: PostBody){
        appRepositoryImpl.addPostToProfile(post).onEach {
            when (it) {
                is Resource.Loading -> {
                    _postData.value = Resource.Loading()
                }

                is Resource.Error -> {
                    _postData.value = Resource.Error(message = it.message ?: "")
                }

                is Resource.Success -> {
                    _postData.value = Resource.Success(data = it.data)
                }
            }

        }.launchIn(viewModelScope)

    }


}