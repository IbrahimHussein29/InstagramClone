package com.sec.instagramclone.ui.main.profile.profileReels

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
class ProfileReelsVM @Inject constructor(
    private val appRepositoryImpl: AppRepositoryImpl
) : ViewModel() {
    private val _reelData = MutableStateFlow<Resource<ArrayList<ReelBody>>?>(null)
    val reelData: StateFlow<Resource<ArrayList<ReelBody>>?> = _reelData


    fun addReelToProfile(reel: ReelBody) {
        appRepositoryImpl.addReelToProfile(reel).onEach {
            when (it) {
                is Resource.Loading -> {
                    _reelData.value = Resource.Loading()
                }

                is Resource.Error -> {
                    _reelData.value = Resource.Error(message = it.message ?: "")
                }

                is Resource.Success -> {
                    _reelData.value = Resource.Success(data = it.data)
                }
            }

        }.launchIn(viewModelScope)

    }

}