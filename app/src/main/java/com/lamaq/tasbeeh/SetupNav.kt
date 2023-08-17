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
        .document("tasbeehData")
        .get()
        .addOnSuccessListener { result ->
            tasbeehData = TasbeehData(
                longTasbeehs = result.data?.get("longTasbeehs") as Map<*, *>,
                shortNames = result.data?.get("shortNames") as List<*>,
                hasSub = result.data?.get("hasSub") as Map<*, *>,
                homeTasbeeh = result.data?.get("homeTasbeeh") as List<*>,
                impNames = result.data?.get("impNames") as List<*>,
                singleTasbeeh = result.data?.get("singleTasbeeh") as List<*>,
                tasbeehTypes = result.data?.get("tasbeehTypes") as List<*>,
            )
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
                tasbeehData = tasbeehData
            )
        }
    }
}