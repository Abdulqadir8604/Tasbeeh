package com.lamaq.tasbeeh.widget

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.glance.Button
import androidx.glance.ButtonDefaults
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.action.ActionParameters
import androidx.glance.action.actionStartActivity
import androidx.glance.action.clickable
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.action.ActionCallback
import androidx.glance.appwidget.action.actionRunCallback
import androidx.glance.appwidget.cornerRadius
import androidx.glance.appwidget.provideContent
import androidx.glance.appwidget.state.updateAppWidgetState
import androidx.glance.background
import androidx.glance.currentState
import androidx.glance.layout.Alignment
import androidx.glance.layout.Column
import androidx.glance.layout.Row
import androidx.glance.layout.Spacer
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.padding
import androidx.glance.layout.size
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import androidx.glance.unit.ColorProvider
import com.lamaq.tasbeeh.MainActivity
import com.lamaq.tasbeeh.ui.theme.DarkColorScheme

object CounterWidget : GlanceAppWidget() {
    val countKey = intPreferencesKey("count")
    override suspend fun provideGlance(context: Context, id: GlanceId) {
        // Load data needed to render the AppWidget.
        // Use `withContext` to switch to another thread for long running
        // operations.

        provideContent {
            // create your AppWidget here
            MyContent()
        }
    }

    @Composable
    private fun MyContent() {
        val count = currentState(key = countKey) ?: 0
        Column(
            modifier = GlanceModifier
                .fillMaxSize()
                .background(DarkColorScheme.secondary)
                .padding(16.dp)
                .cornerRadius(20.dp)
                .clickable(
                    actionStartActivity<MainActivity>()
                )
            ,
            verticalAlignment = Alignment.Vertical.CenterVertically,
            horizontalAlignment = Alignment.Horizontal.CenterHorizontally
        ) {
            Text(
                text = count.toString(),
                style = TextStyle(
                    fontWeight = FontWeight.Medium,
                    color = ColorProvider(DarkColorScheme.primary),
                    fontSize = 40.sp
                ),
            )
            Row (
                modifier = GlanceModifier.padding(16.dp),
                verticalAlignment = Alignment.Vertical.CenterVertically,
            ) {
                Button(
                    text = "R",
                    style = TextStyle(
                        fontWeight = FontWeight.Medium,
                        color = ColorProvider(DarkColorScheme.primary),
                        fontSize = 18.sp
                    ),
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = ColorProvider(DarkColorScheme.primaryContainer)
                    ),
                    onClick = actionRunCallback(ResetActionCallBack::class.java),
                    modifier = GlanceModifier.padding(16.dp).size(50.dp),
                )
                Spacer(modifier = GlanceModifier.padding(4.dp))
                Button(
                    text = "+1",
                    style = TextStyle(
                        fontWeight = FontWeight.Bold,
                        color = ColorProvider(DarkColorScheme.secondary),
                        fontSize = 26.sp,
                    ),
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = ColorProvider(DarkColorScheme.primary)
                    ),
                    onClick = actionRunCallback(IncrementActionCallback::class.java),
                    modifier = GlanceModifier.padding(16.dp).size(70.dp)
                )
                Spacer(modifier = GlanceModifier.padding(4.dp))
                Button(
                    text = "-1",
                    style = TextStyle(
                        fontWeight = FontWeight.Medium,
                        color = ColorProvider(DarkColorScheme.primary),
                        fontSize = 22.sp
                    ),
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = ColorProvider(DarkColorScheme.primaryContainer)
                    ),
                    onClick = actionRunCallback(DecrementActionCallback::class.java),
                    modifier = GlanceModifier.padding(16.dp).size(50.dp)
                )
            }
        }
    }
}

class IncrementActionCallback: ActionCallback {
    override suspend fun onAction(
        context: Context,
        glanceId: GlanceId,
        parameters: ActionParameters
    ) {
        updateAppWidgetState(context, glanceId) { prefs ->
            val currentCount = prefs[CounterWidget.countKey]
            if(currentCount != null) {
                prefs[CounterWidget.countKey] = currentCount + 1
            } else {
                prefs[CounterWidget.countKey] = 1
            }
        }
        CounterWidget.update(context, glanceId)
    }
}

class ResetActionCallBack: ActionCallback {
    override suspend fun onAction(
        context: Context,
        glanceId: GlanceId,
        parameters: ActionParameters
    ) {
        updateAppWidgetState(context, glanceId) { prefs ->
            val currentCount = prefs[CounterWidget.countKey]
            if(currentCount != null) {
                prefs[CounterWidget.countKey] = 0
            } else {
                prefs[CounterWidget.countKey] = 1
            }
        }
        CounterWidget.update(context, glanceId)
    }
}

class DecrementActionCallback : ActionCallback {
    override suspend fun onAction(
        context: Context,
        glanceId: GlanceId,
        parameters: ActionParameters
    ) {
        updateAppWidgetState(context, glanceId) { prefs ->
            val currentCount = prefs[CounterWidget.countKey]
            if (currentCount != null) {
                prefs[CounterWidget.countKey] = if (currentCount > 0) currentCount - 1 else 0
            } else {
                prefs[CounterWidget.countKey] = 1
            }
        }
        CounterWidget.update(context, glanceId)
    }
}

