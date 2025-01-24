package org.d3ifcool.medisgotm.ui.app

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.firestore
import com.google.firebase.storage.storage
import org.d3ifcool.medisgosh.repository.OrderRepository
import org.d3ifcool.medisgosh.repository.UserRepository
import org.d3ifcool.medisgotm.navigation.AuthedNavGraph
import org.d3ifcool.medisgotm.navigation.NavGraph

@Composable
fun EmployeeApp(
    modifier: Modifier = Modifier,
) {
    val navController = rememberNavController()
    val viewModel: AppViewModel = viewModel()
    val currentUser by viewModel.userFlow.collectAsState()

    val db = Firebase.firestore
    val storage = Firebase.storage
    val auth = FirebaseAuth.getInstance()
    val userRepository = UserRepository(db, storage, auth)

    if (currentUser == null) {
        NavGraph(modifier = modifier, navController = navController,
            userRepository = userRepository,
        )
    }

    currentUser?.let {
        val orderRepository = OrderRepository(db, auth, it.uid, storage)
        AuthedNavGraph(
            modifier = modifier,
            navController = navController,
            currentUser = it,
            userRepository = userRepository,
            orderRepository = orderRepository,
        )
    }
}