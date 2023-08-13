package com.lamaq.tasbeeh

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.outlined.Menu
import androidx.compose.material.icons.outlined.PlayArrow
import androidx.compose.material.icons.outlined.Refresh
import androidx.compose.material.icons.outlined.Settings
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
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.DisposableEffect
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.lamaq.tasbeeh.components.TasbeehCards
import com.lamaq.tasbeeh.components.ahlebait
import com.lamaq.tasbeeh.components.singleTasbeeh
import com.lamaq.tasbeeh.components.tasbeehTypes
import com.lamaq.tasbeeh.ui.theme.DarkColorScheme
import com.lamaq.tasbeeh.ui.theme.DrawerScrimColor
import com.lamaq.tasbeeh.ui.theme.LightColorScheme
import com.lamaq.tasbeeh.ui.theme.TasbeehRippleTheme
import com.lamaq.tasbeeh.ui.theme.TasbeehTheme
import kotlinx.coroutines.launch
import kotlin.system.exitProcess

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnnecessaryComposedModifier")
@Composable
fun HomeScreen(
    tasbeehData: String, navController: NavController
) {

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
    val selectedItem = remember { mutableStateOf(
        drawerPref.getString("drawer", tasbeehTypes.elementAt(0)) ?: tasbeehTypes.elementAt(0)
    ) }

    val showExitDialog = remember { mutableStateOf(false) }

    TasbeehTheme {

        if (showExitDialog.value) {
            AlertDialog(
                onDismissRequest = {
                    showExitDialog.value = false
                },
                containerColor = DarkColorScheme.background,
                title = {
                    Text(text = "Exit?")
                },
                titleContentColor = DarkColorScheme.tertiary,
                text = {
                    Text(text = "Are you sure you want to exit?")
                },
                textContentColor = DarkColorScheme.tertiary,
                confirmButton = {
                    TextButton(
                        onClick = {
                            showExitDialog.value = false
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
                ModalNavigationDrawer(
                    drawerState = drawerState,
                    gesturesEnabled = true,
                    scrimColor = DrawerScrimColor,
                    drawerContent = {
                        ModalDrawerSheet(
                            drawerShape = MaterialTheme.shapes.large,
                            drawerContainerColor = DarkColorScheme.background,
                            drawerTonalElevation = 10.dp,
                            modifier = Modifier
                                .wrapContentWidth()
                                .fillMaxHeight()
                                .padding(end = 100.dp),
                        ) {
                            Row {
                                IconButton(
                                    modifier = Modifier
                                        .padding(top = 20.dp, start = 20.dp),
                                    onClick = {

                                }) {
                                    Icon(
                                        imageVector = Icons.Outlined.Refresh,
                                        contentDescription = "Reset all counts",
                                        tint = DarkColorScheme.primary
                                    )
                                }
                                Spacer(modifier = Modifier.width(20.dp))
                                Text(
                                    text = "تسابيح",
                                    style = MaterialTheme.typography.headlineMedium,
                                    color = DarkColorScheme.tertiary,
                                    modifier = Modifier
                                        .padding(top = 20.dp)
                                )
                            }
                            Spacer(Modifier.height(12.dp))
                            tasbeehTypes.forEach { item ->

                                NavigationDrawerItem(
                                    selected = selectedItem.value == item,
                                    modifier = Modifier.padding(10.dp),
                                    icon = {
                                        Icon(
                                            imageVector = if (selectedItem.value == item) {
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
                                            text = item,
                                            style = MaterialTheme.typography.bodyLarge,
                                            color = DarkColorScheme.secondary
                                        )
                                    },
                                    onClick = {
                                        scope.launch {
                                            drawerState.close()
                                        }
                                        selectedItem.value = item
                                        drawerPref.edit().putString("drawer", item).apply()
                                        navController.navigate("home/$item") // Use the item directly
                                    },

                                    colors = NavigationDrawerItemDefaults.colors(
                                        selectedTextColor = DarkColorScheme.secondary,
                                        unselectedTextColor = DarkColorScheme.secondary,
                                        selectedContainerColor = DarkColorScheme.primary,
                                        unselectedContainerColor = DarkColorScheme.surface,
                                        selectedIconColor = DarkColorScheme.secondary,
                                        unselectedIconColor = DarkColorScheme.secondary,
                                    ),

                                    badge = {},

                                    shape = MaterialTheme.shapes.large,
                                )
                            }
                        }
                    },
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.TopEnd
                    ) {
                        Box(
                            modifier = Modifier
                                .padding(50.dp)
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
                                        .padding(start = 20.dp, top = 20.dp)
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
                                        .padding(end = 20.dp, top = 20.dp)
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
                            if (tasbeehData == "أهل البيت") {

                                    Box(
                                        modifier = Modifier
                                            .width(imageSize.dp)
                                            .height(imageSize.dp)
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
                                text = "latest updates will be shown here",
                                style = MaterialTheme.typography.bodyMedium,
                                modifier = Modifier.align(Alignment.CenterHorizontally),
                            )

                            Spacer(modifier = Modifier.height(20.dp))

                            if (tasbeehData.matches(
                                    Regex(
                                        singleTasbeeh.joinToString(
                                            separator = "|", prefix = "(", postfix = ")"
                                        )
                                    )
                                )
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
                                    TasbeehCards(
                                        tasbeehData = tasbeehData,
                                    ) { _, _ ->
                                        if (hasHaptics) haptic.performHapticFeedback(
                                            HapticFeedbackType.LongPress
                                        )
                                        visible = false
                                        navController.navigate(
                                            "tasbeeh/${tasbeehData}/${
                                                sharedPref?.getInt(
                                                    tasbeehData, 0
                                                )
                                            }"
                                        )
                                    }
                                }
                            } else if (tasbeehData == "أهل البيت") {
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
                                    LazyColumn(
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .padding(4.dp)
                                            .scrollable(
                                                state = rememberScrollableState { delta ->
                                                    imageSize += delta.toInt() / 2
                                                    imageSize = imageSize.coerceIn(150, 300)
                                                    delta
                                                },
                                                orientation = Orientation.Vertical
                                            ),
                                        verticalArrangement = Arrangement.spacedBy(4.dp),
                                        state = rememberLazyListState(),
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        content = {
                                            items(ahlebait.size) { index ->
                                                TasbeehCards(
                                                    tasbeehData = ahlebait.elementAt(index),
                                                ) { _, _ ->
                                                    if (hasHaptics) haptic.performHapticFeedback(
                                                        HapticFeedbackType.LongPress
                                                    )
                                                    visible = false
                                                    val tasbeeh = ahlebait.elementAt(index)
                                                    navController.navigate(
                                                        "tasbeeh/${tasbeeh}/${
                                                            sharedPref?.getInt(
                                                                tasbeeh,
                                                                0
                                                            )
                                                        }"
                                                    )
                                                }
                                            }
                                        }
                                    )
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
    DisposableEffect(Unit) {
        onDispose {
            drawerPref?.edit()?.clear()?.apply()
        }
    }

    BackHandler {
        showExitDialog.value = true
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    HomeScreen(
        tasbeehData = tasbeehTypes.elementAt(2), navController = rememberNavController()
    )
}
