package org.d3ifcool.medisgo.ui.profile

import android.graphics.Bitmap
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.d3ifcool.medisgosh.util.ResponseStatus
import org.d3ifcool.medisgosh.model.User
import org.d3ifcool.medisgosh.repository.UserRepository
import org.d3ifcool.medisgosh.util.MediaUtils


class ProfileViewModel(
    private val userRepository: UserRepository,
) : ViewModel() {

    var fetchStatus = mutableStateOf(ResponseStatus.IDLE)
        private set

    var submissionStatus = mutableStateOf(ResponseStatus.IDLE)
        private set

    var profileData = mutableStateOf<User?>(null)
        private set

    init {
        observeProfileData()
    }

    fun observeProfileData() {
        fetchStatus.value = ResponseStatus.LOADING
        viewModelScope.launch(Dispatchers.IO) {
            userRepository.getProfileData(
                onDataUpdated = {
                    fetchStatus.value = ResponseStatus.SUCCESS
                    profileData.value = it
                    Log.d("ProfileVM", "fetchedInitData $it")
                },
                onError = {
                    fetchStatus.value = ResponseStatus.FAILED.apply {
                        updateMessage("Failed: ${it.message}")
                    }
                }
            )
        }
    }

    fun changeProfilePicture(image: ByteArray) {
        submissionStatus.value = ResponseStatus.LOADING
        viewModelScope.launch(Dispatchers.IO) {
            submissionStatus.value = userRepository.changePfp(image)
        }
    }

    fun editProfile(user: User, bitmap: Bitmap? = null) {
        submissionStatus.value = ResponseStatus.LOADING
        viewModelScope.launch(Dispatchers.IO) {
            submissionStatus.value =
                userRepository.editProfile(user, bitmap?.let { MediaUtils.bitmapToByteArray(it) })
        }
    }

    fun logout() {
        submissionStatus.value = ResponseStatus.LOADING
        viewModelScope.launch {
            submissionStatus.value = userRepository.signOut()
        }
    }

    fun resetStatus() {
        submissionStatus.value = ResponseStatus.IDLE
    }

    override fun onCleared() {
        userRepository.removeListener()
        super.onCleared()
    }
}