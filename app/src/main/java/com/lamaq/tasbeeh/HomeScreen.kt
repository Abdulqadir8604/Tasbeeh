package com.lamaq.tasbeeh

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.ArrowForward
import androidx.compose.material.icons.outlined.KeyboardArrowLeft
import androidx.compose.material.icons.outlined.KeyboardArrowRight
import androidx.compose.material.icons.outlined.Menu
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.DrawerDefaults
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
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import androidx.navigation.NavController
import com.lamaq.tasbeeh.components.TasbeehCards
import com.lamaq.tasbeeh.ui.theme.DarkColorScheme
import com.lamaq.tasbeeh.ui.theme.DrawerScrimColor
import com.lamaq.tasbeeh.ui.theme.LightColorScheme
import com.lamaq.tasbeeh.ui.theme.TasbeehTheme
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnnecessaryComposedModifier")
@Composable
fun HomeScreen(
    tasbeehData: Set<String>,
    navController: NavController
) {

    var visible by remember { mutableStateOf(false) }

    val sharedPref: SharedPreferences? = LocalContext.current.getSharedPreferences(
        "tasbeehs",
        Context.MODE_PRIVATE
    )
    val settingsPref = LocalContext.current.getSharedPreferences(
        "settings",
        Context.MODE_PRIVATE
    )
    var hasHaptics by remember { mutableStateOf(true) }
    val haptic = LocalHapticFeedback.current
    hasHaptics = settingsPref.getBoolean("hasHaptics", true)
    var showMenu by remember { mutableStateOf(false) }

    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    // icons to mimic drawer destinations
    val items: Map<String, ImageVector> = mapOf(
        "Sample Item #1" to Icons.Default.Favorite,
        "Sample Item #2" to Icons.Default.Face,
        "Sample Item #3" to Icons.Default.Email
    )

    var selectedItem by remember { mutableStateOf(items.entries.first()) }

    TasbeehTheme {
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
                        Text(
                            text = "تسبیح",
                            style = MaterialTheme.typography.headlineMedium,
                            color = DarkColorScheme.tertiary,
                            modifier = Modifier
                                .padding(top = 20.dp)
                                .align(Alignment.CenterHorizontally)
                        )
                        Spacer(Modifier.height(12.dp))
                        items.forEach { item ->
                            NavigationDrawerItem(
                                modifier = Modifier
                                    .padding(10.dp),
                                icon = {
                                    Icon(
                                        imageVector = item.value,
                                        contentDescription = item.key,
                                        tint = DarkColorScheme.secondary
                                    )
                                },
                                label = {
                                    Text(
                                        text = item.key,
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = DarkColorScheme.secondary
                                    )
                                },
                                onClick = {
//                                    scope.launch { drawerState.close() }
                                    selectedItem = item
                                },

                                selected = items == selectedItem,

                                colors = NavigationDrawerItemDefaults.colors(
                                    selectedTextColor = DarkColorScheme.secondary,
                                    unselectedTextColor = DarkColorScheme.secondary,
                                    selectedContainerColor = DarkColorScheme.surface,
                                    unselectedContainerColor = DarkColorScheme.primary,
                                    selectedIconColor = DarkColorScheme.secondary,
                                    unselectedIconColor = DarkColorScheme.secondary,
                                ),

                                badge = {
                                    if (item.key == "Favorites") {
                                        Text(
                                            text = "2",
                                            style = MaterialTheme.typography.bodyMedium,
                                            color = DarkColorScheme.secondary
                                        )
                                    }
                                },

                                shape = MaterialTheme.shapes.medium,
                            )
                        }
                    }
                },
                content = {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.TopEnd
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
                                modifier = Modifier
                                    .fillMaxWidth()
                            )
                            {
                                IconButton(
                                    onClick = { scope.launch { drawerState.open() } },
                                    modifier = Modifier
                                        .padding(start = 20.dp, top = 20.dp)
                                        .align(Alignment.TopStart)
                                        .background(
                                            DarkColorScheme.secondary,
                                            shape = MaterialTheme.shapes.extraLarge
                                        )
                                ) {
                                    Icon(
                                        imageVector = Icons.Outlined.KeyboardArrowRight,
                                        contentDescription = "Menu",
                                        tint = DarkColorScheme.primary,
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
                            AnimatedVisibility(
                                visible = visible,
                                enter = fadeIn(
                                    animationSpec = tween(
                                        durationMillis = 1000,
                                        easing = LinearEasing
                                    )
                                ) + slideInVertically(
                                    initialOffsetY = { -40 },
                                    animationSpec = tween(
                                        durationMillis = 1000,
                                        easing = FastOutSlowInEasing
                                    )
                                ),
                            ) {
                                Box(
                                    modifier = Modifier
                                        .width(350.dp)
                                        .height(350.dp)
                                        .align(Alignment.CenterHorizontally)
                                        .padding(top = 8.dp, bottom = 20.dp)
                                        .background(Color.Transparent)
                                ) {
                                    Image(
                                        painter = painterResource(id = R.drawable.ashara),
                                        contentDescription = "Ashara Mubarakah",
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .padding(53.dp)
                                    )
                                }
                            }
                            Text(
                                text = "latest updates will be shown here",
                                style = MaterialTheme.typography.bodyMedium,
                                modifier = Modifier.align(Alignment.CenterHorizontally),
                            )
                            LazyRow(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(top = 4.dp),
                                horizontalArrangement = Arrangement.spacedBy(6.dp),
                                state = rememberLazyListState(),
                            ) {
                                items(tasbeehData.size) { index ->
                                    TasbeehCards(
                                        tasbeehData = tasbeehData.elementAt(index),
                                    ) { _, _ ->
                                        if (hasHaptics) haptic.performHapticFeedback(
                                            HapticFeedbackType.LongPress
                                        )
                                        val tasbeeh = tasbeehData.elementAt(index)
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
                        }
                        LaunchedEffect(true) {
                            visible = true
                        }
                    }
                }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    HomeScreen(
        tasbeehData = setOf(
            "صلوات",
            "يا حسين",
            "يا علي",
            "يا فاطمة",
            "يا حسن",
            "يا زينب",
            "يا عباس",
            "يا محمد",
            "يا طيب"
        ),
        navController = NavController(LocalContext.current)
    )
}