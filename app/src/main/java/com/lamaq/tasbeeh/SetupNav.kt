package com.lamaq.tasbeeh

import android.content.Context.MODE_PRIVATE
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.lamaq.tasbeeh.components.TasbeehData
import com.lamaq.tasbeeh.presentation.onboarding.OnBoardingScreen

@Composable
fun SetupNav(
    tasbeehData: TasbeehData,
    navController: NavHostController,
) {

    val onboardingPref = LocalContext.current.getSharedPreferences(
        "onboarding", MODE_PRIVATE
    )

    NavHost(
        navController = navController,
        startDestination = if (onboardingPref.getBoolean("onboardingDone", false)) "home/${tasbeehData.homeTasbeeh.elementAt(0)}" else "onboarding",
    ) { //home/${tasbeehData.homeTasbeeh.elementAt(0)}}
        composable(
            "onboarding",
        ) {
            OnBoardingScreen(
                navController = navController,
                tasbeehData = tasbeehData
            )
        }
        composable(
            "home/{tasbeehData}",
            arguments = listOf(
                navArgument("tasbeehData") { type = NavType.StringType },
            )
        ) { backStackEntry ->
            HomeScreen(
                tasbeehName =
                if (backStackEntry.arguments?.getString("tasbeehData") in tasbeehData.tasbeehTypes)
                    backStackEntry.arguments?.getString("tasbeehData") ?: ""
                else
                    tasbeehData.homeTasbeeh.elementAt(0)
                ,
                navController = navController,
                tasbeehData = tasbeehData,
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
                tasbeehData = tasbeehData
            )
        }
    }
}