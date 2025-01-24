package org.d3ifcool.medisgosh.navigation

const val KEY_ID_SERVICE = "serviceId"
const val KEY_ID_ORDER = "orderId"

sealed class Screen(val route: String) {
    sealed class Auth(route: String) : Screen(route) {
        companion object {
            const val ROUTE = "auth"
        }
        data object Register : General("register")
        data object Login : General("login")
        data object ForgotForm : General("forgot-password/form")
        data object ForgotSuccess : General("forgot-password/success")
    }
    sealed class General(route: String) : Screen(route) {
        companion object {
            const val ROUTE = "general"
        }
        data object Home : General("home")
        data object Profile : General("profile")
    }

    sealed class Client(route: String) : Screen(route) {
        companion object {
            const val ROUTE = "client"
        }
        data object ServiceList : Client("client/services")
        data object ServiceDetail : Client("client/services/{$KEY_ID_SERVICE}/detail") {
            fun withData(id: String) = "client/services/$id/detail"
        }
        data object FillOrder : Client("client/services/{$KEY_ID_SERVICE}/order") {
            fun withData(id: String) = "client/services/$id/order"
        }
        data object Orders : Screen("client/orders")
    }

    sealed class Employee(route: String) : Screen(route) {
        companion object {
            const val ROUTE = "employee"
        }
        data object Orders : Employee("employee/orders")
        data object OrderDetail : Employee("employee/orders/{$KEY_ID_ORDER}/detail") {
            fun withData(id: String) = "employee/orders/$id/detail"
        }
    }
}