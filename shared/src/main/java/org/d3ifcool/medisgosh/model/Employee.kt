package org.d3ifcool.medisgosh.model

data class Employee(
    val type: String,
    val accountName: String,
    val bankName: String,
    val accountNumber: String,
    val strNumber: String,
) {
    constructor() : this("", "", "", "", "")

    companion object {
        const val SUBCOLLECTION = "employee_data"
        const val ROLE = "employee"
    }
}
