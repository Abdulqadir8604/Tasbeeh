package com.lamaq.tasbeeh

import android.content.Context
import android.content.Context.VIBRATOR_SERVICE
import android.content.SharedPreferences
import android.media.AudioAttributes
import android.media.SoundPool
import android.os.VibrationEffect
import android.os.Vibrator
import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Refresh
import com.lamaq.tasbeeh.ui.theme.ColorScheme
import com.lamaq.tasbeeh.ui.theme.LightColorScheme
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.lamaq.tasbeeh.components.shortNames
import com.lamaq.tasbeeh.ui.theme.TasbeehTheme

var sharedPref: SharedPreferences? = null
@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun TasbeehScreen(
    tasbeehName: String,
    navController: NavController,
){
    sharedPref = LocalContext.current.getSharedPreferences(
        "tasbeehs",
        Context.MODE_PRIVATE
    )
    val settingsPref = LocalContext.current.getSharedPreferences(
        "settings",
        Context.MODE_PRIVATE
    )

    var showMenu by remember { mutableStateOf(false) }
    var total_counter by remember { mutableStateOf(0) }
    var counter by remember { mutableStateOf(0) }
    total_counter = sharedPref?.getInt(tasbeehName, 0)!!

    var hasHaptics by remember { mutableStateOf(true) }
    hasHaptics = settingsPref.getBoolean("hasHaptics", true)
    var hasSound by remember { mutableStateOf(true) }
    hasSound = settingsPref.getBoolean("hasSound", true)

    var showEditDialog by remember { mutableStateOf(false) }
    var showLimitDialog by remember { mutableStateOf(false) }

    val default_limit = 2000000000

    var limit by remember { mutableStateOf(0) }
    var limitReached by remember { mutableStateOf(false) }

    val context = LocalContext.current

    val vibrator = context.getSystemService(VIBRATOR_SERVICE) as Vibrator
    val vibrationEffect = VibrationEffect.createOneShot(65, VibrationEffect.DEFAULT_AMPLITUDE)

    lateinit var soundPool: SoundPool

    val audioAttributes = AudioAttributes.Builder()
        .setUsage(AudioAttributes.USAGE_ASSISTANCE_SONIFICATION)
        .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
        .build()

    soundPool = SoundPool.Builder()
        .setMaxStreams(1)
        .setAudioAttributes(audioAttributes)
        .build()

    // Load the sound effect from the raw resource
    val soundId: Int = soundPool.load(context, R.raw.click, 1)

    TasbeehTheme {
        // A surface container using the 'background' color from the theme
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = if (isSystemInDarkTheme()) ColorScheme.surface else LightColorScheme.surface,
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
                            .clickable(
                                onClick = {
                                    if (hasSound && !limitReached) {
                                        soundPool.play(soundId, 1.0f, 1.0f, 1, 0, 1.0f)
                                    }
                                    try {
                                        if (limit > 0) {
                                            if (counter < limit && !limitReached) {
                                                counter++
                                                with (sharedPref!!.edit()) {
                                                    putInt(tasbeehName, total_counter + counter)
                                                    apply()
                                                }
                                            }
                                            else {
                                                limitReached = true
                                                Toast.makeText(
                                                    context,
                                                    "Limit Reached, kindly reset the counter",
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                            }
                                        } else {
                                            if (counter < default_limit && !limitReached) {
                                                counter++
                                                with (sharedPref!!.edit()) {
                                                    putInt(tasbeehName, total_counter + counter)
                                                    apply()
                                                }
                                            }
                                            else {
                                                limitReached = true
                                                Toast.makeText(
                                                    context,
                                                    "Limit Reached, kindly reset the counter",
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                            }
                                        }
                                    } catch (_: Exception) {

                                    }
                                    if (hasHaptics && !limitReached) {
                                        vibrator.vibrate(vibrationEffect)
                                    }
                                },
                                indication = null,
                                interactionSource = MutableInteractionSource(),
                            )
                    ) {
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = if (shortNames.contains(tasbeehName))
                                    tasbeehName
                                else
                                    when (tasbeehName) {
                                        "صلوات" -> {
                                            "أللهم صل على محمد و على أل محمد وبارك وسلم"
                                        }

                                        else -> {
                                            tasbeehName
                                        }
                                    },
                                style = if (!shortNames.contains(tasbeehName))
                                    MaterialTheme.typography.headlineSmall
                                else
                                    MaterialTheme.typography.headlineLarge,
                                modifier = if (!shortNames.contains(tasbeehName))
                                    Modifier.padding(top = 60.dp)
                                else
                                    Modifier.padding(top = 40.dp),
                                color = ColorScheme.tertiaryContainer,
                                textAlign = TextAlign.Center,
                            )
                            val textSizeStyle = TextStyle(
                                color = ColorScheme.onSecondaryContainer,
                                fontSize = 60.sp,
                                lineHeight = 48.sp,
                                textAlign = TextAlign.Center,

                                )
                            Text(
                                text = counter.toString(),
                                modifier = if (!shortNames.contains(tasbeehName)) {
                                    Modifier
                                        .padding(top = 40.dp)
                                        .fillMaxWidth(1f)
                                } else {
                                    Modifier
                                        .padding(top = 80.dp)
                                        .fillMaxWidth(1f)
                                },
                                color = ColorScheme.onSecondaryContainer,
                                style = textSizeStyle,
                                maxLines = 1,
                            )
                            Text(
                                text = "Limit: ${
                                    if (limit > 0)
                                        limit.toString()
                                    else
                                        "Unlimited"
                                }",
                                modifier = Modifier.padding(16.dp),
                                color = ColorScheme.onSecondaryContainer,
                                style = MaterialTheme.typography.bodyMedium,
                                maxLines = 1,
                            )
                            Spacer(modifier = Modifier.padding(40.dp))
                            Text(
                                text = "+1",
                                modifier = Modifier.padding(top = 80.dp),
                                style = MaterialTheme.typography.headlineMedium,
                                color = if (isSystemInDarkTheme()) ColorScheme.secondaryContainer else LightColorScheme.secondaryContainer,
                                fontSize = MaterialTheme.typography.headlineLarge.fontSize,
                                fontWeight = MaterialTheme.typography.headlineLarge.fontWeight,
                            )
                        }
                        Row(
                            modifier = Modifier
                                .padding(bottom = 50.dp)
                                .align(Alignment.BottomCenter),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            IconButton(
                                onClick = {
                                    if (counter <= 0) {
                                        counter = 0
                                    } else {
                                        counter--
                                        with (sharedPref!!.edit()) {
                                            putInt(tasbeehName, total_counter + counter)
                                            apply()
                                        }
                                    }
                                },
                                modifier = Modifier
                                    .padding(10.dp)
                                    .background(
                                        color = ColorScheme.secondaryContainer,
                                        shape = MaterialTheme.shapes.extraLarge
                                    )
                                    .size(50.dp)
                            ) {
                                Icon(
                                    imageVector = ImageVector.vectorResource(id = R.drawable.neg1),
                                    contentDescription = "-1",
                                    tint = ColorScheme.onSecondaryContainer,
                                    modifier = Modifier.size(30.dp)
                                )
                            }
                            IconButton(
                                onClick = {
                                    counter = 0
                                },
                                modifier = Modifier
                                    .padding(10.dp)
                                    .background(
                                        color = ColorScheme.secondaryContainer,
                                        shape = MaterialTheme.shapes.extraLarge
                                    )
                                    .size(50.dp),
                            ) {
                                Icon(
                                    imageVector = Icons.Outlined.Refresh,
                                    contentDescription = "Reset",
                                    tint = ColorScheme.onSecondaryContainer,
                                    modifier = Modifier
                                        .size(30.dp)
                                        .combinedClickable(
                                            onClick = {
                                                counter = 0
                                            },
                                            onLongClick = {
                                                limit = 0
                                                limitReached = false
                                            }
                                        )
                                )
                            }
                            IconButton(
                                onClick = {
                                    showEditDialog = true
                                },
                                modifier = Modifier
                                    .padding(10.dp)
                                    .background(
                                        color = ColorScheme.secondaryContainer,
                                        shape = MaterialTheme.shapes.extraLarge
                                    )
                                    .size(50.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Outlined.Edit,
                                    contentDescription = "Edit",
                                    tint = ColorScheme.onSecondaryContainer,
                                    modifier = Modifier.size(30.dp)
                                )
                            }
                            IconButton(
                                onClick = {
                                    showLimitDialog = true
                                },
                                modifier = Modifier
                                    .padding(10.dp)
                                    .background(
                                        color = ColorScheme.secondaryContainer,
                                        shape = MaterialTheme.shapes.extraLarge
                                    )
                                    .size(50.dp)
                            ) {
                                Icon(
                                    imageVector = ImageVector.vectorResource(id = R.drawable.limit),
                                    contentDescription = "Limit",
                                    tint = ColorScheme.onSecondaryContainer,
                                    modifier = Modifier.size(30.dp)
                                )
                            }
                        }
                    }
                }


            }
        }
    }
    DisposableEffect(Unit) {
        onDispose {
            soundPool.release()
        }
    }
}