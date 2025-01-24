package org.d3ifcool.medisgosh.ui.auth

import android.content.Intent
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.firebase.ui.auth.AuthUI
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.d3ifcool.medisgosh.repository.UserRepository
import org.d3ifcool.medisgosh.util.AppObjectState

class LoginViewModel(
    private val userRepository: UserRepository,
) : ViewModel() {
    var status = mutableStateOf(AppObjectState.IDLE)
        private set

    fun signIn(username: String, password: String) {
        status.value = AppObjectState.LOADING
        viewModelScope.launch(Dispatchers.IO) {
            status.value = userRepository.signIn(username, password)
        }
    }

    fun getSignInIntent(): Intent {
        return AuthUI.getInstance().createSignInIntentBuilder()
            .setAvailableProviders(arrayListOf(AuthUI.IdpConfig.GoogleBuilder().build())).build()
    }
}