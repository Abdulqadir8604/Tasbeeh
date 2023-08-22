package com.lamaq.tasbeeh.presentation.onboarding.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import com.lamaq.tasbeeh.presentation.onboarding.Dimens.IndicatorSize
import com.lamaq.tasbeeh.ui.theme.DarkColorScheme

@Composable
fun PageIndicator(
    modifier: Modifier = Modifier,
    pagesSize: Int,
    selectedPage: Int,
    selectedColor: Color = DarkColorScheme.secondary,
    unselectedColor: Color = DarkColorScheme.primary,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        repeat(pagesSize) {
            Box(modifier = Modifier
                .size(IndicatorSize)
                .clip(CircleShape)
                .background(
                    color = if (it == selectedPage) selectedColor else unselectedColor
                )
            )
        }
    }
}