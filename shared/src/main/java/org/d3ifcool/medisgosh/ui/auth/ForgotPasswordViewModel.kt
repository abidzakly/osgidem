package org.d3ifcool.medisgosh.ui.auth

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.d3ifcool.medisgosh.repository.UserRepository
import org.d3ifcool.medisgosh.util.ResponseStatus

class ForgotPasswordViewModel(
    private val userRepository: UserRepository
) : ViewModel() {

    var status = mutableStateOf(ResponseStatus.IDLE)
        private set

    fun submitEmail(email: String) {
        status.value = ResponseStatus.LOADING
        viewModelScope.launch(Dispatchers.IO) {
            status.value = userRepository.submitForgotPassword(email)
        }
    }

    fun resetStatus() {
        status.value = ResponseStatus.IDLE
    }

    override fun onCleared() {
        super.onCleared()
        status.value = ResponseStatus.IDLE
    }
}