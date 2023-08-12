package com.lamaq.tasbeeh.components

import android.content.Context
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.Done
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.lamaq.tasbeeh.ui.theme.DarkColorScheme
import com.lamaq.tasbeeh.ui.theme.Typography

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun TasbeehCards(
    tasbeehData: String,
    onItemClick: (String, Any?) -> Unit,
) {
    val context = LocalContext.current
    val sharedPref = context.getSharedPreferences(
        "tasbeehs",
        Context.MODE_PRIVATE
    )

    var showEditDialog by remember {
        mutableStateOf(false)
    }

    var totalCount by remember {
        mutableStateOf("")
    }
    totalCount = sharedPref.getInt(tasbeehData, 0).toString()

    Card(
        modifier = Modifier
            .padding(16.dp)
            .width(280.dp)
            .height(300.dp)
            .background(
                color = DarkColorScheme.primary,
                shape = MaterialTheme.shapes.medium
            ),
        elevation = CardDefaults.elevatedCardElevation(
            defaultElevation = 8.dp,
        ),
        colors = CardDefaults.cardColors(
            containerColor = DarkColorScheme.primary,
            contentColor = DarkColorScheme.onPrimary,
        ),
    ) {
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
                    Spacer(modifier = Modifier.padding(10.dp))
                    OutlinedTextField(
                        value = editableCounter,
                        onValueChange = {
                            editableCounter = it
                        },
                        label = {
                            Text(
                                "Enter Start Count",
                                style = MaterialTheme.typography.bodyLarge,
                                color = DarkColorScheme.inversePrimary
                            )
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 90.dp),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number,
                            imeAction = ImeAction.Done,
                        ),
                        textStyle = MaterialTheme.typography.headlineLarge,

                        colors = TextFieldDefaults.textFieldColors(
                            containerColor = DarkColorScheme.background,
                            focusedIndicatorColor = DarkColorScheme.background,
                            unfocusedIndicatorColor = DarkColorScheme.background,
                            cursorColor = DarkColorScheme.secondary,
                            textColor = DarkColorScheme.secondary,
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
                                totalCount = editableCounter.ifEmpty {
                                    totalCount
                                }

                                with(sharedPref.edit()) {
                                    putInt(tasbeehData, totalCount.toInt())
                                    apply()
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
                                tint = DarkColorScheme.primary,
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
                                tint = DarkColorScheme.tertiary,
                                modifier = Modifier.size(50.dp)
                            )
                        }
                    }
                }
            }
        }
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = tasbeehData,
                style = Typography.headlineLarge,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                textAlign = TextAlign.Center,
                color = DarkColorScheme.secondary
            )
            Box(
                modifier = Modifier.combinedClickable(
                    onClick = {},
                    onLongClick = {
                        showEditDialog = true
                    }
                )
            ) {
                Text(
                    text = totalCount,
                    style = MaterialTheme.typography.headlineMedium,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    textAlign = TextAlign.Center,
                    color = DarkColorScheme.secondary,
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            ElevatedButton(
                onClick = {
                    onItemClick(
                        "tasbeeh/${tasbeehData}/${sharedPref.getInt(tasbeehData, 0)}",
                        null
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                colors = ButtonDefaults.elevatedButtonColors(
                    containerColor = DarkColorScheme.tertiary,
                    contentColor = DarkColorScheme.onTertiary
                ),
            ) {
                Text(
                    text = "تسبیح",
                    color = DarkColorScheme.onTertiary,
                    style = Typography.bodyLarge,
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TasbeehCardsPreview() {
    TasbeehCards(
        tasbeehData = "صلوات",
        onItemClick = { _, _ -> }
    )
}

