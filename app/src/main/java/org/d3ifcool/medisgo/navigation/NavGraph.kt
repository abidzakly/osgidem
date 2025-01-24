package org.d3ifcool.medisgo.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import org.d3ifcool.medisgo.ui.register.RegisterScreen
import org.d3ifcool.medisgo.ui.register.RegisterViewModel
import org.d3ifcool.medisgosh.navigation.Screen
import org.d3ifcool.medisgosh.repository.UserRepository
import org.d3ifcool.medisgosh.ui.auth.ForgotPasswordScreen
import org.d3ifcool.medisgosh.ui.auth.ForgotPasswordViewModel
import org.d3ifcool.medisgosh.ui.auth.LoginScreen
import org.d3ifcool.medisgosh.ui.auth.LoginViewModel
import org.d3ifcool.medisgosh.ui.auth.SubmitTicketSuccess
import org.d3ifcool.medisgo.util.ViewModelFactory

@Composable
fun NavGraph(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    userRepository: UserRepository,
) {
    val factory = ViewModelFactory(userRepository = userRepository)
    val registerViewModel: RegisterViewModel = viewModel(factory = factory)
    val loginViewModel: LoginViewModel = viewModel(factory = factory)
    val forgotPasswordViewModel: ForgotPasswordViewModel = viewModel(factory = factory)
    NavHost(
        navController = navController,
        startDestination = Screen.Auth.ROUTE
    ) {
        navigation(
            route = Screen.Auth.ROUTE,
            startDestination = Screen.Auth.Register.route
        ) {
            composable(Screen.Auth.Register.route) {
                RegisterScreen(
                    modifier = modifier,
                    viewModel = registerViewModel
                ) {
                    navController.navigate(Screen.Auth.Login.route) {
                        popUpTo(Screen.Auth.Login.route) {
                            inclusive = true
                        }
                    }
                }
            }
            composable(Screen.Auth.Login.route) {
                LoginScreen(
                    modifier = modifier,
                    isEmployee = false,
                    viewModel = loginViewModel,
                    onSignUp = {
                        navController.navigate(Screen.Auth.Register.route)
                    },
                    onForgotPassword = {
                        navController.navigate(Screen.Auth.ForgotForm.route)
                    }
                )
            }
            composable(Screen.Auth.ForgotForm.route) {
                ForgotPasswordScreen(
                    modifier = modifier,
                    isEmployee = true,
                    viewModel = forgotPasswordViewModel,
                    onBackToSignIn = {
                        navController.navigate(Screen.Auth.Login.route) {
                            popUpTo(Screen.Auth.Login.route) {
                                inclusive = true
                            }
                        }
                    }
                ) {
                    navController.navigate(Screen.Auth.ForgotSuccess.route)
                }
            }
            composable(Screen.Auth.ForgotSuccess.route) {
                SubmitTicketSuccess(
                    modifier = modifier
                ) {
                    navController.navigate(Screen.Auth.Login.route)
                }
            }
        }
    }
}