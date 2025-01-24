package org.d3ifcool.medisgosh.ui.auth

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.d3ifcool.medisgosh.repository.UserRepository
import org.d3ifcool.medisgosh.util.AppObjectState

class ForgotPasswordViewModel(
    private val userRepository: UserRepository
) : ViewModel() {

    var status = mutableStateOf(AppObjectState.IDLE)
        private set

    fun submitEmail(email: String) {
        status.value = AppObjectState.LOADING
        viewModelScope.launch(Dispatchers.IO) {
            status.value = userRepository.submitForgotPassword(email)
        }
    }
}