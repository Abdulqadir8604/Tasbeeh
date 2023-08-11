package com.lamaq.tasbeeh

import android.content.Context
import android.content.SharedPreferences
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import com.lamaq.tasbeeh.components.TasbeehCards
import com.lamaq.tasbeeh.ui.theme.DarkColorScheme
import com.lamaq.tasbeeh.ui.theme.LightColorScheme
import com.lamaq.tasbeeh.ui.theme.TasbeehTheme

@Composable
fun HomeScreen(
    tasbeehData: Map<String, Int>,
    navController: NavController
) {
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
    TasbeehTheme {
        Surface(
            color = if (isSystemInDarkTheme()) DarkColorScheme.surface else LightColorScheme.surface,
            modifier = Modifier.fillMaxSize()
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.TopEnd
            ) {
                Box(
                    modifier = Modifier
                        .padding(50.dp)
                        .align(Alignment.TopEnd)
                        .wrapContentSize()
                        .background(
                            if (isSystemInDarkTheme()) DarkColorScheme.primary else LightColorScheme.primary,
                            shape = MaterialTheme.shapes.large
                        ),
                    content = {
                        DropdownMenu(
                            expanded = showMenu,
                            onDismissRequest = { showMenu = false },
                            modifier = Modifier
                                .background(
                                    if (isSystemInDarkTheme()) DarkColorScheme.primary else LightColorScheme.primary,
                                    shape = MaterialTheme.shapes.large
                                )
                        ) {
                            DropdownMenuItem(
                                text = {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier.padding(10.dp)
                                    ) {
                                        Text(
                                            text = "Haptics",
                                            style = MaterialTheme.typography.bodyMedium,
                                            modifier = Modifier.padding(end = 10.dp),
                                            color = Color.DarkGray
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
                                            modifier = Modifier.padding(start = 10.dp),
                                            colors = SwitchDefaults.colors(
                                                checkedThumbColor = DarkColorScheme.primary,
                                                uncheckedThumbColor = DarkColorScheme.secondary,
                                                uncheckedTrackColor = DarkColorScheme.primary,
                                                checkedTrackColor = DarkColorScheme.secondary
                                            ),

                                            )
                                    }
                                },
                                onClick = {
                                    showMenu = false
                                },
                            )
                        }
                    }
                )
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    IconButton(
                        onClick = { showMenu = true },
                        modifier = Modifier
                            .align(Alignment.End)
                            .padding(end = 20.dp, top = 20.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Settings,
                            contentDescription = "Settings",
                            tint = DarkColorScheme.secondary
                        )
                    }
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
                    Text(
                        text = "latest updates will be shown here",
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                    LazyRow(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(top = 4.dp),
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        state = rememberLazyListState(),
                    ) {
                        items(tasbeehData.size) { index ->
                            TasbeehCards(
                                tasbeehData.entries.elementAt(index)
                            ) { _, _ ->
                                if (hasHaptics) haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                val tasbeeh = tasbeehData.entries.elementAt(index)
                                navController.navigate(
                                    "tasbeeh/${tasbeeh.key}/${
                                        sharedPref?.getInt(
                                            tasbeeh.key,
                                            0
                                        )
                                    }"
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    HomeScreen(
        mapOf("Tasbeeh" to 1000),
        navController = NavController(LocalContext.current)
    )
}