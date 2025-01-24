package org.d3ifcool.medisgotm.ui.order

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.d3ifcool.medisgosh.model.Order
import org.d3ifcool.medisgosh.util.AppObjectState
import org.d3ifcool.medisgosh.repository.OrderRepository

class OrderViewModel(
    private val orderRepository: OrderRepository,
) : ViewModel() {

    var fetchedOrders = mutableStateOf<List<Order>?>(null)
        private set

    var fetchStatus = mutableStateOf(AppObjectState.IDLE)
        private set

    var submissionStatus = mutableStateOf(AppObjectState.IDLE)
        private set

    init {
        observeOrders()
    }

    fun observeOrders() {
        fetchStatus.value = AppObjectState.LOADING
        viewModelScope.launch(Dispatchers.IO) {
            orderRepository.getOrders(
                isEmployee = true,
                onDataUpdated = { list ->
                    if (!list.isNullOrEmpty()) {
                        fetchedOrders.value = list.filter { it.status == "ONGOING" }
                        fetchStatus.value = AppObjectState.SUCCESS
                    } else {
                        fetchStatus.value = AppObjectState.FAILED
                    }
                },
                onError = {
                    fetchStatus.value = AppObjectState.FAILED.apply {
                        updateMessage(it.message)
                    }
                }
            )
        }
    }

    fun markAsDone(order: Order) {
        submissionStatus.value = AppObjectState.LOADING
        viewModelScope.launch(Dispatchers.IO) {
            submissionStatus.value = orderRepository.markAsDone(order)
        }
    }

    fun reset() {
        submissionStatus.value = AppObjectState.IDLE
    }

    override fun onCleared() {
        orderRepository.removeListener()
        super.onCleared()
    }
}