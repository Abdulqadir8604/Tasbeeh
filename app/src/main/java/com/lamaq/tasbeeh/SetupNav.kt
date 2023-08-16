package com.lamaq.tasbeeh

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.lamaq.tasbeeh.components.TasbeehData
import com.lamaq.tasbeeh.components.ahlebait
import com.lamaq.tasbeeh.components.hasSub
import com.lamaq.tasbeeh.components.homeTasbeeh
import com.lamaq.tasbeeh.components.impNames
import com.lamaq.tasbeeh.components.longTasbeehs
import com.lamaq.tasbeeh.components.shortNames
import com.lamaq.tasbeeh.components.singleTasbeeh
import com.lamaq.tasbeeh.components.tasbeehTypes
@Composable
fun SetupNav(
    navController: NavHostController,
) {

    var tasbeehData by remember {
        mutableStateOf(
            TasbeehData(
                longTasbeehs = longTasbeehs,
                shortNames = shortNames,
                hasSub = hasSub,
                homeTasbeeh = homeTasbeeh,
                impNames = impNames,
                singleTasbeeh = singleTasbeeh,
                ahlebait = ahlebait,
                tasbeehTypes = tasbeehTypes
            )
        )
    }

    val settings = FirebaseFirestoreSettings.Builder()
        .setCacheSizeBytes(FirebaseFirestoreSettings.CACHE_SIZE_UNLIMITED)
        .build()
    val db = Firebase.firestore
    db.firestoreSettings = settings

    db.collection("tasbeehs786")
        .get()
        .addOnSuccessListener { result ->
            for (document in result) {
                val data = document.data

                val longTasbeehs = data["longTasbeehs"] as Map<*, *>
                val shortNames = data["shortNames"] as List<*>
                val hasSub = data["hasSub"] as Map<*, *>
                val homeTasbeeh = data["homeTasbeeh"] as List<*>
                val impNames = data["impNames"] as List<*>
                val singleTasbeeh = data["singleTasbeeh"] as List<*>
                val ahlebait = data["ahlebait"] as List<*>
                val tasbeehTypes = data["tasbeehTypes"] as List<*>

                tasbeehData = TasbeehData(
                    longTasbeehs = longTasbeehs,
                    shortNames = shortNames,
                    hasSub = hasSub,
                    homeTasbeeh = homeTasbeeh,
                    impNames = impNames,
                    singleTasbeeh = singleTasbeeh,
                    ahlebait = ahlebait,
                    tasbeehTypes = tasbeehTypes
                )
            }
        }
        .addOnFailureListener { exception ->
            Log.w("FIRESTORE", "Error getting documents.", exception)
        }

    NavHost(navController = navController, startDestination = "home/${tasbeehData.homeTasbeeh.elementAt(0)}}") {
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
            )
        }
    }
}