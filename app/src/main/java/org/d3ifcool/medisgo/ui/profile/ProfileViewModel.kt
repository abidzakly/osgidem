package org.d3ifcool.medisgo.ui.profile

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.d3ifcool.medisgosh.util.AppObjectState
import org.d3ifcool.medisgosh.model.User
import org.d3ifcool.medisgosh.repository.UserRepository


class ProfileViewModel(
    private val userRepository: UserRepository,
) : ViewModel() {

    var fetchStatus = mutableStateOf(AppObjectState.IDLE)
        private set

    var submissionStatus = mutableStateOf(AppObjectState.IDLE)
        private set

    var profileData = mutableStateOf<User?>(null)
        private set

    init {
        observeProfileData()
    }

    fun observeProfileData() {
        fetchStatus.value = AppObjectState.LOADING
        viewModelScope.launch(Dispatchers.IO) {
            userRepository.getProfileData(
                onDataUpdated = {
                    fetchStatus.value = AppObjectState.SUCCESS
                    profileData.value = it
                    Log.d("ProfileVM", "fetchedInitData $it")
                },
                onError = {
                    fetchStatus.value = AppObjectState.FAILED.apply {
                        updateMessage("Failed: ${it.message}")
                    }
                }
            )
        }
    }

    fun changeProfilePicture(image: ByteArray) {
        submissionStatus.value = AppObjectState.LOADING
        viewModelScope.launch(Dispatchers.IO) {
            submissionStatus.value = userRepository.changePfp(image)
        }
    }

    fun editProfile(user: User) {
        submissionStatus.value = AppObjectState.LOADING
        viewModelScope.launch(Dispatchers.IO) {
            submissionStatus.value = userRepository.editProfile(user)
        }
    }

    fun resetStatus() {
        submissionStatus.value = AppObjectState.IDLE
    }

    override fun onCleared() {
        userRepository.removeListener()
        super.onCleared()
    }
}