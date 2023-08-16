package com.lamaq.tasbeeh

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.rememberNavController

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        setContent {
            StartApp()
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        val drawerPref = getSharedPreferences("drawer", MODE_PRIVATE)
        drawerPref.edit().clear().apply()
    }
}

@Composable
fun StartApp() {
    val navController = rememberNavController()
    SetupNav(navController = navController)
}