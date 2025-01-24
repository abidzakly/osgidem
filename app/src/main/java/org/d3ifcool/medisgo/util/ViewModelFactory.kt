package org.d3ifcool.medisgo.util

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import org.d3ifcool.medisgo.ui.order.OrderViewModel
import org.d3ifcool.medisgo.ui.profile.ProfileViewModel
import org.d3ifcool.medisgo.ui.register.RegisterViewModel
import org.d3ifcool.medisgo.ui.service.FillOrderViewModel
import org.d3ifcool.medisgo.ui.service.ServiceDetailViewModel
import org.d3ifcool.medisgo.ui.service.ServiceListViewModel
import org.d3ifcool.medisgosh.repository.OrderRepository
import org.d3ifcool.medisgosh.repository.ServiceRepository
import org.d3ifcool.medisgosh.repository.UserRepository
import org.d3ifcool.medisgosh.ui.auth.ForgotPasswordViewModel
import org.d3ifcool.medisgosh.ui.auth.LoginViewModel

class ViewModelFactory(
    private val uid: String? = null,
    private val orderId: String? = null,
    private val doctorId: String? = null,
    private val userRepository: UserRepository? = null,
    private val orderRepository: OrderRepository? = null,
    private val serviceRepository: ServiceRepository? = null,
) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProfileViewModel::class.java)) {
            return ProfileViewModel(userRepository!!) as T
        } else if (modelClass.isAssignableFrom(OrderViewModel::class.java)) {
            return OrderViewModel(orderRepository!!) as T
        } else if (modelClass.isAssignableFrom(ForgotPasswordViewModel::class.java)) {
            return ForgotPasswordViewModel(userRepository!!) as T
        } else if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            return LoginViewModel(userRepository!!) as T
        } else if (modelClass.isAssignableFrom(RegisterViewModel::class.java)) {
            return RegisterViewModel(userRepository!!) as T
        } else if (modelClass.isAssignableFrom(ServiceListViewModel::class.java)) {
            return ServiceListViewModel(serviceRepository!!, userRepository!!, uid!!) as T
        } else if (modelClass.isAssignableFrom(ServiceDetailViewModel::class.java)) {
            return ServiceDetailViewModel(serviceRepository!!, doctorId!!) as T
        } else if (modelClass.isAssignableFrom(FillOrderViewModel::class.java)) {
            return FillOrderViewModel(userRepository = userRepository!!, orderRepository = orderRepository!!, doctorId!!) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}