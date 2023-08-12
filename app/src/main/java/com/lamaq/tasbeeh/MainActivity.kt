package com.lamaq.tasbeeh

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.rememberNavController
import com.lamaq.tasbeeh.components.tasbeehData

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()

            SetupNav(
                tasbeehData = tasbeehData,
                navController = navController
            )
        }
    }
}