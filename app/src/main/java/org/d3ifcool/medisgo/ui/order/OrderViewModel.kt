package org.d3ifcool.medisgo.ui.order

import android.graphics.Bitmap
import android.util.Log
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

    private val originalFetchedOrders = mutableStateOf<List<Order>?>(null)

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
                isEmployee = false,
                onDataUpdated = { list ->
                    if (!list.isNullOrEmpty()) {
                        fetchStatus.value = AppObjectState.SUCCESS
                        originalFetchedOrders.value = list.sortedByDescending { it.createdAt }
                        filterData(0)
                        Log.d("OrderRepo", "Fetched Data: ${fetchedOrders.value}")
                    } else {
                        fetchStatus.value = AppObjectState.FAILED
                    }
                },
                onError = {
                    fetchStatus.value = AppObjectState.FAILED.apply {
                        updateMessage(it.message)
                    }
                    Log.e("HomeVM", "Error fetching files: ${it.message}", it)
                }
            )
        }
    }

    fun cancelOrder(order: Order) {
        submissionStatus.value = AppObjectState.LOADING
        viewModelScope.launch(Dispatchers.IO) {
            submissionStatus.value = orderRepository.cancelOrder(order)
        }
    }

    fun submitPayment(order: Order, bitmap: Bitmap) {
        submissionStatus.value = AppObjectState.LOADING
        viewModelScope.launch(Dispatchers.IO) {
            submissionStatus.value = orderRepository.submitPayment(order, bitmap)
        }
    }

    fun filterData(selectedTabIndex: Int) {
        when (selectedTabIndex) {
            0 -> {
                fetchedOrders.value = originalFetchedOrders.value?.filter { it.status == "UNPAID" }
            }

            1 -> {
                fetchedOrders.value = originalFetchedOrders.value?.filter { it.status == "ONGOING" }
            }

            2 -> {
                fetchedOrders.value =
                    originalFetchedOrders.value?.filter { it.status == "COMPLETE" }
            }
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