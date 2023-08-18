package com.lamaq.tasbeeh

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import androidx.navigation.compose.rememberNavController
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.lamaq.tasbeeh.components.TasbeehData

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        installSplashScreen()
        setContent {

            var tasbeehData by remember {
                mutableStateOf(
                    TasbeehData(
                        longTasbeehs = mapOf("" to ""),
                        shortNames = listOf(""),
                        hasSub = mapOf("" to ""),
                        homeTasbeeh = listOf("تسبیح"),
                        impNames = listOf(""),
                        singleTasbeeh = listOf(""),
                        tasbeehTypes = listOf(""),
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

            val navController = rememberNavController()
            SetupNav(
                tasbeehData = tasbeehData,
                navController = navController
            )
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        val drawerPref = getSharedPreferences("drawer", MODE_PRIVATE)
        drawerPref.edit().clear().apply()
    }
}