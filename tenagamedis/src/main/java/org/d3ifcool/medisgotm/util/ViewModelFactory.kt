package org.d3ifcool.medisgotm.util

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import org.d3ifcool.medisgosh.repository.OrderRepository
import org.d3ifcool.medisgosh.repository.UserRepository
import org.d3ifcool.medisgosh.ui.auth.ForgotPasswordViewModel
import org.d3ifcool.medisgosh.ui.auth.LoginViewModel
import org.d3ifcool.medisgotm.ui.order.OrderViewModel
import org.d3ifcool.medisgotm.ui.profile.ProfileViewModel

class ViewModelFactory(
    private val uid: String? = null,
    private val orderId: String? = null,
    private val userRepository: UserRepository? = null,
    private val orderRepository: OrderRepository? = null,
) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProfileViewModel::class.java)) {
            return ProfileViewModel(userRepository!!) as T
        } else if (modelClass.isAssignableFrom(OrderViewModel::class.java)) {
            return OrderViewModel(orderRepository!!) as T
        } else if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            return LoginViewModel(userRepository!!) as T
        } else if (modelClass.isAssignableFrom(ForgotPasswordViewModel::class.java)) {
            return ForgotPasswordViewModel(userRepository!!) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}