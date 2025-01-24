package org.d3ifcool.medisgotm.ui.app

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import org.d3ifcool.medisgosh.util.SharedUtil

class AppViewModel : ViewModel() {
    val userFlow = SharedUtil.getUserFlow(viewModelScope)
}