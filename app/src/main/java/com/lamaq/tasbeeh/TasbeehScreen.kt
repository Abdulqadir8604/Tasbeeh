package com.lamaq.tasbeeh

import android.annotation.SuppressLint
import android.content.Context
import android.content.Context.VIBRATOR_SERVICE
import android.content.SharedPreferences
import android.media.AudioAttributes
import android.media.SoundPool
import android.os.VibrationEffect
import android.os.Vibrator
import android.speech.tts.TextToSpeech
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.Done
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.KeyboardArrowLeft
import androidx.compose.material.icons.outlined.Refresh
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import com.lamaq.tasbeeh.components.TasbeehData
import com.lamaq.tasbeeh.ui.theme.DarkColorScheme
import com.lamaq.tasbeeh.ui.theme.LightColorScheme
import com.lamaq.tasbeeh.ui.theme.TasbeehTheme
import com.lamaq.tasbeeh.util.LoadImageFromUrl
import kotlinx.coroutines.launch
import java.util.Locale

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "UnrememberedMutableInteractionSource")
@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun TasbeehScreen(
    tasbeehName: String,
    navController: NavController,
    tasbeehData: TasbeehData,
) {
    val context = LocalContext.current

    var tts: TextToSpeech? = null

    var visible by remember { mutableStateOf(false) }

    val sharedPref: SharedPreferences? = LocalContext.current.getSharedPreferences(
        "tasbeehs",
        Context.MODE_PRIVATE
    )
    val settingsPref = LocalContext.current.getSharedPreferences(
        "settings",
        Context.MODE_PRIVATE
    )

    var showMenu by remember { mutableStateOf(false) }
    var totalCounter by remember { mutableIntStateOf(0) }
    totalCounter = sharedPref?.getInt(tasbeehName, 0)!!

    var counter by remember { mutableIntStateOf(0) }

    var hasHaptics by remember { mutableStateOf(true) }
    hasHaptics = settingsPref.getBoolean("hasHaptics", true)
    var hasSound by remember { mutableStateOf(true) }
    hasSound = settingsPref.getBoolean("hasSound", true)

    var showEditDialog by remember { mutableStateOf(false) }
    var showLimitDialog by remember { mutableStateOf(false) }

    val defaultLimit = 2000000000

    var limit by remember { mutableIntStateOf(0) }
    var limitReached by remember { mutableStateOf(false) }

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

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    val colorScheme = if (isSystemInDarkTheme()) DarkColorScheme else LightColorScheme

    TasbeehTheme {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .wrapContentSize(),
            color = colorScheme.surface,
        ) {
            Scaffold(
                modifier = Modifier.fillMaxSize(),
                containerColor = colorScheme.background,
                snackbarHost = {
                    SnackbarHost(snackbarHostState) { data ->
                        val buttonColor = ButtonDefaults.textButtonColors(
                            contentColor = colorScheme.secondary
                        )
                        Snackbar(
                            modifier = Modifier
                                .padding(12.dp)
                                .border(
                                    2.dp,
                                    colorScheme.secondary,
                                    MaterialTheme.shapes.large
                                ),
                            containerColor = colorScheme.primary,
                            shape = MaterialTheme.shapes.large,
                            actionContentColor = colorScheme.inversePrimary,
                            contentColor = colorScheme.secondary,
                            action = {
                                TextButton(
                                    onClick = {
                                        limitReached = false
                                        limit = 0
                                    },
                                    colors = buttonColor
                                ) { Text("Reset Limit") }
                            }
                        ) {
                            Text(data.visuals.message)
                        }
                    }
                },
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.TopCenter
                ) {
                    // drop down menu
                    Box(
                        modifier = Modifier
                            .padding(60.dp)
                            .align(Alignment.TopEnd)
                            .background(
                                color = colorScheme.primary,
                                shape = MaterialTheme.shapes.medium
                            )
                    ) {
                        DropdownMenu(
                            expanded = showMenu,
                            onDismissRequest = { showMenu = false },
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .background(
                                    colorScheme.primary,
                                )
                        ) {
                            DropdownMenuItem(
                                text = {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = "Sound",
                                            style = MaterialTheme.typography.bodyMedium,
                                            modifier = Modifier.padding(end = 10.dp),
                                            color = colorScheme.secondary,
                                        )
                                        Switch(
                                            checked = hasSound,
                                            onCheckedChange = {
                                                hasSound = it
                                                with(settingsPref.edit()) {
                                                    putBoolean("hasSound", hasSound)
                                                    apply()
                                                }
                                            },
                                            modifier = Modifier
                                                .padding(start = 10.dp)
                                                .height(20.dp),
                                            colors = SwitchDefaults.colors(
                                                checkedThumbColor = colorScheme.primary,
                                                uncheckedThumbColor = colorScheme.secondary,
                                                uncheckedTrackColor = colorScheme.primary,
                                                checkedTrackColor = colorScheme.secondary
                                            ),
                                        )
                                    }
                                },
                                onClick = {
                                    hasSound = !hasSound
                                },
                            )

                            DropdownMenuItem(
                                text = {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = "Vibrate",
                                            style = MaterialTheme.typography.bodyMedium,
                                            modifier = Modifier.padding(end = 10.dp),
                                            color = colorScheme.secondary,
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
                                                checkedThumbColor = colorScheme.primary,
                                                uncheckedThumbColor = colorScheme.secondary,
                                                uncheckedTrackColor = colorScheme.primary,
                                                checkedTrackColor = colorScheme.secondary
                                            ),
                                        )
                                    }
                                },
                                onClick = {
                                    hasHaptics = !hasHaptics
                                },
                            )
                        }
                    }
                    // drop down menu end


                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        AnimatedVisibility(
                            visible = visible,
                            enter = fadeIn(
                                animationSpec = tween(
                                    durationMillis = 250,
                                    easing = LinearEasing
                                )
                            ),
                            exit = fadeOut(
                                animationSpec = tween(
                                    durationMillis = 250,
                                    easing = LinearEasing
                                )
                            )
                        ) {
                            // top bar
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                            )
                            {
                                IconButton(
                                    onClick = {
                                        visible = false
                                        navController.popBackStack()
                                    },
                                    modifier = Modifier
                                        .padding(start = 20.dp, top = 40.dp)
                                        .align(Alignment.TopStart)
                                        .background(
                                            colorScheme.secondary,
                                            shape = MaterialTheme.shapes.extraLarge
                                        )
                                ) {
                                    Icon(
                                        imageVector = Icons.Outlined.KeyboardArrowLeft,
                                        contentDescription = "Back",
                                        tint = colorScheme.primary,
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
                                        tint = colorScheme.secondary,
                                    )
                                }
                            }
                        }
                        // text and counter
                        AnimatedVisibility(
                            visible = visible,
                            enter = fadeIn(
                                animationSpec = tween(
                                    durationMillis = 250,
                                    easing = LinearEasing
                                )
                            ) + slideInVertically(
                                initialOffsetY = { -400 },
                                animationSpec = tween(
                                    durationMillis = 500,
                                    easing = FastOutSlowInEasing
                                )
                            ),
                            exit = fadeOut(
                                animationSpec = tween(
                                    durationMillis = 250,
                                    easing = LinearEasing
                                )
                            ) + slideOutVertically(
                                targetOffsetY = { -400 },
                                animationSpec = tween(
                                    durationMillis = 500,
                                    easing = FastOutSlowInEasing
                                )
                            )
                        ) {
                            Column(
                                modifier = Modifier.wrapContentSize(),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                if (tasbeehName in tasbeehData.longTasbeehs) {
                                    if (tasbeehData.longTasbeehs.filter { it.key == tasbeehName }.values.first()
                                            .toString().contains("http")
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(start = 20.dp, end = 20.dp)
                                        ) {
                                            LoadImageFromUrl(url = tasbeehData.longTasbeehs.filter { it.key == tasbeehName }.values.first().toString())
                                        }
                                    }
                                    else{
                                        Text(
                                            text = tasbeehData.longTasbeehs.filter { it.key == tasbeehName }.values.first().toString(),
                                            style = MaterialTheme.typography.headlineSmall,
                                            modifier = Modifier
                                                .padding(top = 40.dp)
                                                .combinedClickable(
                                                    onClick = {
                                                        Toast
                                                            .makeText(
                                                                context,
                                                                "Long press to hear the tasbeeh",
                                                                Toast.LENGTH_SHORT
                                                            )
                                                            .show()
                                                    },
                                                    onLongClick = {
                                                        tts = TextToSpeech(context) { status ->
                                                            if (status == TextToSpeech.SUCCESS) {
                                                                val result = tts?.setLanguage(
                                                                    Locale.forLanguageTag("ar")
                                                                )
                                                                if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                                                                    tts = null
                                                                } else {
                                                                    tts?.speak(
                                                                        if (tasbeehName in tasbeehData.longTasbeehs)
                                                                            tasbeehData.longTasbeehs.filter { it.key == tasbeehName }.values
                                                                                .first()
                                                                                .toString()
                                                                        else
                                                                            tasbeehName,
                                                                        TextToSpeech.QUEUE_FLUSH,
                                                                        null,
                                                                        null
                                                                    )
                                                                }
                                                            }
                                                        }
                                                    },
                                                ),
                                            color = colorScheme.secondary,
                                            textAlign = TextAlign.Center,
                                        )
                                    }
                                } else {
                                    Text(
                                        text = tasbeehName,
                                        style = if (tasbeehData.longTasbeehs.contains(tasbeehName))
                                            MaterialTheme.typography.headlineSmall
                                        else
                                            MaterialTheme.typography.headlineLarge,
                                        modifier = if (tasbeehData.longTasbeehs.contains(tasbeehName))
                                            Modifier
                                                .padding(top = 40.dp)
                                                .combinedClickable(
                                                    onClick = {
                                                        Toast
                                                            .makeText(
                                                                context,
                                                                "Long press to hear the tasbeeh",
                                                                Toast.LENGTH_SHORT
                                                            )
                                                            .show()
                                                    },
                                                    onLongClick = {
                                                        tts = TextToSpeech(context) { status ->
                                                            if (status == TextToSpeech.SUCCESS) {
                                                                val result = tts?.setLanguage(
                                                                    Locale.forLanguageTag("ar")
                                                                )
                                                                if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                                                                    tts = null
                                                                } else {
                                                                    tts?.speak(
                                                                        if (tasbeehName in tasbeehData.longTasbeehs)
                                                                            tasbeehData.longTasbeehs.filter { it.key == tasbeehName }.values
                                                                                .first()
                                                                                .toString()
                                                                        else
                                                                            tasbeehName,
                                                                        TextToSpeech.QUEUE_FLUSH,
                                                                        null,
                                                                        null
                                                                    )
                                                                }
                                                            }
                                                        }
                                                    },
                                                )
                                        else
                                            Modifier
                                                .padding(top = 30.dp)
                                                .combinedClickable(
                                                    onClick = {
                                                        Toast
                                                            .makeText(
                                                                context,
                                                                "Long press to hear the tasbeeh",
                                                                Toast.LENGTH_SHORT
                                                            )
                                                            .show()
                                                    },
                                                    onLongClick = {
                                                        tts = TextToSpeech(context) { status ->
                                                            if (status == TextToSpeech.SUCCESS) {
                                                                val result = tts?.setLanguage(
                                                                    Locale.forLanguageTag("ar")
                                                                )
                                                                if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                                                                    tts = null
                                                                } else {
                                                                    tts?.speak(
                                                                        tasbeehName,
                                                                        TextToSpeech.QUEUE_FLUSH,
                                                                        null,
                                                                        null
                                                                    )
                                                                }
                                                            }
                                                        }
                                                    },
                                                ),
                                        color = colorScheme.secondary,
                                        textAlign = TextAlign.Center,
                                    )
                                }
                                val textSizeStyle = TextStyle(
                                    color = colorScheme.secondary,
                                    fontSize = 60.sp,
                                    lineHeight = 48.sp,
                                    textAlign = TextAlign.Center,

                                    )
                                Text(
                                    text = counter.toString(),
                                    modifier = if (tasbeehData.shortNames.contains(tasbeehName)) {
                                        Modifier
                                            .padding(top = 30.dp, bottom = 8.dp)
                                            .fillMaxWidth(1f)
                                    } else {
                                        Modifier
                                            .padding(top = 40.dp, bottom = 8.dp)
                                            .fillMaxWidth(1f)
                                    },
                                    color = colorScheme.secondary,
                                    style = textSizeStyle,
                                    maxLines = 1,
                                )
                                Text(
                                    text = "+",
                                    color = colorScheme.secondary,
                                    style = MaterialTheme.typography.bodyLarge,
                                    maxLines = 1,
                                )
                                Text(
                                    text = "$totalCounter",
                                    modifier = Modifier.padding(top = 16.dp, bottom = 8.dp),
                                    color = colorScheme.secondary,
                                    style = MaterialTheme.typography.bodyLarge,
                                    maxLines = 1,
                                )
                                Text(
                                    text = "Limit: ${
                                        if (limit > 0)
                                            limit.toString()
                                        else
                                            "∞"
                                    }",
                                    modifier = Modifier.padding(top = 16.dp, bottom = 10.dp),
                                    color = colorScheme.tertiary,
                                    style = MaterialTheme.typography.bodyMedium,
                                    maxLines = 1,
                                )
                            }
                        }
                        AnimatedVisibility(
                            visible = visible,
                            enter = fadeIn(
                                animationSpec = tween(
                                    durationMillis = 250,
                                    easing = LinearEasing
                                )
                            ) + slideInVertically(
                                initialOffsetY = { 400 },
                                animationSpec = tween(
                                    durationMillis = 500,
                                    easing = FastOutSlowInEasing
                                )
                            ),
                            exit = fadeOut(
                                animationSpec = tween(
                                    durationMillis = 250,
                                    easing = LinearEasing
                                )
                            ) + slideOutVertically(
                                targetOffsetY = { 400 },
                                animationSpec = tween(
                                    durationMillis = 500,
                                    easing = FastOutSlowInEasing
                                )
                            )
                        ) {
                            Box(
                                modifier = Modifier
                                    .padding(16.dp)
                                    .background(
                                        color = colorScheme.primaryContainer,
                                        shape = MaterialTheme.shapes.extraLarge
                                    )
                                    .clickable(
                                        onClick = {
                                            if (hasSound && !limitReached) {
                                                soundPool.play(soundId, 1.0f, 1.0f, 1, 0, 1.0f)
                                            }
                                            try {
                                                if (limit > 0) {
                                                    if (counter < limit && !limitReached) {
                                                        counter++
                                                        with(sharedPref.edit()) {
                                                            putInt(
                                                                tasbeehName,
                                                                totalCounter + counter
                                                            )
                                                            apply()
                                                        }
                                                    } else {
                                                        limitReached = true
                                                        scope.launch {
                                                            snackbarHostState.showSnackbar(
                                                                (if (limitReached)
                                                                    "Limit Reached, kindly reset the counter"
                                                                else
                                                                    "Counter Reset")
                                                            )
                                                        }
                                                    }
                                                } else {
                                                    if (counter < defaultLimit && !limitReached) {
                                                        counter++
                                                        with(sharedPref.edit()) {
                                                            putInt(
                                                                tasbeehName,
                                                                totalCounter + counter
                                                            )
                                                            apply()
                                                        }
                                                    } else {
                                                        limitReached = true
                                                        scope.launch {
                                                            snackbarHostState.showSnackbar(
                                                                (if (limitReached)
                                                                    "Limit Reached, kindly reset the counter"
                                                                else
                                                                    "Counter Reset")
                                                            )
                                                        }
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
                                    Spacer(modifier = Modifier.padding(top = 20.dp))
                                    Box(
                                        modifier = Modifier
                                            .padding(top =
                                            if (tasbeehData.shortNames.contains(tasbeehName))
                                                60.dp
                                            else
                                                40.dp
                                            )
                                            .fillMaxWidth()
                                            .align(Alignment.CenterHorizontally),
                                    ) {
                                        Text(
                                            text = "+1",
                                            modifier = Modifier
                                                .align(Alignment.BottomCenter),
                                            style = MaterialTheme.typography.headlineLarge,
                                            color = colorScheme.onPrimaryContainer,
                                        )
                                    }
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
                                                with(sharedPref.edit()) {
                                                    putInt(tasbeehName, totalCounter + counter)
                                                    apply()
                                                }
                                            }
                                        },
                                        modifier = Modifier
                                            .padding(10.dp)
                                            .background(
                                                color = colorScheme.secondary,
                                                shape = MaterialTheme.shapes.extraLarge
                                            )
                                            .size(50.dp)
                                    ) {
                                        Icon(
                                            imageVector = ImageVector.vectorResource(id = R.drawable.neg1),
                                            contentDescription = "-1",
                                            tint = colorScheme.primary,
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
                                                color = colorScheme.secondary,
                                                shape = MaterialTheme.shapes.extraLarge
                                            )
                                            .size(50.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Outlined.Refresh,
                                            contentDescription = "Reset",
                                            tint = colorScheme.primary,
                                            modifier = Modifier
                                                .size(30.dp)
                                                .combinedClickable(
                                                    onClick = {
                                                        counter = 0
                                                        limitReached = false
                                                    },
                                                    onLongClick = {
                                                        limit = 0
                                                        limitReached = false
                                                        if (hasHaptics) vibrator.vibrate(
                                                            vibrationEffect
                                                        )
                                                    }
                                                ),
                                        )
                                    }
                                    IconButton(
                                        onClick = {
                                            showEditDialog = true
                                        },
                                        modifier = Modifier
                                            .padding(10.dp)
                                            .background(
                                                color = colorScheme.secondary,
                                                shape = MaterialTheme.shapes.extraLarge
                                            )
                                            .size(50.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Outlined.Edit,
                                            contentDescription = "Edit",
                                            tint = colorScheme.primary,
                                            modifier = Modifier.size(30.dp)
                                        )
                                    }
                                    Box(
                                        modifier = Modifier
                                            .padding(10.dp)
                                            .background(
                                                color = colorScheme.secondary,
                                                shape = MaterialTheme.shapes.extraLarge
                                            )
                                            .size(50.dp)
                                    ) {
                                        Text(
                                            text = "Limit",
                                            color = colorScheme.primary,
                                            modifier = Modifier
                                                .padding(5.dp)
                                                .align(Alignment.Center)
                                                .clickable { showLimitDialog = true },
                                            style = MaterialTheme.typography.bodyMedium,
                                            fontWeight = FontWeight.ExtraBold,
                                        )
                                    }
                                }
                            }
                        }
                    }
                    if (showEditDialog) {
                        var editableCounter by remember {
                            mutableStateOf("")
                        }
                        Dialog(
                            onDismissRequest = {
                                showEditDialog = false
                            }
                        ) {
                            Column(
                                modifier = Modifier
                                    .padding(50.dp)
                                    .fillMaxSize(),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Spacer(modifier = Modifier.padding(35.dp))
                                OutlinedTextField(
                                    value = editableCounter,
                                    onValueChange = {
                                        editableCounter = it
                                    },
                                    label = {
                                        Text(
                                            "Edit Count",
                                            style = MaterialTheme.typography.bodyLarge,
                                            color = colorScheme.inversePrimary
                                        )
                                    },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(top = 80.dp),
                                    keyboardOptions = KeyboardOptions(
                                        keyboardType = KeyboardType.Number,
                                        imeAction = ImeAction.Done,
                                    ),
                                    textStyle = MaterialTheme.typography.headlineLarge,

                                    colors = TextFieldDefaults.textFieldColors(
                                        containerColor = colorScheme.background,
                                        focusedIndicatorColor = colorScheme.background,
                                        unfocusedIndicatorColor = colorScheme.background,
                                        cursorColor = colorScheme.secondary,
                                        textColor = colorScheme.secondary,
                                    ),
                                    maxLines = 1,
                                    singleLine = true,
                                )
                                Spacer(modifier = Modifier.padding(10.dp))
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceEvenly
                                ) {
                                    IconButton(
                                        onClick = {
                                            showEditDialog = false
                                            counter = if (editableCounter.isNotEmpty()) {
                                                editableCounter.toInt()
                                            } else {
                                                counter
                                            }
                                        },
                                        modifier = Modifier
                                            .padding(5.dp)
                                            .background(
                                                Color.Transparent,
                                                shape = MaterialTheme.shapes.extraLarge,
                                            )
                                    ) {
                                        Icon(
                                            imageVector = Icons.Outlined.Done,
                                            contentDescription = "Done",
                                            tint = colorScheme.primary,
                                            modifier = Modifier.size(50.dp)
                                        )
                                    }
                                    IconButton(
                                        onClick = {
                                            showEditDialog = false
                                        },
                                        modifier = Modifier
                                            .padding(5.dp)
                                            .background(
                                                Color.Transparent,
                                                shape = MaterialTheme.shapes.extraLarge,
                                            )
                                    ) {
                                        Icon(
                                            imageVector = Icons.Outlined.Close,
                                            contentDescription = "Close",
                                            tint = colorScheme.tertiary,
                                            modifier = Modifier.size(50.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                    if (showLimitDialog) {
                        var editableCounter by remember {
                            mutableStateOf("")
                        }
                        Dialog(
                            onDismissRequest = {
                                showLimitDialog = false
                            }
                        ) {
                            Column(
                                modifier = Modifier
                                    .padding(50.dp)
                                    .fillMaxSize(),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Spacer(modifier = Modifier.padding(35.dp))
                                OutlinedTextField(
                                    value = editableCounter,
                                    onValueChange = {
                                        editableCounter = it
                                    },
                                    label = {
                                        Text(
                                            "Set Limit",
                                            style = MaterialTheme.typography.bodyLarge,
                                            color = colorScheme.inversePrimary
                                        )
                                    },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(top = 80.dp),
                                    keyboardOptions = KeyboardOptions(
                                        keyboardType = KeyboardType.Number,
                                        imeAction = ImeAction.Done,
                                    ),
                                    textStyle = MaterialTheme.typography.headlineLarge,

                                    colors = TextFieldDefaults.textFieldColors(
                                        containerColor = colorScheme.background,
                                        focusedIndicatorColor = colorScheme.background,
                                        unfocusedIndicatorColor = colorScheme.background,
                                        cursorColor = colorScheme.secondary,
                                        textColor = colorScheme.secondary,
                                    ),
                                    maxLines = 1,
                                    singleLine = true,
                                )
                                Spacer(modifier = Modifier.padding(10.dp))
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceEvenly
                                ) {
                                    IconButton(
                                        onClick = {
                                            showLimitDialog = false
                                            limit =
                                                if (editableCounter.isNotEmpty() && editableCounter.toInt() > 0) {
                                                    editableCounter.toInt()
                                                } else {
                                                    defaultLimit
                                                }
                                        },
                                        modifier = Modifier
                                            .padding(5.dp)
                                            .background(
                                                Color.Transparent,
                                                shape = MaterialTheme.shapes.extraLarge,
                                            )
                                    ) {
                                        Icon(
                                            imageVector = Icons.Outlined.Done,
                                            contentDescription = "Done",
                                            tint = colorScheme.primary,
                                            modifier = Modifier.size(50.dp)
                                        )
                                    }
                                    IconButton(
                                        onClick = {
                                            showLimitDialog = false
                                        },
                                        modifier = Modifier
                                            .padding(5.dp)
                                            .background(
                                                Color.Transparent,
                                                shape = MaterialTheme.shapes.extraLarge,
                                            )
                                    ) {
                                        Icon(
                                            imageVector = Icons.Outlined.Close,
                                            contentDescription = "Close",
                                            tint = colorScheme.tertiary,
                                            modifier = Modifier.size(50.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                    LaunchedEffect(true) {
                        visible = true
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
    BackHandler {
        visible = false
        navController.popBackStack()
    }
}