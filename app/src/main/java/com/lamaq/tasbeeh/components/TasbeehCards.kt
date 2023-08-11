package com.lamaq.tasbeeh.components

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lamaq.tasbeeh.ui.theme.DarkColorScheme
import com.lamaq.tasbeeh.ui.theme.Typography

@Composable
fun TasbeehCards(
    tasbeehData: Map.Entry<String, Int>,
    onItemClick: (String, Any?) -> Unit
) {
    val context = LocalContext.current
    val sharedPref = context.getSharedPreferences(
        "tasbeehs",
        Context.MODE_PRIVATE
    )

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
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = tasbeehData.key,
                style = Typography.headlineLarge,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                textAlign = TextAlign.Center,
                color = DarkColorScheme.secondary
            )
            Text(
                text = sharedPref.getInt(tasbeehData.key, 0).toString(),
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                textAlign = TextAlign.Center,
                color = DarkColorScheme.secondary,
            )
            Spacer(modifier = Modifier.height(8.dp))
            ElevatedButton(
                onClick = {
                    onItemClick(
                        "tasbeeh/${tasbeehData.key}/${sharedPref.getInt(tasbeehData.key, 0)}",
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
        tasbeehData = mapOf("Tasbeeh" to 0).entries.first(),
        onItemClick = { _, _ -> }
    )
}

