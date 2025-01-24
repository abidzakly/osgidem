package org.d3ifcool.medisgotm.navigation

import android.util.Log
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.navigation
import androidx.navigation.navArgument
import com.google.firebase.auth.FirebaseUser
import org.d3ifcool.medisgosh.component.AppContainer
import org.d3ifcool.medisgosh.navigation.KEY_ID_ORDER
import org.d3ifcool.medisgosh.navigation.Screen
import org.d3ifcool.medisgosh.repository.OrderRepository
import org.d3ifcool.medisgosh.repository.UserRepository
import org.d3ifcool.medisgosh.ui.home.HomeScreen
import org.d3ifcool.medisgosh.util.AppHelper
import org.d3ifcool.medisgotm.ui.order.OrderScreen
import org.d3ifcool.medisgotm.ui.order.OrderViewModel
import org.d3ifcool.medisgotm.ui.profile.ProfileScreen
import org.d3ifcool.medisgotm.ui.profile.ProfileViewModel
import org.d3ifcool.medisgotm.util.ViewModelFactory

@Composable
fun AuthedNavGraph(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    currentUser: FirebaseUser?,
    userRepository: UserRepository,
    orderRepository: OrderRepository,
) {
    var isHideTopBar by remember { mutableStateOf(false) }
    var isHideBottomBar by remember { mutableStateOf(false) }

    val factory =
        ViewModelFactory(
            uid = currentUser!!.uid,
            userRepository = userRepository,
            orderRepository = orderRepository
        )

    var useFab by remember { mutableStateOf(false) }
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    LaunchedEffect(currentDestination) {
        useFab = currentDestination?.route != Screen.General.Profile.route
    }

    AppContainer.WithBottomBar(
        useFab = useFab,
        isEmployee = true,
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
                        isEmployee = true,
                        user = currentUser,
                        navController = navController
                    ) {
                        AppHelper.navigate(navController, Screen.Employee.ROUTE)
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
                route = Screen.Employee.ROUTE,
                startDestination = Screen.Employee.Orders.route
            ) {
                composable(Screen.Employee.Orders.route) {
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
