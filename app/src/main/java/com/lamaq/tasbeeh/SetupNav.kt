package com.lamaq.tasbeeh

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.lamaq.tasbeeh.components.homeTasbeeh
import com.lamaq.tasbeeh.components.tasbeehTypes
@Composable
fun SetupNav(
    navController: NavHostController,
) {

    NavHost(navController = navController, startDestination = "home/${homeTasbeeh.elementAt(0)}}") {
        composable(
            "home/{tasbeehData}",
            arguments = listOf(
                navArgument("tasbeehData") { type = NavType.StringType },
            )
        ) { backStackEntry ->
            HomeScreen(
                tasbeehData =
                    if (backStackEntry.arguments?.getString("tasbeehData") in tasbeehTypes)
                        backStackEntry.arguments?.getString("tasbeehData") ?: ""
                    else
                        homeTasbeeh.elementAt(0)
                ,
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