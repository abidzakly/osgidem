package org.d3ifcool.medisgosh.model

import com.google.firebase.Timestamp

data class Order(
    val doctor: User? = null,
    val client: User? = null,
    val description: String? = null,
    val supportingImageUrl: String? = null,
    val status: String? = null,
    val appointmentTime: Timestamp? = null,
    val paymentReceiptUrl: String? = null,
    val createdAt: Timestamp? = Timestamp.now(),
    val rating: String? = null,
    val id: String? = null
) {
    constructor() : this(null)

    companion object {
        const val COLLECTION = "orders"
        const val STORAGE_KELUHAN = "foto_keluhan"
        const val STORAGE_RECEIPT = "bukti_pembayaran"
        const val STATUS_UNPAID = "UNPAID"
        const val STATUS_ONGOING = "ONGOING"
        const val STATUS_COMPLETE = "COMPLETE"
        const val FIELD_EMPLOYEE_ID = "doctor.id"
        const val FIELD_CLIENT_ID = "client.id"
        const val FIELD_CREATED_AT = "createdAt"
    }
}
