package com.lamaq.tasbeeh

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument

@Composable
fun SetupNav(
    tasbeehData: Map<String, Int>,
    navController: NavHostController
) {
    NavHost(navController = navController, startDestination = "tasbeeh/ya Hussain/"+786) {
        composable("home") {
            HomeScreen(
                tasbeehData = tasbeehData,
                navController = navController,
            )
        }
        composable(
            "tasbeeh/{tasbeehName}/{tasbeehCount}",
            arguments = listOf(
                navArgument("tasbeehName") { type = NavType.StringType },
                navArgument("tasbeehCount") { type = NavType.IntType },
            )
        ) { backStackEntry ->
            TasbeehScreen(
                tasbeehName = backStackEntry.arguments?.getString("tasbeehName") ?: "",
                navController = navController,
            )
        }
    }
}