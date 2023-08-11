package com.lamaq.tasbeeh.components

import android.content.Context
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp

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
            .clickable {
                onItemClick(
                    "tasbeeh/${tasbeehData.key}/${sharedPref.getInt(tasbeehData.key, 0)}",
                    null
                )
            }
            .padding(16.dp),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = tasbeehData.key,
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = "Count: ${sharedPref.getInt(tasbeehData.key, 0)}",
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}

