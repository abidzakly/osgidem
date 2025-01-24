package org.d3ifcool.medisgo.ui.service

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.d3ifcool.medisgosh.model.User
import org.d3ifcool.medisgosh.repository.ServiceRepository

class ServiceDetailViewModel(
    private val serviceRepository: ServiceRepository,
    private val doctorId: String
) : ViewModel() {

    var fetchedData = mutableStateOf<User?>(null)
        private set

    init {
        getDoctorById()
    }

    fun getDoctorById() {
        viewModelScope.launch(Dispatchers.IO) {
            fetchedData.value = serviceRepository.getDoctorById(doctorId)
        }
    }

}