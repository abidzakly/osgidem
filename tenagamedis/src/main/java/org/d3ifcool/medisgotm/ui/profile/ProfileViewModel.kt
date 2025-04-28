package org.d3ifcool.medisgotm.ui.profile

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.d3ifcool.medisgosh.model.User
import org.d3ifcool.medisgosh.repository.UserRepository
import org.d3ifcool.medisgosh.util.ResponseStatus


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
                },
                onError = {
                    fetchStatus.value = ResponseStatus.FAILED.apply {
                        updateMessage("Failed: ${it.message}")
                    }
                }
            )
        }
    }

    fun logout() {
        submissionStatus.value = ResponseStatus.LOADING
        viewModelScope.launch {
            submissionStatus.value = userRepository.signOut()
        }
    }

    override fun onCleared() {
        userRepository.removeListener()
        super.onCleared()
    }
}