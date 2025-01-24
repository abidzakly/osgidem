package org.d3ifcool.medisgosh.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import org.d3ifcool.medisgosh.R
import org.d3ifcool.medisgosh.navigation.Screen
import org.d3ifcool.medisgosh.ui.theme.NavBarGradientTopColor
import org.d3ifcool.medisgosh.util.AppHelper


data class NavItems(
    val label: String,
    val icon: Int,
    val selectedIcon: Int,
    val route: String
)

val listOfNavClient = listOf(
    NavItems(
        label = "Home",
        icon = R.drawable.home_nav_ico,
        selectedIcon = R.drawable.home_nav_ico_selected,
        route = Screen.General.Home.route
    ),
    NavItems(
        label = "Layanan",
        icon = R.drawable.layanan_nav_ico,
        selectedIcon = R.drawable.layanan_nav_ico_selected,
        route = Screen.Client.ServiceList.route
    ),
    NavItems(
        label = "Order",
        icon = R.drawable.order_nav_ico,
        selectedIcon = R.drawable.order_nav_ico_selected,
        route = Screen.Client.Orders.route
    ),
    NavItems(
        label = "Profile",
        icon = R.drawable.profile_nav_ico,
        selectedIcon = R.drawable.profile_nav_ico_selected,
        route = Screen.General.Profile.route
    )
)

val listOfNavEmployee = listOf(
    NavItems(
        label = "Home",
        icon = R.drawable.home_nav_ico,
        selectedIcon = R.drawable.home_nav_ico_selected,
        route = Screen.General.Home.route
    ),
    NavItems(
        label = "Antrian",
        icon = R.drawable.order_nav_ico,
        selectedIcon = R.drawable.order_nav_ico_selected,
        route = Screen.Employee.ROUTE
    ),
    NavItems(
        label = "Profile",
        icon = R.drawable.profile_nav_ico,
        selectedIcon = R.drawable.profile_nav_ico_selected,
        route = Screen.General.Profile.route
    )
)

@Composable
fun AppBottomBar(navController: NavHostController, isEmployee: Boolean) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                Brush.verticalGradient(
                    startY = -570f,
                    colors = listOf(
                        NavBarGradientTopColor,
                        Color.White.copy(alpha = .6f)
                    )
                )
            )
    ) {
        NavigationBar(
            containerColor = Color.Transparent
        ) {
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentDestination = navBackStackEntry?.destination
            val navList by remember { mutableStateOf(if (isEmployee) listOfNavEmployee else listOfNavClient) }
            navList.forEach { navItems ->
                val isSelected =
                    currentDestination?.hierarchy?.any { it.route == navItems.route } == true
                NavigationBarItem(
                    selected = isSelected,
                    onClick = {
                        AppHelper.navigate(navController, navItems.route)
                    },
                    icon = {
                        Icon(
                            modifier = Modifier.size(24.dp),
                            painter = painterResource(if (isSelected) navItems.selectedIcon else navItems.icon),
                            contentDescription = null,
                            tint = Color.Unspecified
                        )
                    },
                    label = {
                        AppText.Small12(
                            text = navItems.label,
                            color = Color.Gray,
                            fontWeight = FontWeight.Bold
                        )
                    },
                    colors = NavigationBarItemDefaults.colors(
                        indicatorColor = Color.Transparent,
                    )
                )
            }
        }
    }
}
