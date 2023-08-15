package com.lamaq.tasbeeh

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.rememberNavController

class MainActivity : ComponentActivity() {
    private lateinit var notificationPermissionManager: NotificationPermissionManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        notificationPermissionManager = NotificationPermissionManager(this)
        setContent {
            if (notificationPermissionManager.hasPermission()) {
                StartApp()
            } else {
                NotificationPermissionDialog(
                    onPermissionGranted = {
                        notificationPermissionManager.setPermission(true)
                        recreate() // Recreate activity to invoke @Composable again
                    },
                    onPermissionDenied = {
                        notificationPermissionManager.setPermission(false)
                        recreate() // Recreate activity to invoke @Composable again
                    }
                )
            }
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

@Composable
fun NotificationPermissionDialog(
    onPermissionGranted: () -> Unit,
    onPermissionDenied: () -> Unit
) {
    LocalContext.current

    AlertDialog(
        onDismissRequest = {},
        title = {
            Text(text = "Notification Permission")
        },
        text = {
            Text(text = "Allow the app to send you notifications? We promise not to disturb you.")
        },
        confirmButton = {
            Button(
                onClick = {
                    // Request notification permission here
                    // If permission granted, call onPermissionGranted
                    onPermissionGranted()
                }
            ) {
                Text(text = "Allow", color = MaterialTheme.colorScheme.inversePrimary)
            }
        },
        dismissButton = {
            Button(
                onClick = {
                    // Call onPermissionDenied if permission is denied
                    onPermissionDenied()
                }
            ) {
                Text(text = "Deny", color = MaterialTheme.colorScheme.inversePrimary)
            }
        },
        containerColor = MaterialTheme.colorScheme.surface,
        shape = MaterialTheme.shapes.extraLarge,
        icon = { Icons.Default.Notifications },
        textContentColor = MaterialTheme.colorScheme.secondary,
        titleContentColor = MaterialTheme.colorScheme.secondary,
        properties = DialogProperties(usePlatformDefaultWidth = true),
        modifier = androidx.compose.ui.Modifier
            .padding(16.dp),
    )
}

class NotificationPermissionManager(context: Context) {

    private val sharedPreferences =
        context.getSharedPreferences("notification_permission", Context.MODE_PRIVATE)

    fun hasPermission(): Boolean {
        return sharedPreferences.getBoolean("has_permission", false)
    }

    fun setPermission(hasPermission: Boolean) {
        sharedPreferences.edit().putBoolean("has_permission", hasPermission).apply()
    }
}

