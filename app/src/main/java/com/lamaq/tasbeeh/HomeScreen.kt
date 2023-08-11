package com.lamaq.tasbeeh

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.lamaq.tasbeeh.components.TasbeehCards
import com.lamaq.tasbeeh.ui.theme.LightColorScheme
import com.lamaq.tasbeeh.ui.theme.ColorScheme
import com.lamaq.tasbeeh.ui.theme.TasbeehTheme

@Composable
fun HomeScreen(
    tasbeehData: Map<String, Int>,
    navController: NavController
)
{
    val hasHaptics by remember { mutableStateOf(true) }
    val haptic = LocalHapticFeedback.current
    var showMenu by remember { mutableStateOf(false) }
    TasbeehTheme {
        Surface(
            color = if (isSystemInDarkTheme()) ColorScheme.surface else LightColorScheme.surface,
            modifier = Modifier.fillMaxSize()
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.TopCenter // Align content to the top right corner
            ) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .width(400.dp)
                            .height(400.dp)
                            .align(Alignment.CenterHorizontally)
                            .padding(top = 50.dp)
                            .background(Color.Transparent) // Set background color to Transparent
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.ashara),
                            contentDescription = "Ashara Mubarakah",
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(60.dp)
                        )
                    }
                    Text(
                        text = "latest updates will be shown here",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    LazyRow(
                        modifier = Modifier
                            .fillMaxSize(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        state = rememberLazyListState(),
                    ) {
                        items(tasbeehData.size) { index ->
                            TasbeehCards(
                                tasbeehData.entries.elementAt(index)
                            ) {_, _ ->
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