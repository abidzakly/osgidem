package org.d3ifcool.medisgosh.model

data class User(
    val name: String? = null,
    val email: String? = null,
    val photoUrl: String? = null,
    val dateOfBirth: String? = null,
    val address: String? = null,
    val gender: String? = null,
    val phoneNumber: String? = null,
    val role: String? = null,
    val employeeData: Employee? = null,
    val id: String? = null,
) {
    constructor() : this( null)

    companion object {
        const val COLLECTION = "users"
        const val SUBCOL_EMPLOYEE = "employeeData"
        const val CLIENT_ROLE = "client"
        const val EMPLOYEE_ROLE = "employee"
    }
}