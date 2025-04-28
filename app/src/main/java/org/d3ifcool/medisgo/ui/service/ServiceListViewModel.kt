package org.d3ifcool.medisgo.ui.service

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.d3ifcool.medisgosh.model.User
import org.d3ifcool.medisgosh.repository.ServiceRepository
import org.d3ifcool.medisgosh.repository.UserRepository
import org.d3ifcool.medisgosh.util.ResponseStatus

class ServiceListViewModel(
    private val serviceRepository: ServiceRepository,
    private val userRepository: UserRepository,
    private val uid: String,
) : ViewModel() {

    var fetchedData = mutableStateOf<List<User>?>(null)
        private set

    var fetchStatus = mutableStateOf(ResponseStatus.IDLE)
        private set

    var userProfileData = mutableStateOf<User?>(null)
        private set

    init {
        observeList()
        validateUserProfile()
    }

    fun observeList() {
        viewModelScope.launch(Dispatchers.IO) {
            serviceRepository.getDoctors(onDataUpdated = {
                fetchStatus.value = ResponseStatus.SUCCESS
                fetchedData.value = it
                Log.d("ServiceListVM", "fetchedData: ${fetchStatus.value}")
            },
                onError = {
                    fetchStatus.value = ResponseStatus.FAILED.apply {
                        updateMessage("Failed: ${it.message}")
                    }
                }
            )
        }
    }

    private fun validateUserProfile() {
        viewModelScope.launch(Dispatchers.IO) {
            userProfileData.value = userRepository.getUserById(uid)
            Log.d("ServiceListVM", "fetchedUser: ${userProfileData.value}")
        }
    }

    override fun onCleared() {
        serviceRepository.removeListener()
        super.onCleared()
    }
}