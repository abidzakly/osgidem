package org.d3ifcool.medisgo.navigation

import android.util.Log
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.navigation
import androidx.navigation.navArgument
import com.google.firebase.auth.FirebaseUser
import org.d3ifcool.medisgo.ui.order.OrderScreen
import org.d3ifcool.medisgo.ui.order.OrderViewModel
import org.d3ifcool.medisgo.ui.profile.ProfileScreen
import org.d3ifcool.medisgo.ui.profile.ProfileViewModel
import org.d3ifcool.medisgo.ui.service.FillOrderScreen
import org.d3ifcool.medisgo.ui.service.FillOrderViewModel
import org.d3ifcool.medisgo.ui.service.ServiceDetailScreen
import org.d3ifcool.medisgo.ui.service.ServiceDetailViewModel
import org.d3ifcool.medisgo.ui.service.ServiceListScreen
import org.d3ifcool.medisgo.ui.service.ServiceListViewModel
import org.d3ifcool.medisgo.util.ViewModelFactory
import org.d3ifcool.medisgosh.component.AppContainer
import org.d3ifcool.medisgosh.navigation.KEY_ID_SERVICE
import org.d3ifcool.medisgosh.navigation.Screen
import org.d3ifcool.medisgosh.repository.OrderRepository
import org.d3ifcool.medisgosh.repository.ServiceRepository
import org.d3ifcool.medisgosh.repository.UserRepository
import org.d3ifcool.medisgosh.ui.home.HomeScreen
import org.d3ifcool.medisgosh.util.AppHelper

@Composable
fun AuthedNavGraph(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    currentUser: FirebaseUser?,
    userRepository: UserRepository,
    orderRepository: OrderRepository,
    serviceRepository: ServiceRepository,
) {
    var isHideTopBar by remember { mutableStateOf(false) }
    var isHideBottomBar by remember { mutableStateOf(false) }

    val factory =
        ViewModelFactory(
            uid = currentUser!!.uid,
            userRepository = userRepository,
            orderRepository = orderRepository,
            serviceRepository = serviceRepository
        )
    var useFab by remember { mutableStateOf(false) }
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    LaunchedEffect(currentDestination) {
        useFab = currentDestination?.route != Screen.Client.ServiceDetail.route && currentDestination?.route != Screen.General.Profile.route
    }

    AppContainer.WithBottomBar(
        useFab = useFab,
        isEmployee = false,
        isHideBottomBar = isHideBottomBar,
        navController = navController,
        modifier = modifier,
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.General.ROUTE
        ) {
            navigation(
                route = Screen.General.ROUTE,
                startDestination = Screen.General.Home.route
            ) {
                composable(Screen.General.Home.route) {
                    isHideTopBar = false
                    isHideBottomBar = false

                    HomeScreen(
                        modifier = Modifier.padding(innerPadding),
                        isEmployee = false,
                        user = currentUser,
                        navController = navController
                    ) {
                        AppHelper.navigate(navController, Screen.Client.ROUTE)
                    }
                }
                composable(Screen.General.Profile.route) {
                    val profileViewModel: ProfileViewModel = viewModel(factory = factory)
                    ProfileScreen(
                        modifier = Modifier.padding(innerPadding),
                        user = currentUser,
                        viewModel = profileViewModel
                    )
                }
            }
            navigation(
                route = Screen.Client.ROUTE,
                startDestination = Screen.Client.ServiceList.route
            ) {
                composable(
                    Screen.Client.ServiceList.route,
                ) {
                    val serviceListViewModel: ServiceListViewModel = viewModel(factory = factory)
                    ServiceListScreen(
                        modifier = Modifier.padding(innerPadding),
                        user = currentUser,
                        viewModel = serviceListViewModel,
                        navController = navController
                    )
                }
                composable(
                    Screen.Client.ServiceDetail.route,
                    arguments = listOf(
                        navArgument(KEY_ID_SERVICE) {
                            type = NavType.StringType
                        },
                    )
                ) {
                    val id = it.arguments?.getString(KEY_ID_SERVICE)
                    val newFactory = ViewModelFactory(serviceRepository = serviceRepository, doctorId = id)
                    val viewModel : ServiceDetailViewModel = viewModel(factory = newFactory)
                    ServiceDetailScreen(
                        modifier = Modifier.padding(innerPadding),
                        navController = navController,
                        viewModel = viewModel,
                        user = currentUser
                    )
                }
                composable(
                    Screen.Client.FillOrder.route,
                    arguments = listOf(
                        navArgument(KEY_ID_SERVICE) {
                            type = NavType.StringType
                        },
                    )
                ) {
                    val id = it.arguments?.getString(KEY_ID_SERVICE)
                    val newFactory = ViewModelFactory(doctorId = id, userRepository = userRepository, orderRepository = orderRepository)
                    val viewModel: FillOrderViewModel = viewModel(factory = newFactory)
                    FillOrderScreen(
                        modifier = Modifier.padding(innerPadding),
                        navController = navController,
                        viewModel = viewModel,
                        user = currentUser
                    )
                }
                composable(Screen.Client.Orders.route) {
                    isHideTopBar = false
                    isHideBottomBar = false
                    val orderViewModel: OrderViewModel = viewModel(factory = factory)
                    OrderScreen(
                        modifier = Modifier.padding(innerPadding),
                        user = currentUser,
                        navController = navController,
                        viewModel = orderViewModel,
                    )
                }
            }
        }
    }
}
