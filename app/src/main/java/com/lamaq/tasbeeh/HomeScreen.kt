package com.lamaq.tasbeeh

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.rememberScrollableState
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.Menu
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.PlayArrow
import androidx.compose.material.icons.outlined.Refresh
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.Warning
import androidx.compose.material.ripple.LocalRippleTheme
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.app.NotificationManagerCompat
import androidx.navigation.NavController
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.ConfigUpdate
import com.google.firebase.remoteconfig.ConfigUpdateListener
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigException
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
import com.lamaq.tasbeeh.components.TasbeehCards
import com.lamaq.tasbeeh.components.TasbeehData
import com.lamaq.tasbeeh.ui.theme.DarkColorScheme
import com.lamaq.tasbeeh.ui.theme.DrawerScrimColor
import com.lamaq.tasbeeh.ui.theme.LightColorScheme
import com.lamaq.tasbeeh.ui.theme.TasbeehRippleTheme
import com.lamaq.tasbeeh.ui.theme.TasbeehTheme
import com.lamaq.tasbeeh.util.ApiService
import com.lamaq.tasbeeh.util.Welcome
import com.lamaq.tasbeeh.util.convertToArabicDigits
import com.lamaq.tasbeeh.util.fullMonthName
import com.lamaq.tasbeeh.util.getCurrentDate
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.sql.Time
import kotlin.system.exitProcess

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnnecessaryComposedModifier", "UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun HomeScreen(
    tasbeehName: Any?,
    navController: NavController,
    tasbeehData: TasbeehData
) {
    val context = LocalContext.current

    var visible by remember { mutableStateOf(false) }

    val sharedPref: SharedPreferences? = LocalContext.current.getSharedPreferences(
        "tasbeehs", Context.MODE_PRIVATE
    )
    val settingsPref = LocalContext.current.getSharedPreferences(
        "settings", Context.MODE_PRIVATE
    )
    var hasHaptics by remember { mutableStateOf(true) }
    val haptic = LocalHapticFeedback.current
    hasHaptics = settingsPref.getBoolean("hasHaptics", true)
    var showMenu by remember { mutableStateOf(false) }

    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    var imageSize by remember { mutableIntStateOf(300) }

    val drawerPref = LocalContext.current.getSharedPreferences(
        "drawer", Context.MODE_PRIVATE
    )
    val selectedItem = remember {
        mutableStateOf(
            drawerPref.getString("drawer", tasbeehData.tasbeehTypes.elementAt(0).toString())
                ?: tasbeehData.tasbeehTypes.elementAt(0).toString()
        )
    }

    val remoteConfig: FirebaseRemoteConfig = Firebase.remoteConfig
    val configSettings = remoteConfigSettings {
        minimumFetchIntervalInSeconds = 0
    }
    remoteConfig.setConfigSettingsAsync(configSettings)
    remoteConfig.setDefaultsAsync(R.xml.remote_config_defaults)

    val news by remember { mutableStateOf(remoteConfig.getString("news")) }

    val db = Firebase.firestore
    var multiTasbeehs by remember { mutableStateOf(listOf<String>()) }
    if (tasbeehData.hasSub[tasbeehName] != null) {
        db.document(
            tasbeehData.hasSub[tasbeehName].toString()
        )
            .get()
            .addOnSuccessListener { document ->
                document.data?.forEach { (_, value) ->
                    multiTasbeehs =
                        value as List<String>
                }
            }
    }

    val showExitDialog = remember { mutableStateOf(false) }
    var resetAllCounts by remember { mutableStateOf(false) }

    val snackbarHostState = remember { SnackbarHostState() }

    var showNotifAlert by remember { mutableStateOf(false) }
    var showRationalAlertforNotif by remember { mutableStateOf(false) }
    val askedForNotiPer = settingsPref.getBoolean("askedForNotiPer", false)

    val notificationManager = NotificationManagerCompat.from(LocalContext.current)
    val areNotificationsEnabled = notificationManager.areNotificationsEnabled()

    var dateString by remember { mutableStateOf("") }
    var hDay by remember { mutableStateOf("") }
    var hYear by remember { mutableStateOf("") }

    if (!askedForNotiPer) {
        if (!areNotificationsEnabled) {
            showNotifAlert = true
        }
    }

    if (showNotifAlert) {
        settingsPref.edit().putBoolean("askedForNotiPer", true).apply()
        AlertDialog(
            onDismissRequest = {
                showNotifAlert = false
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        showNotifAlert = false
                        val intent = Intent()
                        intent.action = "android.settings.APP_NOTIFICATION_SETTINGS"
                        intent.putExtra("android.provider.extra.APP_PACKAGE", context.packageName)
                        context.startActivity(intent)
                    }
                ) {
                    Text("Enable", color = DarkColorScheme.inversePrimary)
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        showNotifAlert = false
                        showRationalAlertforNotif = true
                    }
                ) {
                    Text("Dismiss", color = DarkColorScheme.inversePrimary)
                }
            },
            containerColor = DarkColorScheme.background,
            icon = {
                Icon(
                    imageVector = Icons.Outlined.Notifications,
                    contentDescription = "notifications",
                    tint = DarkColorScheme.inversePrimary,
                    modifier = Modifier.size(30.dp)
                )
            },
            title = {
                Text(text = "Enable Notifications")
            },
            titleContentColor = DarkColorScheme.secondary,
            text = {
                Text(text = "Please enable notifications to receive updates. We promise not to spam you :)")
            },
            textContentColor = DarkColorScheme.secondary,
        )
    }

    if (showRationalAlertforNotif) {
        AlertDialog(
            onDismissRequest = {
                showRationalAlertforNotif = false
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        showRationalAlertforNotif = false
                    }
                ) {
                    Text("Okay", color = DarkColorScheme.inversePrimary)
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        showRationalAlertforNotif = false
                    }
                ) {
                    Text("Dismiss", color = DarkColorScheme.inversePrimary)
                }
            },
            containerColor = DarkColorScheme.background,
            icon = {
                Icon(
                    imageVector = Icons.Outlined.Notifications,
                    contentDescription = "notifications",
                    tint = DarkColorScheme.inversePrimary,
                    modifier = Modifier.size(30.dp)
                )
            },
            title = {
                Text(text = "Enable Notifications through device settings on your own whenever you want")
            },
            titleContentColor = DarkColorScheme.secondary,
            text = {
                Text(text = "Notifications are required to receive important updates and announcements!!")
            },
            textContentColor = DarkColorScheme.secondary,
        )
    }


    val retrofit = Retrofit.Builder()
        .baseUrl("https://api.aladhan.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val apiService = retrofit.create(ApiService::class.java)

    val call = apiService.convertToHijri(getCurrentDate())
    call.enqueue(object : retrofit2.Callback<Welcome> {
        override fun onResponse(call: Call<Welcome>, response: Response<Welcome>) {
            if (response.isSuccessful) {
                val welcomeResponse = response.body()
                val month = fullMonthName[welcomeResponse?.data?.hijri?.month?.number.toString()]
                val day = convertToArabicDigits(welcomeResponse?.data?.hijri?.day?.toInt()?.plus(1).toString())
                val year = convertToArabicDigits(welcomeResponse?.data?.hijri?.year.toString())
                val hijriDate =
                    if (Time(System.currentTimeMillis()).hours > 19 || Time(System.currentTimeMillis()).hours < 6) {
                        // meaning it is night
                        "$day رات $month $year"
                    } else {
                        "$day $month $year"
                    }
                dateString = hijriDate
            } else {
                println(response.errorBody())
            }
        }

        override fun onFailure(call: Call<Welcome>, t: Throwable) {
            dateString = "internet needed"
        }
    })


    TasbeehTheme {

        remoteConfig.addOnConfigUpdateListener(object : ConfigUpdateListener {
            override fun onUpdate(configUpdate: ConfigUpdate) {

                if (configUpdate.updatedKeys.contains("news")) {
                    remoteConfig.activate().addOnCompleteListener {
                        remoteConfig.getString("news")
                    }
                }
            }

            override fun onError(error: FirebaseRemoteConfigException) {
                println("Error: ${error.message}")
            }
        })

        if (resetAllCounts) {
            AlertDialog(
                onDismissRequest = {
                    showExitDialog.value = false
                },
                containerColor = DarkColorScheme.background,
                icon = {
                    Icon(
                        imageVector = Icons.Outlined.Warning,
                        contentDescription = "Reset all counts",
                        tint = DarkColorScheme.tertiary,
                        modifier = Modifier.size(30.dp)
                    )
                },
                title = {
                    Text(text = "Reset All Counts?")
                },
                titleContentColor = DarkColorScheme.secondary,
                text = {
                    Text(text = "This will lose all your counts without any backup. Are you sure?")
                },
                textContentColor = DarkColorScheme.secondary,
                confirmButton = {
                    TextButton(
                        onClick = {
                            sharedPref?.edit()?.clear()?.apply()
                            resetAllCounts = false
                        }
                    ) {
                        Text("Reset All", color = DarkColorScheme.tertiary)
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = {
                            resetAllCounts = false
                        }
                    ) {
                        Text("Noooo!!", color = DarkColorScheme.inversePrimary)
                    }
                }
            )
        }

        if (showExitDialog.value) {
            AlertDialog(
                onDismissRequest = {
                    showExitDialog.value = false
                },
                containerColor = DarkColorScheme.background,
                title = {
                    Text(text = "Exit?")
                },
                titleContentColor = DarkColorScheme.secondary,
                text = {
                    Text(text = "Are you sure you want to exit?")
                },
                textContentColor = DarkColorScheme.secondary,
                confirmButton = {
                    TextButton(
                        onClick = {
                            showExitDialog.value = false
                            drawerPref?.edit()?.clear()?.apply()
                            exitProcess(0)
                        }
                    ) {
                        Text("Confirm", color = DarkColorScheme.inversePrimary)
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = {
                            showExitDialog.value = false
                        }
                    ) {
                        Text("Dismiss", color = DarkColorScheme.inversePrimary)
                    }
                }
            )
        }

        CompositionLocalProvider(
            LocalRippleTheme provides TasbeehRippleTheme,
        ) {
            Surface(
                color = if (isSystemInDarkTheme()) DarkColorScheme.surface else LightColorScheme.surface,
                modifier = Modifier.fillMaxSize(),
            ) {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    containerColor = if (isSystemInDarkTheme()) DarkColorScheme.background else LightColorScheme.background,
                    snackbarHost = { SnackbarHost(snackbarHostState) },
                ) {
                    ModalNavigationDrawer(
                        drawerState = drawerState,
                        gesturesEnabled = true,
                        scrimColor = DrawerScrimColor,
                        drawerContent = {
                            ModalDrawerSheet(
                                drawerShape = RoundedCornerShape(
                                    topStart = 0.dp,
                                    topEnd = 100.dp,
                                    bottomStart = 0.dp,
                                    bottomEnd = 100.dp
                                ),
                                drawerContainerColor = DarkColorScheme.background,
                                drawerTonalElevation = 10.dp,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .fillMaxHeight()
                                    .padding(start = 0.dp, end = 40.dp),
                            ) {
                                Box(
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    IconButton(
                                        modifier = Modifier
                                            .padding(top = 10.dp, start = 20.dp)
                                            .align(Alignment.TopStart),
                                        onClick = {
                                            resetAllCounts = true
                                        }) {
                                        Icon(
                                            imageVector = Icons.Outlined.Refresh,
                                            contentDescription = "Reset all counts",
                                            tint = DarkColorScheme.secondary
                                        )
                                    }
                                    Text(
                                        text = "تسابيح",
                                        style = MaterialTheme.typography.headlineMedium,
                                        color = DarkColorScheme.tertiary,
                                        modifier = Modifier
                                            .padding(top = 10.dp)
                                            .align(Alignment.TopCenter)
                                    )
                                    IconButton(
                                        modifier = Modifier
                                            .padding(top = 10.dp, end = 40.dp)
                                            .align(Alignment.TopEnd),
                                        onClick = {
                                            scope.launch {
                                                drawerState.close()
                                            }
                                        }) {
                                        Icon(
                                            imageVector = Icons.Outlined.Close,
                                            contentDescription = "Close Menu",
                                            tint = DarkColorScheme.secondary
                                        )
                                    }
                                }
                                Spacer(Modifier.height(12.dp))
                                Text(
                                    text = dateString,
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = DarkColorScheme.primary,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(top = 20.dp)
                                        .background(
                                            DarkColorScheme.secondary,
                                        ),
                                    textAlign = TextAlign.Center
                                )
                                Spacer(Modifier.height(12.dp))
                                LazyColumn(
                                    modifier = Modifier
                                        .fillMaxSize(),
                                    verticalArrangement = Arrangement.Top,
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ){
                                    items(1) {
                                        tasbeehData.tasbeehTypes.forEach { item ->
                                            NavigationDrawerItem(
                                                selected = tasbeehName == item,
                                                modifier = Modifier
                                                    .padding(
                                                        start = 0.dp,
                                                        end = 30.dp,
                                                        top = 10.dp,
                                                        bottom = 10.dp
                                                    )
                                                    .fillMaxWidth(),
                                                icon = {
                                                    Icon(
                                                        imageVector = if (tasbeehName == item) {
                                                            Icons.Filled.PlayArrow
                                                        } else {
                                                            Icons.Outlined.PlayArrow
                                                        },
                                                        contentDescription = "icon",
                                                        tint = DarkColorScheme.secondary
                                                    )
                                                },
                                                label = {
                                                    Text(
                                                        text = item.toString(),
                                                        style = MaterialTheme.typography.headlineSmall,
                                                        color = DarkColorScheme.secondary
                                                    )
                                                },
                                                onClick = {
                                                    scope.launch {
                                                        drawerState.close()
                                                    }
                                                    selectedItem.value = item.toString()
                                                    drawerPref.edit()
                                                        .putString("drawer", item.toString())
                                                        .apply()
                                                    navController.navigate("home/$item")
                                                },

                                                colors = NavigationDrawerItemDefaults.colors(
                                                    selectedTextColor = DarkColorScheme.secondary,
                                                    unselectedTextColor = DarkColorScheme.secondary,
                                                    selectedContainerColor = DarkColorScheme.primary,
                                                    unselectedContainerColor = DarkColorScheme.surface,
                                                    selectedIconColor = DarkColorScheme.secondary,
                                                    unselectedIconColor = DarkColorScheme.secondary,
                                                ),

                                                badge = {
                                                    if (tasbeehData.singleTasbeeh.contains(item)) {
                                                        val count =
                                                            sharedPref?.getInt(item.toString(), 0)
                                                        if (count != null) {
                                                            if (count > 0) {
                                                                Text(
                                                                    text = count.toString(),
                                                                    style = MaterialTheme.typography.bodyMedium,
                                                                    color = DarkColorScheme.secondary
                                                                )
                                                            }
                                                        }
                                                    } else {
                                                        Icon(
                                                            imageVector = Icons.Outlined.MoreVert,
                                                            tint = DarkColorScheme.secondary,
                                                            contentDescription = "More Tasbeehs"
                                                        )
                                                    }
                                                },

                                                shape = RoundedCornerShape(
                                                    topStart = 0.dp,
                                                    topEnd = 30.dp,
                                                    bottomStart = 0.dp,
                                                    bottomEnd = 30.dp
                                                ),
                                            )
                                        }
                                    }
                                }
                                Spacer(Modifier.height(12.dp))
                                Box(
                                    modifier = Modifier.fillMaxSize()
                                ) {
                                    Column(
                                        modifier = Modifier
                                            .fillMaxSize(),
                                        verticalArrangement = Arrangement.Bottom,
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        Text(
                                            text = "v${
                                                LocalContext.current.packageManager.getPackageInfo(
                                                    LocalContext.current.packageName,
                                                    0
                                                ).versionName
                                            }",
                                            style = MaterialTheme.typography.bodyMedium,
                                            color = DarkColorScheme.secondary,
                                            modifier = Modifier
                                                .padding(bottom = 20.dp)
                                        )
                                    }
                                }
                            }
                        },
                    ) {
                        Box(
                            modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.TopEnd
                        ) {
                            Box(
                                modifier = Modifier
                                    .padding(60.dp)
                                    .align(Alignment.TopEnd),
                            ) {
                                DropdownMenu(
                                    expanded = showMenu,
                                    onDismissRequest = { showMenu = false },
                                    modifier = Modifier
                                        .align(Alignment.TopEnd)
                                        .background(
                                            DarkColorScheme.primary,
                                        ),
                                ) {
                                    DropdownMenuItem(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .wrapContentSize(Alignment.CenterStart),
                                        onClick = {
                                            hasHaptics = !hasHaptics
                                        },

                                        text = {
                                            Row(
                                                verticalAlignment = Alignment.CenterVertically,
                                            ) {
                                                Text(
                                                    text = "Vibrate",
                                                    style = MaterialTheme.typography.bodyMedium,
                                                    modifier = Modifier.padding(end = 10.dp),
                                                    color = DarkColorScheme.secondary,

                                                    )
                                                Switch(
                                                    checked = hasHaptics,
                                                    onCheckedChange = {
                                                        hasHaptics = it
                                                        with(settingsPref.edit()) {
                                                            putBoolean("hasHaptics", hasHaptics)
                                                            apply()
                                                        }
                                                    },
                                                    modifier = Modifier
                                                        .padding(start = 10.dp)
                                                        .height(20.dp),
                                                    colors = SwitchDefaults.colors(
                                                        checkedThumbColor = DarkColorScheme.primary,
                                                        uncheckedThumbColor = DarkColorScheme.secondary,
                                                        uncheckedTrackColor = DarkColorScheme.primary,
                                                        checkedTrackColor = DarkColorScheme.secondary
                                                    ),
                                                )
                                            }
                                        },
                                    )
                                }
                            }
                            Column(
                                modifier = Modifier.fillMaxSize(),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Box(
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    IconButton(
                                        onClick = {
                                            scope.launch {
                                                drawerState.open()
                                            }
                                        },
                                        modifier = Modifier
                                            .padding(start = 20.dp, top = 40.dp)
                                            .background(
                                                DarkColorScheme.secondary,
                                                MaterialTheme.shapes.extraLarge
                                            )

                                    ) {
                                        Icon(
                                            imageVector = Icons.Outlined.Menu,
                                            contentDescription = "Menu",
                                            tint = DarkColorScheme.primary,
                                            modifier = Modifier.size(30.dp)
                                        )
                                    }
                                    IconButton(
                                        onClick = { showMenu = true },
                                        modifier = Modifier
                                            .padding(end = 20.dp, top = 40.dp)
                                            .align(Alignment.TopEnd)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Outlined.Settings,
                                            modifier = Modifier.size(30.dp),
                                            contentDescription = "Settings",
                                            tint = DarkColorScheme.secondary,
                                        )
                                    }
                                }
                                if (!tasbeehData.singleTasbeeh.contains(tasbeehName)) {
                                    Box(
                                        modifier = Modifier
                                            .width(imageSize.dp)
                                            .height(imageSize.dp)
                                            .align(Alignment.CenterHorizontally)
                                            .padding(top = 8.dp, bottom = 20.dp)
                                            .background(Color.Transparent)
                                            .animateContentSize(
                                                animationSpec = tween(
                                                    durationMillis = 1000,
                                                    easing = FastOutSlowInEasing
                                                )
                                            )
                                    ) {
                                        Image(
                                            painter = painterResource(id = R.drawable.ashara),
                                            contentDescription = "Ashara Mubarakah",
                                            modifier = Modifier
                                                .fillMaxSize()
                                                .padding(20.dp)
                                                .animateContentSize()
                                        )
                                    }
                                } else {
                                    Box(
                                        modifier = Modifier
                                            .width(300.dp)
                                            .height(300.dp)
                                            .align(Alignment.CenterHorizontally)
                                            .padding(top = 8.dp, bottom = 20.dp)
                                            .background(Color.Transparent)
                                    ) {
                                        Image(
                                            painter = painterResource(id = R.drawable.ashara),
                                            contentDescription = "Ashara Mubarakah",
                                            modifier = Modifier
                                                .fillMaxSize()
                                                .padding(20.dp)
                                        )
                                    }
                                }
                                Text(
                                    text = news,
                                    style = MaterialTheme.typography.bodyMedium,
                                    modifier = Modifier.align(Alignment.CenterHorizontally),
                                )

                                Spacer(modifier = Modifier.height(20.dp))

                                if (tasbeehData.singleTasbeeh.contains(tasbeehName)) {
                                    AnimatedVisibility(
                                        visible = visible,
                                        enter = slideInVertically(
                                            initialOffsetY = { 400 }, animationSpec = tween(
                                                durationMillis = 1000, easing = FastOutSlowInEasing
                                            )
                                        ),
                                        exit = slideOutVertically(
                                            targetOffsetY = { 400 }, animationSpec = tween(
                                                durationMillis = 1000, easing = FastOutSlowInEasing
                                            )
                                        ),
                                    ) {
                                        TasbeehCards(
                                            tasbeehName = tasbeehName.toString(),
                                            fieldName = tasbeehName.toString(),
                                            tasbeehData = tasbeehData,
                                        ) { _, _ ->
                                            if (hasHaptics) haptic.performHapticFeedback(
                                                HapticFeedbackType.LongPress
                                            )
                                            visible = false
                                            navController.navigate(
                                                "tasbeeh/${tasbeehName}/${
                                                    sharedPref?.getInt(
                                                        tasbeehName.toString(), 0
                                                    )
                                                }"
                                            )
                                        }
                                    }
                                } else if (!tasbeehData.singleTasbeeh.contains(tasbeehName)
                                ) {
                                    AnimatedVisibility(
                                        visible = visible,
                                        enter = slideInVertically(
                                            initialOffsetY = { 400 }, animationSpec = tween(
                                                durationMillis = 1000, easing = FastOutSlowInEasing
                                            )
                                        ),
                                        exit = slideOutVertically(
                                            targetOffsetY = { 400 }, animationSpec = tween(
                                                durationMillis = 1000, easing = FastOutSlowInEasing
                                            )
                                        ),
                                    ) {
                                        LazyVerticalStaggeredGrid(
                                            modifier = Modifier
                                                .fillMaxSize()
                                                .padding(4.dp)
                                                .scrollable(
                                                    rememberScrollableState { delta ->
                                                        imageSize += delta.toInt()
                                                        imageSize = imageSize.coerceIn(200, 300)
                                                        delta
                                                    },
                                                    Orientation.Vertical
                                                ),
                                            state = rememberLazyStaggeredGridState(),
                                            columns = StaggeredGridCells.Adaptive(150.dp)
                                        ) {
                                            items(multiTasbeehs.size) { index ->
                                                val item = multiTasbeehs[index]
                                                TasbeehCards(
                                                    tasbeehName = item,
                                                    fieldName = tasbeehName.toString(),
                                                    tasbeehData = tasbeehData,
                                                ) { _, _ ->
                                                    if (hasHaptics) haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                                    visible = false
                                                    navController.navigate(
                                                        "tasbeeh/${item}/${sharedPref?.getInt(item, 0)}"
                                                    )
                                                }
                                            }
                                        }
                                    }
                                } else {
                                    Text(
                                        text = "No data found",
                                        style = MaterialTheme.typography.bodyMedium,
                                        modifier = Modifier.align(Alignment.CenterHorizontally),
                                    )
                                }
                            }
                            LaunchedEffect(true) {
                                visible = true
                            }
                        }
                    }
                }
            }
        }
    }

    BackHandler {
        if (tasbeehName == tasbeehData.homeTasbeeh.elementAt(0)) {
            showExitDialog.value = true
        } else {
            navController.navigate("home/${tasbeehData.homeTasbeeh.elementAt(0)}")
            drawerPref.edit().putString("drawer", tasbeehData.homeTasbeeh.elementAt(0).toString())
                .apply()
        }
    }
}