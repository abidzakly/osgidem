package org.d3ifcool.medisgo.ui.service

import android.graphics.Bitmap
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.d3ifcool.medisgosh.model.Order
import org.d3ifcool.medisgosh.model.User
import org.d3ifcool.medisgosh.repository.OrderRepository
import org.d3ifcool.medisgosh.repository.UserRepository
import org.d3ifcool.medisgosh.util.ResponseStatus

class FillOrderViewModel(
    private val userRepository: UserRepository,
    private val orderRepository: OrderRepository,
    private val doctorId: String,
    private val orderId: String? = null,
) : ViewModel() {

    var fetchedDoctor = mutableStateOf<User?>(null)
        private set

    var submitStatus = mutableStateOf(ResponseStatus.IDLE)
        private set

    init {
        getDoctorById()
    }

    private fun getDoctorById() {
        viewModelScope.launch(Dispatchers.IO) {
            fetchedDoctor.value = userRepository.getUserById(doctorId)
        }
    }

    fun submitOrder(order: Order, bitmap: Bitmap) {
        submitStatus.value = ResponseStatus.LOADING
        viewModelScope.launch(Dispatchers.IO) {
            submitStatus.value = orderRepository.submitOrder(order, bitmap)
        }
    }

    fun reset() {
        submitStatus.value = ResponseStatus.IDLE
    }
}