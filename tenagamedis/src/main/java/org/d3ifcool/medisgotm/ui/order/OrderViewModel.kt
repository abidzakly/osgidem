package org.d3ifcool.medisgotm.ui.order

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.d3ifcool.medisgosh.model.Order
import org.d3ifcool.medisgosh.util.ResponseStatus
import org.d3ifcool.medisgosh.repository.OrderRepository

class OrderViewModel(
    private val orderRepository: OrderRepository,
) : ViewModel() {

    var fetchedOrders = mutableStateOf<List<Order>?>(null)
        private set

    var fetchStatus = mutableStateOf(ResponseStatus.IDLE)
        private set

    var submissionStatus = mutableStateOf(ResponseStatus.IDLE)
        private set

    init {
        observeOrders()
    }

    fun observeOrders() {
        fetchStatus.value = ResponseStatus.LOADING
        viewModelScope.launch(Dispatchers.IO) {
            orderRepository.getOrders(
                isEmployee = true,
                onDataUpdated = { list ->
                    if (!list.isNullOrEmpty()) {
                        fetchedOrders.value = list.filter { it.status == "ONGOING" }
                        fetchStatus.value = ResponseStatus.SUCCESS
                    } else {
                        fetchStatus.value = ResponseStatus.FAILED
                    }
                },
                onError = {
                    fetchStatus.value = ResponseStatus.FAILED.apply {
                        updateMessage(it.message)
                    }
                }
            )
        }
    }

    fun markAsDone(order: Order) {
        submissionStatus.value = ResponseStatus.LOADING
        viewModelScope.launch(Dispatchers.IO) {
            submissionStatus.value = orderRepository.markAsDone(order)
        }
    }

    fun reset() {
        submissionStatus.value = ResponseStatus.IDLE
    }

    override fun onCleared() {
        orderRepository.removeListener()
        super.onCleared()
    }
}