package org.d3ifcool.medisgosh.util

enum class AppObjectState(var message: String? = null) {
    IDLE, LOADING, SUCCESS, FAILED;

    fun updateMessage(message: String? = null) {
        this.message = message
    }
}